import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    // Test case for TShortHash()
    @Test
    public void testTShortHash() {
        TShortHash tShortHash = new TShortHash();
        assertNotNull(tShortHash);
    }

    // Test case for TShortHash(int initialCapacity)
    @Test
    public void testTShortHashWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        assertNotNull(tShortHash);
        assertEquals(initialCapacity, tShortHash.size());
    }

    // Test case for TShortHash(int initialCapacity, float loadFactor)
    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        assertNotNull(tShortHash);
        assertEquals(initialCapacity, tShortHash.size());
        assertEquals(loadFactor, tShortHash.loadFactor(), 0.001);
    }

    // Test case for TShortHash(int initialCapacity, float loadFactor, short no_entry_value)
    @Test
    public void testTShortHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = -1;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        assertNotNull(tShortHash);
        assertEquals(initialCapacity, tShortHash.size());
        assertEquals(loadFactor, tShortHash.loadFactor(), 0.001);
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }

    // Test case for contains(short val)
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        short value = 10;
        assertFalse(tShortHash.contains(value));
        tShortHash.insertKey(value);
        assertTrue(tShortHash.contains(value));
    }

    // Test case for forEach(TShortProcedure procedure)
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        tShortHash.insertKey((short) 30);
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;

            @Override
            public boolean execute(short value) {
                sum += value;
                return true;
            }

            public int getSum() {
                return sum;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
        assertEquals(60, procedure.getSum());
    }

    // Test case for removeAt(int index)
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        short removedVal = (short) 10;
        int removedIndex = tShortHash.index(removedVal);
        tShortHash.removeAt(removedIndex);
        assertFalse(tShortHash.contains(removedVal));
    }

    // Test case for index(short val)
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        short value = 20;
        int expectedIndex = 1;
        assertEquals(expectedIndex, tShortHash.index(value));
    }

    // Test case for insertKey(short val)
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        short value = 10;
        int expectedIndex = 0;
        assertEquals(expectedIndex, tShortHash.insertKey(value));
        assertTrue(tShortHash.contains(value));
        assertFalse(tShortHash.consumeFreeSlot);
    }

    // Test case for insertKeyRehash(short val, int index, int hash, byte state)
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.setUp(10);
        short value = 10;
        int index = 0;
        int hash = 2147483647;  // Integer.MAX_VALUE
        byte state = 2;
        int expectedIndex = 2;
        assertEquals(expectedIndex, tShortHash.insertKeyRehash(value, index, hash, state));
        assertTrue(tShortHash.contains(value));
        assertFalse(tShortHash.consumeFreeSlot);
    }

    // Test case for insertKeyAt(int index, short val)
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.setUp(10);
        int index = 0;
        short value = 10;
        tShortHash.insertKeyAt(index, value);
        assertTrue(tShortHash.contains(value));
        assertEquals(value, tShortHash._set[index]);
        assertEquals(1, tShortHash._states[index]);
    }

}