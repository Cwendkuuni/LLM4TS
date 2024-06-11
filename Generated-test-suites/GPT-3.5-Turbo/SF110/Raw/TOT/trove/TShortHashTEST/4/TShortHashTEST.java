import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        TShortHash tShortHash = new TShortHash(10);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TShortHash tShortHash = new TShortHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        TShortHash tShortHash = new TShortHash(10, 0.75f, (short) -1);
        assertEquals(-1, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int capacity = 100;
        assertEquals(capacity, tShortHash.setUp(capacity));
        assertEquals(capacity, tShortHash._set.length);
    }
    
    @Test
    public void testContains() {
        tShortHash.insertKey((short) 1);
        
        assertTrue(tShortHash.contains((short) 1));
        assertFalse(tShortHash.contains((short) 2));
    }
    
    @Test
    public void testForEach() {
        final boolean[] isExecuted = {false};
        
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                isExecuted[0] = true;
                return true;
            }
        };
        
        tShortHash.insertKey((short) 1);
        assertTrue(tShortHash.forEach(procedure));
        
        assertTrue(isExecuted[0]);
    }
    
    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 1);
        
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
        tShortHash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
    }
    
    @Test
    public void testIndex() {
        tShortHash.insertKey((short) 1);
        assertEquals(0, tShortHash.index((short) 1));
        assertEquals(-1, tShortHash.index((short) 2));
    }
    
    @Test
    public void testIndexRehashed() {
        tShortHash.insertKey((short) 1);
        assertEquals(0, tShortHash.indexRehashed((short) 1, 0, 1, (byte) 1));
        assertEquals(-1, tShortHash.indexRehashed((short) 2, 0, 1, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        int index = tShortHash.insertKey((short) 1);
        
        assertEquals(0, index);
        assertEquals((short) 1, tShortHash._set[index]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        int index = 0;
        int hash = 1;
        byte state = 1;
        
        tShortHash.insertKey((short) -1);
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((-1) * tShortHash._states.length);
        tShortHash.insertKey((-1) * tShortHash._states.length + 1);
        
        tShortHash.insertKeyRehash((short) 5, index, hash, state); // Throws IllegalStateException
    }
    
    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 1);
        
        assertEquals((short) 1, tShortHash._set[0]);
        assertEquals((byte) 1, tShortHash._states[0]);
    }
}