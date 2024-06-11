
package lotus.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.stack.Stack;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        assertNotNull(Game.getPlayingPlayer());
    }

    @Test
    public void testGetNonPlayingPlayer() {
        assertNotNull(Game.getNonPlayingPlayer());
    }

    @Test
    public void testGetOtherPlayer() {
        Player p = new Player(new UserInterface(), "name", new CardCollection());
        assertNotNull(Game.getOtherPlayer(p));
    }

    @Test
    public void testInit() {
        UserInterface IP1 = new UserInterface();
        UserInterface IP2 = new UserInterface();
        String n1 = "player1";
        String n2 = "player2";
        CardCollection deck1 = new CardCollection();
        CardCollection deck2 = new CardCollection();

        Game.init(IP1, IP2, n1, n2, deck1, deck2);

        assertNotNull(Game.player1);
        assertNotNull(Game.player2);
        assertNotNull(Game.currentPhase);
        assertNotNull(Game.stack);
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        Game.stack.push(new Card());
        Game.givePriorityToCurrentPlayer();

        assertNull(Game.stack.getLastResolvedCard());
    }

}
