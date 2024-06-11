import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash(5, 0.75f, (short) -1);
    }
    
    @Test
    public void testGetNoEntryValue() {
        short result = tShortHash.getNoEntryValue();
        assertEquals(-1, result);
    }
    
    @Test
    public void testContains() {
        boolean result = tShortHash.contains((short) 2);
        assertFalse(result);
    }
    
    @Test
    public void testForEach() {
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short val) {
                return val > 0;
            }
        };
        
        boolean result = tShortHash.forEach(procedure);
        assertTrue(result);
    }
    
    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        tShortHash.removeAt(1);
        
        boolean result = tShortHash.contains((short) 2);
        assertFalse(result);
    }
    
    @Test
    public void testIndex() {
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        int result = tShortHash.index((short) 2);
        assertEquals(1, result);
    }
    
    @Test
    public void testIndexRehashed() {
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        int result = tShortHash.indexRehashed((short) 2, 0, 1, (byte) 1);
        assertEquals(1, result);
    }
    
    @Test
    public void testInsertKey() {
        int result = tShortHash.insertKey((short) 1);
        assertEquals(0, result);
    }
    
    @Test
    public void testInsertKeyRehash() {
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        int result = tShortHash.insertKeyRehash((short) 3, 0, 1, (byte) 1);
        assertEquals(2, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 1);
        tShortHash.insertKeyAt(1, (short) 2);
        
        boolean result = tShortHash.contains((short) 2);
        assertTrue(result);
    }
}