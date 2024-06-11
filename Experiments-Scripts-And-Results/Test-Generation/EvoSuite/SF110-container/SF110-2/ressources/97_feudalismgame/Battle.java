package src;

import java.lang.reflect.*;
import java.util.*;

public class Battle extends Actions
{
    @Override
    public void perform(final Collection args) {
        try {
            final Iterator argsIter = args.iterator();
            this.getClass();
            final Class aMethod = Class.forName("feudalism.Battle");
            final Class[] argType = { String.class };
            final Method methodObj = aMethod.getMethod(argsIter.next(), Collection.class);
            methodObj.invoke(this, args);
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
    
    public int[] rollDice(final int diceQty) {
        final int[] results = new int[6];
        final Random rand = new Random();
        int die = 0;
        for (int i = 0; i < diceQty; ++i) {
            die = rand.nextInt(6);
            final int[] array = results;
            final int n = die;
            ++array[n];
        }
        return results;
    }
    
    public void vassal(final Collection args) {
        int attackersDice = 0;
        int defendersDice = 0;
        ArrayList<Player> allies = new ArrayList<Player>();
        final Iterator argsIter = args.iterator();
        argsIter.next();
        final Knight attackingKnight = CurrentPlayers.currentPlayers.get(argsIter.next());
        final Fiefdoms fief = Map.map.get(attackingKnight.getLocation());
        if (fief.getRebelliousVassals().size() > 0) {
            if (attackingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
                final ArrayList<Vassals> rebelliousVassals = fief.getRebelliousVassals();
                allies = attackingKnight.getAllies();
                attackingKnight.allyTo(attackingKnight.getName());
                if (allies.size() > 0) {
                    for (final Player soldier : allies) {
                        if (soldier.getRank().equalsIgnoreCase("knight")) {
                            attackersDice += 3;
                        }
                        if (soldier.getRank().equalsIgnoreCase("count")) {
                            attackersDice += 4;
                        }
                        if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                            attackersDice += 6;
                        }
                        if (soldier.getRank().equalsIgnoreCase("vassal")) {
                            ++attackersDice;
                        }
                    }
                }
                attackersDice += fief.getLoyalVassals().size();
                defendersDice = fief.getRebelliousVassals().size();
                System.out.println("Attacker's dice: " + attackersDice);
                System.out.println("Defender's dice: " + defendersDice);
                while (allies.size() > 0 && rebelliousVassals.size() > 0) {
                    final int[] attackers = this.rollDice(attackersDice);
                    final int[] defenders = this.rollDice(defendersDice);
                    for (int i = 1; i < 6; ++i) {
                        if (allies.size() > 0 && rebelliousVassals.size() > 0) {
                            System.out.println(i + " Rebellious: " + rebelliousVassals.size());
                            System.out.println(i + " Allied Forces: " + allies.size());
                            System.out.println("Allies has thrown " + attackers[i] + " " + i + "'s");
                            System.out.println("Rebellious Vassals has thrown " + defenders[i] + " " + i + "'s");
                            attackers[i] = ((attackers[i] < rebelliousVassals.size()) ? attackers[i] : rebelliousVassals.size());
                            defenders[i] = ((defenders[i] < allies.size()) ? defenders[i] : allies.size());
                            switch (i) {
                                case 3: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        rebelliousVassals.remove(0);
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = new Random().nextInt(allies.size());
                                        final Player soldier2 = allies.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.kill();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 4: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            rebelliousVassals.remove(0);
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                            final Player soldier2 = allies.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.kill();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    allies.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                                case 1: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        rebelliousVassals.get(0).wound();
                                        rebelliousVassals.remove(0);
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier2 = allies.get(rand);
                                        System.out.println(c);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.wound();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.wound();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            rebelliousVassals.get(0).wound();
                                            rebelliousVassals.remove(0);
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        for (int d = 0; d < defenders[i]; ++d) {
                                            final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                            System.out.println("n: " + rand);
                                            final Player soldier2 = allies.get(rand);
                                            System.out.println("got a " + soldier2.getRank());
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.wound();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    allies.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                            else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                                soldier2.wound();
                                                allies.remove(rand);
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (allies.size() > rebelliousVassals.size()) {
                    System.out.println("Allied forces won the battle " + rebelliousVassals.size());
                    for (final Player vassal : allies) {
                        if (vassal instanceof Vassals) {
                            vassal.wound();
                        }
                    }
                }
                else if (allies.size() < rebelliousVassals.size() && CurrentPlayers.currentPlayers.get(fief.getOwner()).getDead()) {
                    fief.setName(CurrentPlayers.getKing().getName());
                }
            }
        }
        else {
            System.out.println("There is no vassal rebellion in your fiefdom");
        }
    }
    
    public void peasant(final Collection args) {
        int attackersDice = 0;
        int defendersDice = 0;
        final Iterator argsIter = args.iterator();
        argsIter.next();
        final Knight attackingKnight = CurrentPlayers.currentPlayers.get(argsIter.next());
        final Fiefdoms fief = Map.map.get(attackingKnight.getLocation());
        ArrayList<Player> allies = new ArrayList<Player>();
        if (fief.getPeasants() > 0) {
            if (attackingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
                int peasantArmy = fief.getPeasants();
                allies = attackingKnight.getAllies();
                attackingKnight.allyTo(attackingKnight.getName());
                System.out.println("Attacker's dice: " + attackersDice);
                if (allies.size() > 0) {
                    for (final Player soldier : allies) {
                        if (soldier.getRank().equalsIgnoreCase("knight")) {
                            attackersDice += 3;
                        }
                        if (soldier.getRank().equalsIgnoreCase("count")) {
                            attackersDice += 4;
                        }
                        if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                            attackersDice += 6;
                        }
                        if (soldier.getRank().equalsIgnoreCase("vassal")) {
                            ++attackersDice;
                        }
                    }
                }
                attackersDice += fief.getLoyalVassals().size();
                defendersDice = 1;
                while (allies.size() > 0 && peasantArmy > 0) {
                    final int[] attackers = this.rollDice(attackersDice);
                    final int[] defenders = this.rollDice(defendersDice);
                    for (int i = 1; i < 6; ++i) {
                        if (allies.size() > 0 && peasantArmy > 0) {
                            System.out.println(i + " Peasants: " + peasantArmy);
                            System.out.println(i + " Allied Forces: " + allies.size());
                            System.out.println("Allies has thrown " + attackers[i] + " " + i + "'s");
                            System.out.println("Rebellious Vassals has thrown " + defenders[i] + " " + i + "'s");
                            attackers[i] = ((attackers[i] < peasantArmy) ? attackers[i] : peasantArmy);
                            defenders[i] = ((defenders[i] < allies.size()) ? defenders[i] : allies.size());
                            switch (i) {
                                case 1: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        --peasantArmy;
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier2 = allies.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.kill();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            --peasantArmy;
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        final int rand2 = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier3 = allies.get(rand2);
                                        if (soldier3 instanceof Knight) {
                                            if (((Knight)soldier3).getWounds() + ((Knight)soldier3).getKills() < 3) {
                                                soldier3.kill();
                                                System.out.println("Got a knight");
                                            }
                                            else {
                                                allies.remove(rand2);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                                case 3: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        --peasantArmy;
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier2 = allies.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() < 3) {
                                                soldier2.wound();
                                                System.out.println("Got a Knight");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.wound();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 4: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            --peasantArmy;
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        final int rand2 = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier3 = allies.get(rand2);
                                        if (soldier3 instanceof Knight) {
                                            if (((Knight)soldier3).getWounds() + ((Knight)soldier3).getKills() < 3) {
                                                soldier3.kill();
                                                System.out.println("Got a Knight");
                                            }
                                            else {
                                                allies.remove(rand2);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (allies.size() > peasantArmy) {
                    System.out.println("Allied forces won the battle " + peasantArmy);
                    for (final Player vassal : allies) {
                        if (vassal instanceof Vassals) {
                            vassal.wound();
                        }
                    }
                }
                else {
                    System.out.println("Peasants won the battle " + allies.size());
                }
            }
        }
        else {
            System.out.println("There is no peasant uprising in this fiefdom");
        }
    }
    
    public void viking(final Collection args) {
        int attackersDice = 0;
        int defendersDice = 0;
        final Iterator argsIter = args.iterator();
        argsIter.next();
        final Knight attackingKnight = CurrentPlayers.currentPlayers.get(argsIter.next());
        final Fiefdoms fief = Map.map.get(attackingKnight.getLocation());
        System.out.println(fief.getLoyalVassals().size());
        ArrayList<Player> allies = new ArrayList<Player>();
        if (fief.getViking().size() > 0) {
            if (attackingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
                final ArrayList<Intruders> viking = fief.getViking();
                attackingKnight.allyTo(attackingKnight.getName());
                allies = attackingKnight.getAllies();
                if (allies.size() > 0) {
                    for (final Player soldier : allies) {
                        if (soldier.getRank().equalsIgnoreCase("knight")) {
                            attackersDice += 3;
                        }
                        if (soldier.getRank().equalsIgnoreCase("count")) {
                            attackersDice += 4;
                        }
                        if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                            attackersDice += 6;
                        }
                        if (soldier.getRank().equalsIgnoreCase("vassal")) {
                            ++attackersDice;
                        }
                    }
                }
                defendersDice = viking.size();
                System.out.println("\nAttacker's dice: " + attackersDice);
                System.out.println("Defender's dice: " + defendersDice);
                while (allies.size() > 0 && viking.size() > 0) {
                    final int[] attackers = this.rollDice(attackersDice);
                    final int[] defenders = this.rollDice(defendersDice);
                    for (int i = 1; i < 6; ++i) {
                        if (allies.size() > 0 && viking.size() > 0) {
                            System.out.println(i + " Rebellious: " + viking.size());
                            System.out.println(i + " Allied Forces: " + allies.size());
                            System.out.println("Allies has thrown " + attackers[i] + " " + i + "'s");
                            System.out.println("Rebellious Vassals has thrown " + defenders[i] + " " + i + "'s");
                            attackers[i] = ((attackers[i] < viking.size()) ? attackers[i] : viking.size());
                            defenders[i] = ((defenders[i] < allies.size()) ? defenders[i] : allies.size());
                            switch (i) {
                                case 1: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        viking.remove(0);
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = new Random().nextInt(allies.size());
                                        final Player soldier2 = allies.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.kill();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            viking.remove(0);
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        for (int c = 0; c < attackers[i]; ++i) {
                                            final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                            final Player soldier2 = allies.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.kill();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    allies.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                                case 3: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        viking.get(0).wound();
                                        viking.remove(0);
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier2 = allies.get(rand);
                                        System.out.println(c);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.wound();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.wound();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 4: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            viking.get(0).wound();
                                            viking.remove(0);
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        for (int d = 0; d < defenders[i]; ++d) {
                                            final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                            System.out.println("n: " + rand);
                                            final Player soldier2 = allies.get(rand);
                                            System.out.println("got a " + soldier2.getRank());
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.wound();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    allies.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                            else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                                soldier2.wound();
                                                allies.remove(rand);
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (allies.size() > viking.size()) {
                    System.out.println("Allied forces won the battle " + viking.size());
                    for (final Player vassal : allies) {
                        if (vassal instanceof Vassals) {
                            vassal.wound();
                        }
                    }
                }
                else {
                    System.out.println("Rebellious Vassals won the battle " + allies.size());
                }
            }
        }
        else {
            System.out.println("There is no Viking invasion in this fiefdom");
        }
    }
    
    public void magyar(final Collection args) {
        int attackersDice = 0;
        int defendersDice = 0;
        final Iterator argsIter = args.iterator();
        argsIter.next();
        final Knight attackingKnight = CurrentPlayers.currentPlayers.get(argsIter.next());
        final Fiefdoms fief = Map.map.get(attackingKnight.getLocation());
        if (fief.getMagyar().size() > 0) {
            if (attackingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
                final ArrayList<Intruders> magyar = fief.getMagyar();
                attackingKnight.allyTo(attackingKnight.getName());
                final ArrayList<Player> allies = attackingKnight.getAllies();
                if (allies.size() > 0) {
                    for (final Player soldier : allies) {
                        if (soldier.getRank().equalsIgnoreCase("knight")) {
                            attackersDice += 3;
                        }
                        if (soldier.getRank().equalsIgnoreCase("count")) {
                            attackersDice += 4;
                        }
                        if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                            attackersDice += 6;
                        }
                        if (soldier.getRank().equalsIgnoreCase("vassal")) {
                            ++attackersDice;
                        }
                    }
                }
                defendersDice = magyar.size();
                System.out.println("Attacker's dice: " + attackersDice);
                System.out.println("Defender's dice: " + defendersDice);
                while (allies.size() > 0 && magyar.size() > 0) {
                    final int[] attackers = this.rollDice(attackersDice);
                    final int[] defenders = this.rollDice(defendersDice);
                    for (int i = 1; i < 6; ++i) {
                        if (allies.size() > 0 && magyar.size() > 0) {
                            System.out.println(i + " Rebellious: " + magyar.size());
                            System.out.println(i + " Allied Forces: " + allies.size());
                            System.out.println("Allies has thrown " + attackers[i] + " " + i + "'s");
                            System.out.println("Rebellious Vassals has thrown " + defenders[i] + " " + i + "'s");
                            attackers[i] = ((attackers[i] < magyar.size()) ? attackers[i] : magyar.size());
                            defenders[i] = ((defenders[i] < allies.size()) ? defenders[i] : allies.size());
                            switch (i) {
                                case 1: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        magyar.remove(0);
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = new Random().nextInt(allies.size());
                                        final Player soldier2 = allies.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.kill();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            magyar.remove(0);
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                            final Player soldier2 = allies.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.kill();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    allies.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                                case 3: {
                                    for (int b = 0; b < attackers[i]; ++b) {
                                        magyar.get(0).wound();
                                        magyar.remove(0);
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier2 = allies.get(rand);
                                        System.out.println(c);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.wound();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.wound();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 4: {
                                    if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            magyar.get(0).wound();
                                            magyar.remove(0);
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i]) {
                                        for (int d = 0; d < defenders[i]; ++d) {
                                            final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                            System.out.println("n: " + rand);
                                            final Player soldier2 = allies.get(rand);
                                            System.out.println("got a " + soldier2.getRank());
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.wound();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    allies.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                            else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                                soldier2.wound();
                                                allies.remove(rand);
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (allies.size() > magyar.size()) {
                    System.out.println("Allied forces won the battle " + magyar.size());
                }
                else {
                    System.out.println("Rebellious Vassals won the battle " + allies.size());
                }
            }
        }
        else {
            System.out.println("There is no invasion by Magyars in this fiefdom");
        }
    }
    
    public void muslim(final Collection args) {
        int attackersDice = 0;
        int defendersDice = 0;
        final Iterator argsIter = args.iterator();
        argsIter.next();
        final Knight attackingKnight = CurrentPlayers.currentPlayers.get(argsIter.next());
        final Fiefdoms fief = Map.map.get(attackingKnight.getLocation());
        if (fief.getMuslim().size() > 0 && attackingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
            final ArrayList<Intruders> muslim = fief.getMuslim();
            final ArrayList<Player> allies = attackingKnight.getAllies();
            attackingKnight.allyTo(attackingKnight.getName());
            if (allies.size() > 0) {
                for (final Player soldier : allies) {
                    if (soldier.getRank().equalsIgnoreCase("knight")) {
                        attackersDice += 3;
                    }
                    if (soldier.getRank().equalsIgnoreCase("count")) {
                        attackersDice += 4;
                    }
                    if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                        attackersDice += 6;
                    }
                    if (soldier.getRank().equalsIgnoreCase("vassal")) {
                        ++attackersDice;
                    }
                }
            }
            defendersDice = muslim.size();
            System.out.println("Attacker's dice: " + attackersDice);
            System.out.println("Defender's dice: " + defendersDice);
            while (allies.size() > 0 && muslim.size() > 0) {
                final int[] attackers = this.rollDice(attackersDice);
                final int[] defenders = this.rollDice(defendersDice);
                for (int i = 1; i < 6; ++i) {
                    if (allies.size() > 0 && muslim.size() > 0) {
                        System.out.println(i + " Rebellious: " + muslim.size());
                        System.out.println(i + " Allied Forces: " + allies.size());
                        System.out.println("Allies has thrown " + attackers[i] + " " + i + "'s");
                        System.out.println("Rebellious Vassals has thrown " + defenders[i] + " " + i + "'s");
                        attackers[i] = ((attackers[i] < muslim.size()) ? attackers[i] : muslim.size());
                        defenders[i] = ((defenders[i] < allies.size()) ? defenders[i] : allies.size());
                        switch (i) {
                            case 1: {
                                for (int b = 0; b < attackers[i]; ++b) {
                                    muslim.remove(0);
                                }
                                for (int c = 0; c < defenders[i]; ++c) {
                                    final int rand = new Random().nextInt(allies.size());
                                    final Player soldier2 = allies.get(rand);
                                    if (soldier2 instanceof Knight) {
                                        if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                            soldier2.kill();
                                            System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                        }
                                        else {
                                            allies.remove(rand);
                                            System.out.println("Killed a Knight");
                                        }
                                    }
                                    else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                        soldier2.kill();
                                        allies.remove(rand);
                                    }
                                }
                                break;
                            }
                            case 2: {
                                if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                    for (int c = 0; c < attackers[i]; ++c) {
                                        muslim.remove(0);
                                    }
                                    break;
                                }
                                if (attackers[i] < defenders[i]) {
                                    for (int c = 0; c < attackers[i]; ++c) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        final Player soldier2 = allies.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                    }
                                    break;
                                }
                                break;
                            }
                            case 3: {
                                for (int b = 0; b < attackers[i]; ++b) {
                                    muslim.get(0).wound();
                                    muslim.remove(0);
                                }
                                for (int c = 0; c < defenders[i]; ++c) {
                                    final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                    final Player soldier2 = allies.get(rand);
                                    System.out.println(c);
                                    if (soldier2 instanceof Knight) {
                                        if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                            soldier2.wound();
                                            System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                        }
                                        else {
                                            allies.remove(rand);
                                            System.out.println("Killed a Knight");
                                        }
                                    }
                                    else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                        soldier2.wound();
                                        allies.remove(rand);
                                    }
                                }
                                break;
                            }
                            case 4: {
                                if (attackers[i] > defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                    for (int c = 0; c < attackers[i]; ++c) {
                                        muslim.get(0).wound();
                                        muslim.remove(0);
                                    }
                                    break;
                                }
                                if (attackers[i] < defenders[i]) {
                                    for (int d = 0; d < defenders[i]; ++d) {
                                        final int rand = (allies.size() > 0) ? new Random().nextInt(allies.size()) : 0;
                                        System.out.println("n: " + rand);
                                        final Player soldier2 = allies.get(rand);
                                        System.out.println("got a " + soldier2.getRank());
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.wound();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                allies.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.wound();
                                            allies.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if (allies.size() > muslim.size()) {
                System.out.println("Allied forces won the battle " + muslim.size());
            }
            else {
                System.out.println("Rebellious Vassals won the battle " + allies.size());
            }
        }
    }
    
    public void knight(final Collection args) {
        int attackersDice = 0;
        int defendersDice = 0;
        final Iterator argsIter = args.iterator();
        argsIter.next();
        final Knight attackingKnight = CurrentPlayers.currentPlayers.get(argsIter.next());
        final Fiefdoms fief = Map.map.get(attackingKnight.getLocation());
        final Knight defendingKnight = CurrentPlayers.currentPlayers.get(fief.getOwner());
        if (attackingKnight != defendingKnight) {
            if (attackingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
                attackingKnight.allyTo(attackingKnight.getName());
                final ArrayList<Player> attackingArmy = attackingKnight.getAllies();
                System.out.println("Attacking Army allies: " + attackingArmy.size());
                ArrayList<Player> defendingArmy = new ArrayList<Player>();
                if (defendingKnight.getLocation().equalsIgnoreCase(fief.getName())) {
                    defendingKnight.allyTo(defendingKnight.getName());
                    defendingArmy = defendingKnight.getAllies();
                }
                else {
                    defendingArmy.addAll(fief.getLoyalVassals());
                }
                if (defendingArmy.size() > 0) {
                    for (final Player soldier : defendingArmy) {
                        if (soldier.getRank().equalsIgnoreCase("knight")) {
                            defendersDice += 3;
                        }
                        if (soldier.getRank().equalsIgnoreCase("count")) {
                            defendersDice += 4;
                        }
                        if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                            defendersDice += 6;
                        }
                        if (soldier.getRank().equalsIgnoreCase("vassal")) {
                            ++defendersDice;
                        }
                    }
                }
                if (attackingArmy.size() > 0) {
                    for (final Player soldier : attackingArmy) {
                        if (soldier.getRank().equalsIgnoreCase("knight")) {
                            attackersDice += 3;
                        }
                        if (soldier.getRank().equalsIgnoreCase("count")) {
                            attackersDice += 4;
                        }
                        if (soldier.getRank().equalsIgnoreCase("duke") || soldier.getRank().equalsIgnoreCase("king")) {
                            attackersDice += 6;
                        }
                        if (soldier.getRank().equalsIgnoreCase("vassal")) {
                            ++attackersDice;
                        }
                    }
                }
                System.out.println("Attacker's dice: " + attackersDice);
                System.out.println("Defender's dice: " + defendersDice);
                while (attackingArmy.size() > 0 && defendingArmy.size() > 0) {
                    final int[] attackers = this.rollDice(attackersDice);
                    final int[] defenders = this.rollDice(defendersDice);
                    for (int i = 1; i < 6; ++i) {
                        if (attackingArmy.size() > 0 && defendingArmy.size() > 0) {
                            System.out.println(i + " Rebellious: " + defendingArmy.size());
                            System.out.println(i + " Allied Forces: " + attackingArmy.size());
                            System.out.println("Allies has thrown " + attackers[i] + " " + i + "'s");
                            System.out.println("Rebellious Vassals has thrown " + defenders[i] + " " + i + "'s");
                            attackers[i] = ((attackers[i] < defendingArmy.size()) ? attackers[i] : defendingArmy.size());
                            defenders[i] = ((defenders[i] < attackingArmy.size()) ? defenders[i] : attackingArmy.size());
                            switch (i) {
                                case 1: {
                                    for (int c = 0; c < attackers[i]; ++c) {
                                        final int rand = new Random().nextInt(defendingArmy.size());
                                        final Player soldier2 = defendingArmy.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                defendingArmy.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.kill();
                                            defendingArmy.remove(rand);
                                        }
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (attackingArmy.size() > 0) ? new Random().nextInt(attackingArmy.size()) : 0;
                                        final Player soldier2 = attackingArmy.get(rand);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.kill();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                attackingArmy.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.kill();
                                            attackingArmy.remove(rand);
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if (attackers[i] > defenders[i]) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            final int rand = (defendingArmy.size() > 0) ? new Random().nextInt(defendingArmy.size()) : 0;
                                            final Player soldier2 = defendingArmy.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.kill();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    defendingArmy.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int c = 0; c < defenders[i]; ++c) {
                                            final int rand = (attackingArmy.size() > 0) ? new Random().nextInt(attackingArmy.size()) : 0;
                                            final Player soldier2 = attackingArmy.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.kill();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    attackingArmy.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                                case 3: {
                                    for (int c = 0; c < attackers[i]; ++c) {
                                        final int rand = (defendingArmy.size() > 0) ? new Random().nextInt(defendingArmy.size()) : 0;
                                        final Player soldier2 = defendingArmy.get(rand);
                                        System.out.println(c);
                                        if (soldier2 instanceof Knight) {
                                            if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                soldier2.wound();
                                                System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                            }
                                            else {
                                                defendingArmy.remove(rand);
                                                System.out.println("Killed a Knight");
                                            }
                                        }
                                        else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                            soldier2.wound();
                                            defendingArmy.remove(rand);
                                        }
                                    }
                                    for (int c = 0; c < defenders[i]; ++c) {
                                        final int rand = (attackingArmy.size() > 0) ? new Random().nextInt(attackingArmy.size()) : 0;
                                        if (rand > 0) {
                                            final Player soldier2 = attackingArmy.get(rand);
                                            System.out.println(c);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.wound();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    attackingArmy.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                            else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                                soldier2.wound();
                                                attackingArmy.remove(rand);
                                            }
                                        }
                                    }
                                    break;
                                }
                                case 4: {
                                    if (attackers[i] > defenders[i]) {
                                        for (int c = 0; c < attackers[i]; ++c) {
                                            final int rand = (defendingArmy.size() > 0) ? new Random().nextInt(defendingArmy.size()) : 0;
                                            final Player soldier2 = defendingArmy.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.wound();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    defendingArmy.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                        }
                                        break;
                                    }
                                    if (attackers[i] < defenders[i] || (attackers[i] == defenders[i] && fief.getFiefdomCastles() + fief.getPersonalCastles() > 0)) {
                                        for (int d = 0; d < defenders[i]; ++d) {
                                            final int rand = (attackingArmy.size() > 0) ? new Random().nextInt(attackingArmy.size()) : 0;
                                            final Player soldier2 = attackingArmy.get(rand);
                                            if (soldier2 instanceof Knight) {
                                                if (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills() + 1 < 3) {
                                                    soldier2.wound();
                                                    System.out.println("Got a knight. " + (((Knight)soldier2).getWounds() + ((Knight)soldier2).getKills()) + " slots have been filled");
                                                }
                                                else {
                                                    attackingArmy.remove(rand);
                                                    System.out.println("Killed a Knight");
                                                }
                                            }
                                            else if (soldier2 instanceof Vassals || soldier2 instanceof Retainers || soldier2 instanceof Mercenaries) {
                                                soldier2.wound();
                                                attackingArmy.remove(rand);
                                            }
                                        }
                                        break;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                if (attackingArmy.size() > defendingArmy.size()) {
                    System.out.println("Attacking army won the battle " + defendingArmy.size());
                }
                else {
                    System.out.println("Defending army won the battle " + attackingArmy.size());
                }
                if (attackingArmy.size() > 0) {}
            }
        }
        else {
            System.out.println("You can only go to war against a fiefdom that doesn't belong to you.");
        }
    }
}
