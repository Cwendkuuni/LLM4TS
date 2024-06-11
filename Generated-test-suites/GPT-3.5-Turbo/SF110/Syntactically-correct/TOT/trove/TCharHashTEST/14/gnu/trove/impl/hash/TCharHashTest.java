import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash hash = new TCharHash();
        assertNotNull(hash);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TCharHash hash = new TCharHash(10);
        assertNotNull(hash);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testConstructor3() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testConstructor4() {
        TCharHash hash = new TCharHash(10, 0.75f, 'A');
        assertNotNull(hash);
        assertEquals('A', hash.getNoEntryValue());
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertNotNull(hash._set);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        assertTrue(hash.contains('A'));
        assertFalse(hash.contains('B'));
    }

    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
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
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.removeAt(0);
        assertFalse(hash.contains('A'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        assertEquals(0, hash.index('A'));
        assertEquals(-1, hash.index('B'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
        assertEquals(0, hash.indexRehashed('A', 0, 0, (byte) 1));
        assertEquals(1, hash.indexRehashed('B', 1, 1, (byte) 1));
        assertEquals(-1, hash.indexRehashed('C', 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(10);
        int index1 = hash.insertKey('A');
        int index2 = hash.insertKey('B');
        assertEquals(0, index1);
        assertEquals(1, index2);
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
        int index = hash.insertKeyRehash('C', 0, 0, (byte) 0);
        assertEquals(2, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash(1);
        hash.insertKeyAt(0, 'A');
        hash.insertKeyAt(0, 'B');
    }
}