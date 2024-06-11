package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    
    private TCharHash testTCharHash;
    
    @Before
    public void setUp() {
        testTCharHash = new TCharHash();
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, testTCharHash.getNoEntryValue());
    }
    
    @Test
    public void testContains_true() {
        testTCharHash.insertKey('a');
        assertTrue(testTCharHash.contains('a'));
    }
    
    @Test
    public void testContains_false() {
        testTCharHash.insertKey('a');
        assertFalse(testTCharHash.contains('b'));
    }
    
    @Test
    public void testForEach_true() {
        testTCharHash.insertKey('a');
        testTCharHash.insertKey('b');
        testTCharHash.insertKey('c');

        TCharProcedure procedure = new TCharProcedure() {
            boolean executeResult = true;
            
            @Override
            public boolean execute(char val) {
                if (Character.isLowerCase(val)) {
                    return executeResult;
                } else {
                    return !executeResult;
                }
            }
        };

        assertTrue(testTCharHash.forEach(procedure));
    }
    
    @Test
    public void testForEach_false() {
        testTCharHash.insertKey('a');
        testTCharHash.insertKey('b');
        testTCharHash.insertKey('c');
        
        TCharProcedure procedure = new TCharProcedure() {
            boolean executeResult = false;
            
            @Override
            public boolean execute(char val) {
                if (Character.isUpperCase(val)) {
                    return executeResult;
                } else {
                    return !executeResult;
                }
            }
        };
        
        assertFalse(testTCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        testTCharHash.insertKey('a');
        testTCharHash.removeAt(0);
        
        assertFalse(testTCharHash.contains('a'));
    }
    
    @Test
    public void testIndex() {
        testTCharHash.insertKey('a');
        testTCharHash.insertKey('b');
        testTCharHash.insertKey('c');
        
        assertEquals(0, testTCharHash.index('a'));
        assertEquals(1, testTCharHash.index('b'));
        assertEquals(2, testTCharHash.index('c'));
    }
    
    @Test
    public void testIndex_noMatch() {
        testTCharHash.insertKey('a');
        testTCharHash.insertKey('b');
        testTCharHash.insertKey('c');
        
        assertEquals(-1, testTCharHash.index('d'));
    }
    
    @Test
    public void testInsertKey() {
        int index = testTCharHash.insertKey('a');
        
        assertEquals(0, index);
        assertTrue(testTCharHash.contains('a'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        testTCharHash.insertKey('a');
        testTCharHash.insertKey('b');
        testTCharHash.insertKey('c');
        
        int index = testTCharHash.insertKeyRehash('d', 0, 1, (byte) 1);
        
        assertEquals(3, index);
        assertTrue(testTCharHash.contains('d'));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash_noFreeSlots() {
        testTCharHash.insertKey('a');
        testTCharHash.insertKey('b');
        testTCharHash.insertKey('c');
        testTCharHash.insertKey('d');
        
        testTCharHash.insertKeyRehash('e', 0, 1, (byte) 1);
    }
    
    @Test
    public void testInsertKeyAt() {
        testTCharHash.insertKeyAt(0, 'a');
        
        assertTrue(testTCharHash.contains('a'));
    }
}