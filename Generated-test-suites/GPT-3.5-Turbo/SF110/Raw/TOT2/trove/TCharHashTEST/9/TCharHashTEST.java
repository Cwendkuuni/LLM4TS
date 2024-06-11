import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TCharHash charHash = new TCharHash();
        int capacity = 10;
        assertEquals(capacity, charHash.setUp(capacity));
    }
    
    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
        assertFalse(charHash.contains('B'));
    }
    
    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        
        TCharProcedure procedure = new TCharProcedure() {
            boolean result = true;
            
            @Override
            public boolean execute(char value) {
                if (value == 'B') {
                    result = false;
                    return false;
                }
                return true;
            }
        };
        
        assertFalse(charHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.removeAt(0);
        assertFalse(charHash.contains('A'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        assertTrue(charHash.contains('A'));
        assertEquals(-1, charHash.insertKey('A'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('A');
        charHash.insertKey('B');
        charHash.insertKey('C');
        
        int indexD = charHash.insertKeyRehash('D', 3, 4, (byte) 1);
        
        assertTrue(charHash.contains('D'));
        assertEquals(3, indexD);
    }
    
}