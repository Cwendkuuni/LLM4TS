import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {
    @Test
    public void testDefaultConstructor() {
        TCharHash hash = new TCharHash();
        // Test getNoEntryValue() method
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        // Test contains() method
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('\0'));
        // Test forEach() method
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'a';
            }
        };
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        // Test getNoEntryValue() method
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        // Test contains() method
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('\0'));
        // Test forEach() method
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'a';
            }
        };
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.5f);
        // Test getNoEntryValue() method
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        // Test contains() method
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('\0'));
        // Test forEach() method
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'a';
            }
        };
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        char no_entry_value = 'x';
        TCharHash hash = new TCharHash(10, 0.5f, no_entry_value);
        // Test getNoEntryValue() method
        assertEquals(no_entry_value, hash.getNoEntryValue());
        // Test contains() method
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('\0'));
        // Test forEach() method
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'a';
            }
        };
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testSetUpMethod() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }
    
    @Test
    public void testContainsMethod() {
        TCharHash hash = new TCharHash();
        assertFalse(hash.contains('a'));
        assertFalse(hash.contains('\0'));
    }
    
    @Test
    public void testForEachMethod() {
        TCharHash hash = new TCharHash();
        TCharProcedure procedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return value != 'a';
            }
        };
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAtMethod() {
        TCharHash hash = new TCharHash();
        hash.removeAt(0);
        assertFalse(hash.contains('\0'));
    }
    
    @Test
    public void testIndexMethod() {
        TCharHash hash = new TCharHash();
        int index = hash.index('a');
        assertEquals(-1, index);
        index = hash.index('\0');
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashedMethod() {
        TCharHash hash = new TCharHash();
        int index = hash.indexRehashed('a', 0, 0, (byte) 0);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyMethod() {
        TCharHash hash = new TCharHash();
        int index = hash.insertKey('a');
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehashMethod() {
        TCharHash hash = new TCharHash();
        int index = hash.insertKeyRehash('a', 0, 0, (byte) 0);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyAtMethod() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'a');
        assertTrue(hash.contains('a'));
    }
}