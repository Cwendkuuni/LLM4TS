package gnu.trove.impl.hash;

import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float key = hash.getNoEntryKey();
        assertEquals(0.0f, key, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char value = hash.getNoEntryValue();
        assertEquals('\0', value);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        final StringBuilder sb = new StringBuilder();
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                sb.append(value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals("1.02.0", sb.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);

        hash.removeAt(0);

        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);

        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = hash.indexRehashed(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);

        index = hash.indexRehashed(2.0f, 0, 0, (byte) 1);
        assertEquals(1, index);

        index = hash.indexRehashed(3.0f, 0, 0, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();

        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        index = hash.insertKey(1.0f);
        assertEquals(-1, index);

        index = hash.insertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);

        index = hash.insertKeyRehash(2.0f, 1, 1, (byte) 1);
        assertEquals(1, index);

        index = hash.insertKeyRehash(3.0f, 0, 0, (byte) 1);
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();

        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);

        index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertTrue(hash.contains(2.0f));
    }

}