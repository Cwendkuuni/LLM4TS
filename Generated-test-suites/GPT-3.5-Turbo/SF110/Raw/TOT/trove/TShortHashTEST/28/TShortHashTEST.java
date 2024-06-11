import static org.junit.Assert.*;
import org.junit.*;

public class TShortHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash(10);
        assertEquals(10, tShortHash.setUp(10));
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        assertTrue(tShortHash.contains((short) 1));
        assertFalse(tShortHash.contains((short) 2));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 3);
        
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            
            public boolean execute(short value) {
                count++;
                return (value > 0);
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
        assertEquals(3, procedure.count);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        assertEquals(1, tShortHash.size());
        
        tShortHash.removeAt(0);
        assertEquals(0, tShortHash.size());
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        assertEquals(0, tShortHash.index((short) 1));
        assertEquals(-1, tShortHash.index((short) 2));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash(10);
        assertEquals(0, tShortHash.insertKey((short) 1));
        assertEquals(-1, tShortHash.insertKey((short) 1));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        
        assertEquals(2, tShortHash.insertKeyRehash((short) 2, 0, 123, (byte) 1));
        assertEquals(9, tShortHash.insertKeyRehash((short) 2, 0, 123, (byte) 0));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash(1);
        tShortHash.insertKeyAt(0, (short) 1);
        
        tShortHash.insertKeyAt(0, (short) 2);
    }
}