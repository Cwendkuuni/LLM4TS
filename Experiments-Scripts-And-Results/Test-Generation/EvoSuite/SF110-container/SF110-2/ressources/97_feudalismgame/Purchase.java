package src;

import java.util.*;
import java.lang.reflect.*;

public class Purchase extends Actions
{
    @Override
    public void perform(final Collection args) {
        try {
            final Iterator argsIter = args.iterator();
            this.getClass();
            final Class aMethod = Class.forName("feudalism.Purchase");
            final Class[] argType = { String.class };
            final Method methodObj = aMethod.getMethod(argsIter.next(), Iterator.class);
            methodObj.invoke(this, argsIter);
            GameAutoActions.saveAll();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        }
        catch (IllegalAccessException e4) {
            e4.printStackTrace();
        }
        catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        catch (ClassNotFoundException e6) {
            e6.printStackTrace();
        }
    }
    
    public void vassal(final Iterator arg) {
        final int qty = Integer.parseInt(arg.next());
        final String buyerName = arg.next();
        final Fiefdoms fiefdom = Map.map.get(arg.next());
        final Knight buyerObj = CurrentPlayers.currentPlayers.get(buyerName);
        if (fiefdom.getOwner() != null) {
            if (buyerObj.getRank().equalsIgnoreCase("king") || buyerObj.isFiefdomMine(fiefdom.getName())) {
                if (buyerObj.setTotalMoney(qty, false)) {
                    fiefdom.setLoyalVassals(qty, true);
                }
                else {
                    System.out.println("You cannot buy more vassals, you have insufficient funds");
                }
            }
            else if (CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("count") || CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("duke")) {
                System.out.println("You can only buy a castle for a fiefdom that belongs to you");
            }
        }
        else {
            System.out.println("Fiefdom doesn't belong to the kingdom");
        }
    }
    
    public void castleFiefdom(final Iterator arg) {
        final int qty = Integer.parseInt(arg.next());
        final String buyerName = arg.next();
        final Fiefdoms fiefdom = Map.map.get(arg.next());
        final Knight buyerObj = CurrentPlayers.currentPlayers.get(buyerName);
        if (fiefdom.getOwner() != null) {
            if (buyerObj.getRank().equalsIgnoreCase("king") || (buyerObj.isFiefdomMine(fiefdom.getName()) && fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size() == 0)) {
                if (buyerObj.setTotalMoney(qty * 25, false)) {
                    fiefdom.buyFiefdomCastles(qty);
                }
                else {
                    System.out.println("You cannot buy more Fiefdom castles, you have insufficient funds");
                }
            }
            else if (CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("count") || CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("duke")) {
                System.out.println("You can only buy a castle for a fiefdom that belongs to you");
            }
        }
        else {
            System.out.println("Fiefdom doesn't belong to the kingdom");
        }
    }
    
    public void castlePersonal(final Iterator arg) {
        final int qty = Integer.parseInt(arg.next());
        final String buyerName = arg.next();
        final Fiefdoms fiefdom = Map.map.get(arg.next());
        final Knight buyerObj = CurrentPlayers.currentPlayers.get(buyerName);
        if (fiefdom.getOwner() != null) {
            if ((buyerObj.getRank().equalsIgnoreCase("king") && CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("knight")) || (buyerObj.isFiefdomMine(fiefdom.getName()) && fiefdom.getViking().size() + fiefdom.getMagyar().size() + fiefdom.getMuslim().size() == 0)) {
                if (buyerObj.setTotalMoney(qty * 10, false)) {
                    fiefdom.buyPersonalCastles(qty);
                }
                else {
                    System.out.println("You cannot buy more Personal castle, you have insufficient funds");
                }
            }
            else if (buyerObj.getRank().equalsIgnoreCase("king")) {
                System.out.println("You cannot buy a Personal Castle for Counts or Dukes");
            }
            else if (CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("count") || CurrentPlayers.currentPlayers.get(fiefdom.getOwner()).getRank().equalsIgnoreCase("duke")) {
                System.out.println("You can only buy a castle for a fiefdom that belongs to you");
            }
        }
        else {
            System.out.println("Fiefdom doesn't belong to the kingdom: " + fiefdom.getName() + " owner: " + Map.map.get(fiefdom.getName()).getOwner());
        }
    }
    
    public void mercenary(final Iterator arg) {
        final int qty = Integer.parseInt(arg.next());
        final String buyerName = arg.next();
        final Knight knight = CurrentPlayers.currentPlayers.get(arg.next());
        final Knight buyerObj = CurrentPlayers.currentPlayers.get(buyerName);
        if (CurrentPlayers.currentPlayers.containsKey(buyerObj.getName()) && CurrentPlayers.currentPlayers.containsKey(knight.getName())) {
            if (buyerObj.getName().equals(knight.getName()) || buyerObj.getRank().equals("king")) {
                if (buyerObj.setTotalMoney(qty, false)) {
                    knight.addMercenaries(qty);
                }
                else {
                    System.out.println("You cannot buy more mercenaries, you have insufficient funds");
                }
            }
        }
        else {
            System.out.println("Inexistent Knight");
        }
    }
}
