import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testDefaultConstructor() {
        TCharHash hash = new TCharHash();
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(0, hash.size());
        assertFalse(hash.contains((char)0));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(0, hash.size());
        assertFalse(hash.contains((char)0));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        assertEquals(0, hash.size());
        assertFalse(hash.contains((char)0));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
        char noEntryValue = 'X';
        TCharHash hash = new TCharHash(10, 0.75f, noEntryValue);
        assertEquals(noEntryValue, hash.getNoEntryValue());
        assertEquals(0, hash.size());
        assertFalse(hash.contains((char)0));
    }

    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        assertTrue(hash.contains((char)1));
        assertFalse(hash.contains((char)2));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        hash.insertKey((char)2);
        TCharProcedure procedure = new TCharProcedure() {
            int count = 0;
            public boolean execute(char value) {
                count++;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(2, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        assertEquals(1, hash.size());
        hash.removeAt(0);
        assertEquals(0, hash.size());
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        hash.insertKey((char)2);
        assertEquals(0, hash.index((char)1));
        assertEquals(1, hash.index((char)2));
        assertEquals(-1, hash.index((char)3));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        hash.insertKey((char)2);
        hash.insertKey((char)4);
        assertEquals(1, hash.indexRehashed((char)2, 2, 2, (byte)1));
        assertEquals(-1, hash.indexRehashed((char)3, 3, 3, (byte)1));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        assertEquals(1, hash.size());
        hash.insertKey((char)1);
        assertEquals(1, hash.size());
        hash.insertKey((char)2);
        assertEquals(2, hash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash.insertKey((char)1);
        hash.insertKey((char)2);
        hash.insertKey((char)4);
        assertEquals(1, hash.insertKeyRehash((char)2, 2, 2, (byte)1));
        assertEquals(3, hash.size());
        assertEquals(-1, hash.insertKeyRehash((char)3, 3, 3, (byte)1));
        assertEquals(4, hash.size());
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, (char)1);
        assertTrue(hash.contains((char)1));
        assertFalse(hash.contains((char)2));
        hash.insertKeyAt(0, (char)2);
        assertTrue(hash.contains((char)1));
        assertTrue(hash.contains((char)2));
    }

}