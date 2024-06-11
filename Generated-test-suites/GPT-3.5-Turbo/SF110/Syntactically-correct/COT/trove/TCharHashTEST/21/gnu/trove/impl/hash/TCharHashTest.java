
import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {

    @Test
    public void testTCharHash() {
        TCharHash hash = new TCharHash();
        assertNotNull(hash);
    }

    @Test
    public void testTCharHashWithInitialCapacity() {
        TCharHash hash = new TCharHash(10);
        assertNotNull(hash);
    }

    @Test
    public void testTCharHashWithInitialCapacityAndLoadFactor() {
        TCharHash hash = new TCharHash(10, 0.75f);
        assertNotNull(hash);
    }

    @Test
    public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
        char no_entry_value = 'X';
        TCharHash hash = new TCharHash(10, 0.75f, no_entry_value);
        assertEquals(no_entry_value, hash.getNoEntryValue());
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
        hash.insertKey('C');
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                return value != 'B';
            }
        };
        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        hash.removeAt(1);
        assertFalse(hash.contains('B'));
    }

    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        assertEquals(1, hash.index('B'));
        assertEquals(-1, hash.index('D'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        assertEquals(1, hash.indexRehashed('B', 1, 123, (byte) 1));
        assertEquals(-1, hash.indexRehashed('D', 1, 123, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash(10);
        hash.insertKey('A');
        hash.insertKey('B');
        hash.insertKey('C');
        assertTrue(hash.contains('A'));
        assertTrue(hash.contains('B'));
        assertTrue(hash.contains('C'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash(10);
        for (int i = 0; i < 100; i++) {
            hash.insertKey((char) i);
        }
        assertTrue(hash.contains((char) 0));
        assertTrue(hash.contains((char) 50));
        assertTrue(hash.contains((char) 99));
        assertFalse(hash.contains((char) 100));
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAtFull() {
        TCharHash hash = new TCharHash(10);
        for (int i = 0; i < 10; i++) {
            hash.insertKey((char) i);
        }
        hash.insertKey((char) 10);
    }
}

