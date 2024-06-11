
import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short)10);
        assertTrue(hash.contains((short)10));
        assertFalse(hash.contains((short)20));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short)10);
        hash.insertKey((short)20);
        hash.insertKey((short)30);
        assertTrue(hash.forEach(new TShortProcedure() {
            public boolean execute(short val) {
                return val > 0;
            }
        }));
        assertFalse(hash.forEach(new TShortProcedure() {
            public boolean execute(short val) {
                return val > 50;
            }
        }));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short)10);
        assertEquals((short)10, hash._set[0]);
        hash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short)10);
        assertEquals(0, hash.index((short)10));
        assertEquals(-1, hash.index((short)20));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        assertEquals(0, hash.insertKey((short)10));
        assertEquals(0, hash.insertKey((short)10));
    }
    
    // Add additional test cases for edge cases and exceptions here
    
}