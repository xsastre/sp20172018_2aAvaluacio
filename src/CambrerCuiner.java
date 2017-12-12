/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author xavier
 */
import java.util.Date;
 
public class CambrerCuiner {
 
    public static void main(String[] args) {
        Comanda comanda = new Comanda();
        Cambrer cambrer = new Cambrer(comanda);
        Thread filCambrer = new Thread(cambrer, "FilCambre");
        filCambrer.start();

        Cuiner cuiner = new Cuiner(comanda);
        Thread filCuiner = new Thread(cuiner, "FilCuiner");
        filCuiner.start();
    }
}
 
class Cuiner implements Runnable {
 
    Comanda ordreComanda;
 
    public Cuiner(Comanda ordreComanda) {
        this.ordreComanda = ordreComanda;
    }
 
    public void ferComanda(){
        synchronized (ordreComanda) {
            while (ordreComanda.countComada == 0) {
                try {
                    System.out.println("Cuiner està esperant una comanda a data: " + new Date());
                    ordreComanda.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try{
            ordreComanda.setPlat("Llenties "+ordreComanda.countComada);
            ordreComanda.countComada = ordreComanda.countComada- 1;
            Thread.sleep(4000);
            System.out.println("Cuiner té la comanda i comença a fer la comanda de "+ordreComanda.getPlat()+" a data: " + new Date());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
 
    @Override
    public void run() {
        while (true) {
            ferComanda();
        }
 
    }
 
}
 
class Cambrer implements Runnable {
 
    Comanda ordreComanda;

    public Cambrer(Comanda ordreComanda) {
        this.ordreComanda = ordreComanda;
    }
    public void prenComanda(){
    //System.out.println("Cambre toma nota al client triga 3 segons en respondre a data: " + new Date());
        synchronized (ordreComanda) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            ordreComanda.countComada += 1;
            ordreComanda.setPlat("Llenties "+ordreComanda.countComada);
            System.out.println("Cambrer anota comanda i ho notifica al cuiner "+ordreComanda.getPlat()+" a data " + new Date()+ " i notifica al cuiner.");
            ordreComanda.notifyAll();
        }
    }
    public void run() {
        try{
            for (int x=0;x<10;x++) {
                int espera=((int)Math.random()*1000)+1000;
                Thread.sleep(espera);
                prenComanda();
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }
 
}
 
 
class Comanda {
 
    private String plat="";
    volatile int countComada = 0;

    public String getPlat() {
        return plat;
    }

    public void setPlat(String text) {
        this.plat = text;
    }
 
}