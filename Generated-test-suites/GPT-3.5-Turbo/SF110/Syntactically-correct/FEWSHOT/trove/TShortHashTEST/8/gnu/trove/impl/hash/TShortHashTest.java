import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 100);
        assertTrue(tShortHash.contains((short) 100));
        assertFalse(tShortHash.contains((short) 200));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 100);
        tShortHash.insertKey((short) 200);
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;
            public boolean execute(short value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
        assertEquals(300, procedure.sum);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 100);
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 100));
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 100);
        assertEquals(0, tShortHash.index((short) 100));
        assertEquals(-1, tShortHash.index((short) 200));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(0, tShortHash.insertKey((short) 100));
        assertEquals(-1, tShortHash.insertKey((short) 100));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 100);
        assertEquals(0, tShortHash.insertKeyRehash((short) 100, 0, 0, (byte) 1));
        assertEquals(0, tShortHash.insertKeyRehash((short) 200, 0, 0, (byte) 1));
    }
    
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        assertEquals((short) -32768, tShortHash.getNoEntryValue());
    }
}