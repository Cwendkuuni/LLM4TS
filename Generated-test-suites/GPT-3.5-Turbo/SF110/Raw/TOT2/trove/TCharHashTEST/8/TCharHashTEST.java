import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    @Test
    public void testNoEntryValue() {
        TCharHash charHash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('a');
        assertTrue(charHash.contains('a'));
        assertFalse(charHash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('a');
        charHash.insertKey('b');
        charHash.insertKey('c');
        
        StringBuilder result = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                result.append(value);
                return true;
            }
        };
        
        assertTrue(charHash.forEach(procedure));
        assertEquals("abc", result.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash charHash = new TCharHash();
        charHash.insertKey('a');
        charHash.insertKey('b');
        charHash.removeAt(0);
        
        assertFalse(charHash.contains('a'));
        assertTrue(charHash.contains('b'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash charHash = new TCharHash();
        int index = charHash.insertKey('a');
        assertEquals(0, index);
        
        index = charHash.insertKey('b');
        assertEquals(1, index);
        
        index = charHash.insertKey('a');
        assertEquals(-1, index);
    }
}