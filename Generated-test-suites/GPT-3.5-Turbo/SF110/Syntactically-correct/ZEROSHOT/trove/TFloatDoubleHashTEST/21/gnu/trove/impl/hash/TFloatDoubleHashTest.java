import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        assertTrue(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        hash._set[0] = 1.0f;

        hash.removeAt(0);

        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte)1));
    }

    @Test
    public void testInsertKey() {
        int index = hash.insertKey(1.0f);

        assertTrue(hash.consumeFreeSlot);
        assertEquals(1.0f, hash._set[index], 0.0001);
        assertEquals(1, hash._states[index]);
    }

    @Test
    public void testInsertKeyRehash() {
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        int index = hash.insertKeyRehash(2.0f, 0, 0, (byte)1);

        assertFalse(hash.consumeFreeSlot);
        assertEquals(0.0f, hash._set[0], 0.0001);
        assertEquals(1.0f, hash._set[index], 0.0001);
        assertEquals(1, hash._states[index]);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);

        assertEquals(1.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        int index = hash.XinsertKey(1.0f);

        assertTrue(hash.consumeFreeSlot);
        assertEquals(1.0f, hash._set[index], 0.0001);
        assertEquals(1, hash._states[index]);
    }

    @Test
    public void testWriteExternal() {
        // Cannot test as ObjectOutput is not available.
    }

    @Test
    public void testReadExternal() {
        // Cannot test as ObjectInput is not available.
    }
}