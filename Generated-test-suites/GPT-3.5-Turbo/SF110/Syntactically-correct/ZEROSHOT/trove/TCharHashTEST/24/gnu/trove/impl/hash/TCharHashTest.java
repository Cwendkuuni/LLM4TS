import org.junit.*;
import static org.junit.Assert.*;

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
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TCharHash hash = new TCharHash(initialCapacity, loadFactor);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(loadFactor, hash.loadFactor(), 0.001);
        assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }

    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char no_entry_value = 'X';
        TCharHash hash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(loadFactor, hash.loadFactor(), 0.001);
        assertEquals(no_entry_value, hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, hash.capacity());
        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
        assertFalse(hash.contains('D'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        hash.removeAt(0);
        assertFalse(hash.contains('A'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        assertEquals(0, hash.index('A'));
        assertEquals(1, hash.index('B'));
        assertEquals(2, hash.index('C'));
        assertEquals(-1, hash.index('D'));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        int index1 = hash.insertKey('A');
        int index2 = hash.insertKey('B');
        assertEquals(0, index1);
        assertEquals(1, index2);
        assertFalse(hash.contains('D'));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(3);
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        hash.insertKey('D');
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash(3);
        hash.insertKeyAt(0, 'A');
        hash.insertKeyAt(1, 'B');
        hash.insertKeyAt(2, 'C');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
    }

}