import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testTShortHash() {
        TShortHash t = new TShortHash();
        assertNotNull(t);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, t.getNoEntryValue());
    }
    
    @Test
    public void testTShortHashWithInitialCapacity() {
        TShortHash t = new TShortHash(10);
        assertNotNull(t);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, t.getNoEntryValue());
    }
    
    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactor() {
        TShortHash t = new TShortHash(10, 0.75f);
        assertNotNull(t);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, t.getNoEntryValue());
    }
    
    @Test
    public void testTShortHashWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        short noEntryValue = 10;
        TShortHash t = new TShortHash(10, 0.75f, noEntryValue);
        assertNotNull(t);
        assertEquals(noEntryValue, t.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TShortHash t = new TShortHash();
        int capacity = t.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(t._set);
        assertEquals(capacity, t._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash t = new TShortHash();
        t._set = new short[]{1, 2, 3};
        assertTrue(t.contains(2));
        assertFalse(t.contains(5));
    }
    
    @Test
    public void testForEach() {
        TShortHash t = new TShortHash();
        t._states = new byte[]{1, 1, 0};
        t._set = new short[]{1, 2, 3};
        TShortProcedure procedure = new TShortProcedure() {
            boolean executed = false;

            @Override
            public boolean execute(short value) {
                executed = true;
                return true;
            }
        };
        assertFalse(t.forEach(procedure));
        assertTrue(procedure.executed);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash t = new TShortHash();
        t._states = new byte[]{1, 1, 0};
        t._set = new short[]{1, 2, 3};
        t.removeAt(1);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, t._set[1]);
        assertEquals(0, t._states[1]);
    }
    
    @Test
    public void testIndex() {
        TShortHash t = new TShortHash();
        t._states = new byte[]{1, 1, 0};
        t._set = new short[]{1, 2, 3};
        assertEquals(1, t.index(2));
        assertEquals(-1, t.index(5));
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash t = new TShortHash();
        t._set = new short[]{1, 2, 3};
        t._states = new byte[]{1, 2, 1};
        int index = t.indexRehashed(2, 0, 0, (byte) 2);
        assertEquals(0, index);
        index = t.indexRehashed(5, 0, 0, (byte) 1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash t = new TShortHash();
        t._set = new short[]{1, 2, 3};
        t._states = new byte[]{1, 2, 0};
        int index = t.insertKey(2);
        assertEquals(-1, index);
        index = t.insertKey(5);
        assertEquals(2, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash t = new TShortHash();
        t._set = new short[]{1, 2, 3};
        t._states = new byte[]{1, 2, 0};
        int index = t.insertKeyRehash(2, 0, 0, (byte) 2);
        assertEquals(0, index);
        index = t.insertKeyRehash(5, 0, 0, (byte) 1);
        assertEquals(0, index);
        assertEquals((byte) 1, t._states[0]);
        assertEquals(5, t._set[0]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashThrowsIllegalStateException() {
        TShortHash t = new TShortHash();
        t._set = new short[]{1, 2, 3};
        t._states = new byte[]{1, 2, 1};
        t.insertKeyRehash(2, 0, 0, (byte) 2);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash t = new TShortHash();
        t._set = new short[]{1, 2, 3};
        t._states = new byte[]{1, 2, 0};
        t.insertKeyAt(1, 5);
        assertEquals(5, t._set[1]);
        assertEquals((byte) 1, t._states[1]);
    }
}