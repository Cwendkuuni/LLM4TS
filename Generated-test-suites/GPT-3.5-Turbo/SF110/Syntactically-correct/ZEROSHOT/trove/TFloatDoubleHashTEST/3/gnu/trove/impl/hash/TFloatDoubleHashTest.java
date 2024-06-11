package gnu.trove.impl.hash;

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
    public void testSetUp() {
        int capacity = 100;
        int result = hash.setUp(capacity);
        assertEquals(capacity, result);
        assertEquals(capacity, hash._set.length);
    }

    @Test
    public void testContains() {
        float val = 10.0f;
        assertFalse(hash.contains(val));

        hash.insertKey(val);

        assertTrue(hash.contains(val));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            int count = 0;

            @Override
            public boolean execute(float value) {
                count++;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(3, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index = hash.index(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));

        hash.removeAt(index);

        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);

        int index = hash.index(20.0f);

        assertEquals(1, index);
    }

    @Test
    public void testInsertKey() {
        float val = 10.0f;
        int index = hash.insertKey(val);

        assertTrue(hash.contains(val));
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        float val = 10.0f;
        hash.insertKey(val);

        val = 20.0f;
        int index = hash.insertKeyRehash(val, 0, 0, (byte) 1);

        assertTrue(hash.contains(val));
        assertEquals(1, index);
    }

    @Test
    public void testInsertKeyAt() {
        float val = 10.0f;
        int index = 0;
        hash.insertKeyAt(index, val);

        assertTrue(hash.contains(val));
        assertEquals(10.0f, hash._set[index], 0.0001);
    }

    @Test
    public void testXinsertKey() {
        float key = 10.0f;
        hash.XinsertKey(key);

        assertTrue(hash.contains(key));
    }

    @Test
    public void testWriteExternal() {
        // Not testable
    }

    @Test
    public void testReadExternal() {
        // Not testable
    }

}