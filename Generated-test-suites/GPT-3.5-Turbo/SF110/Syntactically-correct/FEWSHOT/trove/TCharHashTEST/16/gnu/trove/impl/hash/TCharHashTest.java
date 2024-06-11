package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    @Test
    public void testConstructorWithNoArgs() {
        TCharHash charHash = new TCharHash();
        assertEquals('\0', charHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash charHash = new TCharHash(10);
        assertEquals('\0', charHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash charHash = new TCharHash(10, 0.75f);
        assertEquals('\0', charHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        TCharHash charHash = new TCharHash(10, 0.75f, 'A');
        assertEquals('A', charHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash();
        assertEquals(10, charHash.setUp(10));
    }

    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        assertFalse(charHash.contains('A'));
    }

    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        assertTrue(charHash.forEach(val -> true));
    }

    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.removeAt(0);
        assertFalse(charHash.contains('\0'));
    }

    @Test
    public void testIndex() {
        TCharHash charHash = new TCharHash();
        assertEquals(-1, charHash.index('A'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash charHash = new TCharHash();
        assertEquals(-1, charHash.indexRehashed('A', 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        int index = charHash.insertKey('A');
        assertTrue(index >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWithNoAvailableSlots() {
        TCharHash charHash = new TCharHash();
        charHash.insertKeyRehash('A', 0, 0, (byte) 2);
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKeyAt(0, 'A');
        assertTrue(charHash.contains('A'));
    }
}