package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(1.0f));
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public double getSum() {
                return sum;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(6.0, procedure.getSum(), 0.0001);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(3, hash.size());
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.size());
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(1, hash.size());
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.size());
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(1.0f));
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte)0));
        hash.insertKey(1.0f);
        assertEquals(0, hash.indexRehashed(1.0f, 0, 0, (byte)0));
    }

    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte)0));
        assertEquals(1, hash.insertKeyRehash(1.0f, 0, 0, (byte)1));
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(1.0f));
    }

    @Test
    public void testWriteExternal() {
        // TODO: Implement this test
    }

    @Test
    public void testReadExternal() {
        // TODO: Implement this test
    }
}