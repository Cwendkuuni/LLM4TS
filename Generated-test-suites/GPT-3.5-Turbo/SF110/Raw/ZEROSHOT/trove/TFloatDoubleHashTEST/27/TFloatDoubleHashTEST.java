package gnu.trove.impl.hash;

import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0.0f, hashMap.getNoEntryKey(), 0.01);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash();
        assertEquals(0.0, hashMap.getNoEntryValue(), 0.01);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        assertTrue(hashMap.contains(1.0f));
        assertFalse(hashMap.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);
        hashMap.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(hashMap.forEach(procedure));
        assertEquals(6.0, procedure.sum, 0.01);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        hashMap.removeAt(0);
        assertFalse(hashMap.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        assertEquals(0, hashMap.index(1.0f));
        assertEquals(-1, hashMap.index(2.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        assertEquals(-1, hashMap.indexRehashed(2.0f, 0, 0, (byte)0));
        assertEquals(-1, hashMap.indexRehashed(1.0f, 0, 0, (byte)2));
        assertEquals(0, hashMap.indexRehashed(1.0f, 0, 0, (byte)1));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(0, hashMap.insertKey(1.0f));
        assertEquals(-1, hashMap.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKey(1.0f);
        assertEquals(1, hashMap.insertKeyRehash(2.0f, 0, 0, (byte)0));
        assertEquals(1, hashMap.insertKeyRehash(2.0f, 0, 0, (byte)1));
        assertEquals(1, hashMap.insertKeyRehash(1.0f, 5, 5, (byte)2));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        hashMap.insertKeyAt(0, 1.0f);
        assertTrue(hashMap.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hashMap = new TFloatDoubleHash(10);
        assertEquals(0, hashMap.XinsertKey(1.0f));
        assertEquals(-1, hashMap.XinsertKey(1.0f));
    }

}