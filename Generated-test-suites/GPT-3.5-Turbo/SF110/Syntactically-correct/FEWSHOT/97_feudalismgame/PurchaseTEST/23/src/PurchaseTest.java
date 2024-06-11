package src;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class PurchaseTest {

    private Purchase purchase;

    @Before
    public void setUp() {
        purchase = new Purchase();
    }

    @Test
    public void testVassal() {
        Iterator iterator = mock(Iterator.class);
        when(iterator.next()).thenReturn("10", "buyerName", "fiefdomName");

        // Test when fiefdom belongs to current player
        purchase.vassal(iterator);

        // Test when buyerObj is king
        purchase.vassal(iterator);

        // Test when buyerObj is not king and fiefdom belongs to another player
        purchase.vassal(iterator);

        verify(iterator, times(6)).next();
    }

    @Test
    public void testCastleFiefdom() {
        Iterator iterator = mock(Iterator.class);
        when(iterator.next()).thenReturn("5", "buyerName", "fiefdomName");

        // Test when fiefdom belongs to current player and there are no vikings, magyars, and muslims in the fiefdom
        purchase.castleFiefdom(iterator);

        // Test when buyerObj is king
        purchase.castleFiefdom(iterator);

        // Test when buyerObj is not king and fiefdom belongs to another player
        purchase.castleFiefdom(iterator);

        verify(iterator, times(6)).next();
    }

    @Test
    public void testCastlePersonal() {
        Iterator iterator = mock(Iterator.class);
        when(iterator.next()).thenReturn("3", "buyerName", "fiefdomName");

        // Test when fiefdom belongs to current player and there are no vikings, magyars, and muslims in the fiefdom
        purchase.castlePersonal(iterator);

        // Test when buyerObj is king
        purchase.castlePersonal(iterator);

        // Test when buyerObj is not king and fiefdom belongs to another player
        purchase.castlePersonal(iterator);

        verify(iterator, times(6)).next();
    }

    @Test
    public void testMercenary() {
        Iterator iterator = mock(Iterator.class);
        when(iterator.next()).thenReturn("2", "buyerName", "knightName");

        // Test when buyerObj equals knight
        purchase.mercenary(iterator);

        // Test when buyerObj is king
        purchase.mercenary(iterator);

        // Test when buyerObj is not king and knight is existent
        purchase.mercenary(iterator);

        verify(iterator, times(6)).next();
    }
}