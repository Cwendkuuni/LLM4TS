package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testInitialCapacityConstructor() {
        TCharHash hash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        TCharHash hash = new TCharHash(10, 0.5f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        char noEntryValue = 'A';
        TCharHash hash = new TCharHash(10, 0.5f, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash(10);
        
        hash.insertKey('A');
        hash.insertKey('B');
        
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertFalse(hash.contains('C'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(10);
        
        hash.insertKey('A');
        hash.insertKey('B');
        
        TCharProcedure procedure = new TCharProcedure() {
            String result = "";
            
            @Override
            public boolean execute(char value) {
                result += value;
                return true;
            }
            
            public String getResult() {
                return result;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals("AB", procedure.getResult());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash(10);
        
        hash.insertKey('A');
        hash.insertKey('B');
        
        hash.removeAt(0);
        
        assertFalse(hash.contains('A'));
        assertTrue(hash.contains('B'));
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(10);
        
        hash.insertKey('A');
        hash.insertKey('B');
        
        assertEquals(0, hash.index('A'));
        assertEquals(1, hash.index('B'));
        assertEquals(-1, hash.index('C'));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(10);
        
        assertEquals(0, hash.insertKey('A'));
        assertEquals(1, hash.insertKey('B'));
        assertEquals(-2, hash.insertKey('A'));
    }
}