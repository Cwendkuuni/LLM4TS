import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TCharHash hash = new TCharHash(initialCapacity);
        assertEquals(initialCapacity, hash.size());
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TCharHash hash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash.size());
        assertEquals(loadFactor, hash.getLoadFactor(), 0.001);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char no_entry_value = ' ';
        TCharHash hash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        assertEquals(initialCapacity, hash.size());
        assertEquals(loadFactor, hash.getLoadFactor(), 0.001);
        assertEquals(no_entry_value, hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, hash._set.length);
        for (int i = 0; i < initialCapacity; i++) {
            assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[i]);
        }
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        assertTrue(hash.contains('a'));
        assertFalse(hash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        StringBuilder builder = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                builder.append(value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals("abc", builder.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        assertEquals(2, hash.size());
        hash.removeAt(0);
        assertFalse(hash.contains('a'));
        assertEquals(1, hash.size());
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[0]);
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        assertEquals(1, hash.index('b'));
        assertEquals(-1, hash.index('d'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        hash.insertKey('c');
        assertEquals(1, hash.indexRehashed('b', 1, 1, (byte)1));
        assertEquals(-1, hash.indexRehashed('d', 1, 1, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        assertEquals(0, hash.insertKey('a'));
        assertEquals(2, hash.insertKey('b'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash.insertKey('a');
        hash.insertKey('b');
        assertEquals(2, hash.insertKeyRehash('c', 2, 2, (byte)1));
        assertEquals(-3, hash.insertKeyRehash('a', 2, 2, (byte)1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'a');
        assertEquals('a', hash._set[0]);
        assertEquals((byte)1, hash._states[0]);
    }
}