package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float result = hash.getNoEntryKey();
        assertEquals(0.0f, result, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double result = hash.getNoEntryValue();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        boolean result = hash.contains(1.0f);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        };
        boolean result = hash.forEach(procedure);
        assertTrue(result);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int result = hash.setUp(10);
        assertEquals(10, result);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash.removeAt(0);
        float[] set = hash._set;
        assertEquals(0.0, set[0], 0.0);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int result = hash.index(1.0f);
        assertEquals(-1, result);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash._states[0] = 1;
        hash._set[0] = 1.0f;
        int result = hash.indexRehashed(1.0f, 0, 0, (byte) 1);
        assertEquals(0, result);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int result = hash.insertKey(1.0f);
        assertEquals(0, result);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash._states[0] = 2;
        int result = hash.insertKeyRehash(1.0f, 0, 0, (byte) 2);
        assertEquals(-9, result);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        hash.insertKeyAt(0, 1.0f);
        float[] set = hash._set;
        assertEquals(1.0f, set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.setUp(10);
        int result = hash.XinsertKey(1.0f);
        assertEquals(0, result);
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.writeExternal(null);
        } catch (IOException e) {
            fail("An exception should not occur");
        }
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        try {
            hash.readExternal(null);
        } catch (IOException e) {
            fail("An exception should not occur");
        } catch (ClassNotFoundException e) {
            fail("An exception should not occur");
        }
    }

}