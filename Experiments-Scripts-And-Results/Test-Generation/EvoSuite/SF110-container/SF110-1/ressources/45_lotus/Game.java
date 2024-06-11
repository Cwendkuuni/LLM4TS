package lotus.core;

import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class Game
{
    public static Combat combat;
    public static Phase currentPhase;
    public static int playingPlayer;
    public static Player player1;
    public static Player player2;
    public static Stack stack;
    
    public static Player getPlayingPlayer() {
        if (Game.playingPlayer == 1) {
            return Game.player1;
        }
        return Game.player2;
    }
    
    public static Player getNonPlayingPlayer() {
        return getOtherPlayer(getPlayingPlayer());
    }
    
    public static Player getOtherPlayer(final Player p) {
        if (p == Game.player1) {
            return Game.player2;
        }
        return Game.player1;
    }
    
    public static void init(final UserInterface IP1, final UserInterface IP2, final String n1, final String n2, final CardCollection deck1, final CardCollection deck2) {
        Game.player1 = new Player(IP1, n1, deck1);
        Game.player2 = new Player(IP2, n2, deck2);
        IP1.init(Game.player1);
        IP2.init(Game.player2);
        Game.playingPlayer = 1;
        Game.currentPhase = new UntapPhase();
        for (final Card c : deck1) {
            c.owner = Game.player1;
            c.zone = Game.player1.library;
        }
        for (final Card c : deck2) {
            c.owner = Game.player2;
            c.zone = Game.player2.library;
        }
    }
    
    public static void givePriorityToCurrentPlayer() {
        boolean APdone = false;
        boolean NAPdone = false;
        do {
            APdone = !getPlayingPlayer().letPlayerSpeak();
            if (NAPdone && APdone) {
                break;
            }
            NAPdone = !getNonPlayingPlayer().letPlayerSpeak();
        } while (!NAPdone || !APdone);
        if (!Game.stack.empty()) {
            Game.stack.resolveLast();
            givePriorityToCurrentPlayer();
        }
    }
    
    static {
        Game.stack = new Stack();
    }
}
