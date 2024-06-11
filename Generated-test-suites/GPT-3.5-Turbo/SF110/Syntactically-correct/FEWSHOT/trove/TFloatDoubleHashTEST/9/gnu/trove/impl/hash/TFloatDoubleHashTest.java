import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash(10, 0.5f);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, hash.capacity());
        assertEquals(0.5f, hash.loadFactor(), 0.01f);
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.01);
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));

        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final float[] keys = new float[3];
        hash.forEach(new TFloatProcedure() {
            private int index = 0;

            @Override
            public boolean execute(float value) {
                keys[index++] = value;
                return true;
            }
        });

        assertEquals(1.0f, keys[0], 0.01f);
        assertEquals(2.0f, keys[1], 0.01f);
        assertEquals(3.0f, keys[2], 0.01f);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));

        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(-1, hash.index(2.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertFalse(hash.indexRehashed(1.0f, 0, 0, (byte) 0));

        hash.insertKey(1.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed(2.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
        assertTrue(hash.consumeFreeSlot);

        hash.insertKey(2.0f);
        assertEquals(1, hash.insertKey(2.0f));
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte) 0));

        hash.insertKey(1.0f);
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte) 1));
        assertEquals(-1, hash.insertKeyRehash(2.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));

        hash.insertKeyAt(1, 2.0f);
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));

        hash.XinsertKey(2.0f);
        assertEquals(1, hash.XinsertKey(2.0f));
    }

}