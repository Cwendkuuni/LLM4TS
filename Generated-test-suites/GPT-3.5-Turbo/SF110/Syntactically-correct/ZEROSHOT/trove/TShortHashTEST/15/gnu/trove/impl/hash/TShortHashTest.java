import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            // Abstract methods
            protected int computeHashCode(short val) { return 0; }
            protected boolean equals(short val1, short val2) { return false; }
        };
    }
    
    @Test
    public void testGetNoEntryValue() {
        short expectedNoEntryValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short actualNoEntryValue = tShortHash.getNoEntryValue();
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }
    
    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short) 0)); // Empty array
        tShortHash.insertKey((short) 0);
        assertTrue(tShortHash.contains((short) 0));
    }
    
    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short val) { return true; }
        };
        assertFalse(tShortHash.forEach(procedure)); // Empty array
        tShortHash.insertKey((short) 0);
        assertTrue(tShortHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 0);
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 0));
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, tShortHash.index((short) 0)); // Empty array
        tShortHash.insertKey((short) 0);
        assertEquals(0, tShortHash.index((short) 0));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.insertKey((short) 0));
        assertEquals(-1, tShortHash.insertKey((short) 0)); // Duplicate - should return negative index
    }
}