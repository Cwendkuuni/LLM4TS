import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 100;
        hash.setUp(initialCapacity);

        assertEquals(initialCapacity, hash._set.length);
    }

    @Test
    public void testContains() {
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                if (value == 2.0f) {
                    return false;
                }
                return true;
            }
        };

        assertFalse(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(1);

        assertFalse(hash.contains(2.0f));
        assertEquals(0.0f, hash._set[1], 0.001);
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int firstRemovedIndex = hash._set.length - 1;

        hash.removeAt(1);
        hash.insertKeyRehash(4.0f, firstRemovedIndex, 5, (byte) 2);

        assertTrue(hash.contains(4.0f));
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(1, 1.0f);

        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        hash.XinsertKey(3.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testWriteExternal() {
        // test writeExternal
    }

    @Test
    public void testReadExternal() {
        // test readExternal
    }

}