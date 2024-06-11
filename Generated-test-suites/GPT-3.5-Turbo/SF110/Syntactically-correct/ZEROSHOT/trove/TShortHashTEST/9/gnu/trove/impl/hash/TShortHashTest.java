import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            public int capacity() {
                return 10;
            }

            @Override
            public double fillFactor() {
                return 0.5;
            }

            @Override
            protected void rehash(int newCapacity) {
                // Do nothing
            }
        };
    }

    @Test
    public void testConstructorWithDefaultValues() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TShortHash tShortHash = new TShortHash(20);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(20, tShortHash.capacity());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash tShortHash = new TShortHash(20, 0.7f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertEquals(20, tShortHash.capacity());
        assertEquals(0.7f, tShortHash.fillFactor(), 0);
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
        TShortHash tShortHash = new TShortHash(20, 0.7f, (short) 0);
        assertEquals(0, tShortHash.getNoEntryValue());
        assertEquals(20, tShortHash.capacity());
        assertEquals(0.7f, tShortHash.fillFactor(), 0);
    }

    @Test
    public void testSetUp() {
        assertEquals(10, tShortHash.setUp(10));
        assertEquals(10, tShortHash._set.length);
    }

    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 5));
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5));
    }

    @Test
    public void testForEach() {
        TShortProcedure tShortProcedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value > 0;
            }
        };
        
        assertFalse(tShortHash.forEach(tShortProcedure));
        
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)-2);
        
        assertFalse(tShortHash.forEach(tShortProcedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        
        int index = tShortHash.index((short)1);
        tShortHash.removeAt(index);
        
        assertFalse(tShortHash.contains((short)1));
        assertTrue(tShortHash.contains((short)2));
    }

    @Test
    public void testIndex() {
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        
        assertEquals(0, tShortHash.index((short)1));
        assertEquals(1, tShortHash.index((short)2));
        assertEquals(-1, tShortHash.index((short)3));
    }

    @Test
    public void testInsertKey() {
        int index = tShortHash.insertKey((short)10);
        assertTrue(tShortHash.contains((short)10));
        assertEquals(0, index);
        
        index = tShortHash.insertKey((short)20);
        assertTrue(tShortHash.contains((short)20));
        assertEquals(1, index);

        index = tShortHash.insertKey((short)30);
        assertTrue(tShortHash.contains((short)30));
        assertEquals(2, index);
    }

    @Test
    public void testInsertKeyRehash() {
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        
        int index = tShortHash.index((short)1);
        tShortHash.removeAt(index);
        
        index = tShortHash.insertKeyRehash((short)1, index, 0, (byte) 0);
        assertTrue(tShortHash.contains((short)1));
        assertEquals(0, index);
        
        index = tShortHash.insertKeyRehash((short)3, index, 0, (byte) 0);
        assertTrue(tShortHash.contains((short)3));
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFullKeySet() {
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        tShortHash.insertKey((short)3);
        
        tShortHash.insertKeyRehash((short)4, 0, 0, (byte) 0);
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short)10);
        assertTrue(tShortHash.contains((short)10));
        assertEquals(10, tShortHash._set[0]);
        assertEquals(1, tShortHash._states[0]);
    }

}