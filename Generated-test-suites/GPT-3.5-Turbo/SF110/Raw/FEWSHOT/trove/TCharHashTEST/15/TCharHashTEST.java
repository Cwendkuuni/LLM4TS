package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    @Test
    public void testContains() {
        assertFalse(tCharHash.contains('a'));
        
        tCharHash.insertKey('a');
        assertTrue(tCharHash.contains('a'));
        
        assertFalse(tCharHash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;
            
            @Override
            public boolean execute(char value) {
                count++;
                return true;
            }
        };
        
        assertTrue(tCharHash.forEach(procedure));
        assertEquals(0, procedure.count);
        
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertTrue(tCharHash.forEach(procedure));
        assertEquals(3, procedure.count);
        
        procedure = new TCharProcedure() {
            
            @Override
            public boolean execute(char value) {
                return false;
            }
        };
        
        assertFalse(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertTrue(tCharHash.contains('a'));
        tCharHash.removeAt(0);
        assertFalse(tCharHash.contains('a'));
        
        assertTrue(tCharHash.contains('b'));
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('b'));
        
        assertTrue(tCharHash.contains('c'));
        tCharHash.removeAt(2);
        assertFalse(tCharHash.contains('c'));
    }
    
    @Test
    public void testIndex() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        assertEquals(0, tCharHash.index('a'));
        assertEquals(1, tCharHash.index('b'));
        assertEquals(2, tCharHash.index('c'));
        
        assertEquals(-1, tCharHash.index('d'));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tCharHash.insertKey('a'));
        assertTrue(tCharHash.contains('a'));
        
        assertEquals(1, tCharHash.insertKey('b'));
        assertTrue(tCharHash.contains('b'));
        
        assertEquals(2, tCharHash.insertKey('c'));
        assertTrue(tCharHash.contains('c'));
        
        assertEquals(-1, tCharHash.insertKey('a'));
        assertEquals(-2, tCharHash.insertKey('b'));
        assertEquals(-3, tCharHash.insertKey('c'));
    }
}