package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        double noEntryValue = hash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(10);
        assertEquals(16, capacity); // Initial capacity should be rounded up to the nearest power of 2

        float[] set = hash._set;
        assertEquals(16, set.length); // The size of _set should match the capacity

        byte[] states = hash._states;
        assertEquals(16, states.length); // The size of _states should match the capacity
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        boolean contains1 = hash.contains(1.0f);
        boolean contains2 = hash.contains(2.0f);
        boolean contains3 = hash.contains(3.0f);
        boolean contains4 = hash.contains(4.0f);

        assertTrue(contains1);
        assertTrue(contains2);
        assertTrue(contains3);
        assertFalse(contains4);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };

        boolean forEachResult = hash.forEach(procedure);
        assertTrue(forEachResult);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        hash.removeAt(1);

        assertEquals(1.0f, hash._set[1], 0.0);
        assertEquals(2, hash._states[1]);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index1 = hash.index(1.0f);
        int index2 = hash.index(2.0f);
        int index3 = hash.index(3.0f);
        int index4 = hash.index(4.0f);

        assertEquals(1, index1);
        assertEquals(2, index2);
        assertEquals(0, index3);
        assertEquals(-1, index4);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.indexRehashed(1.0f, 0, 0, (byte)1);

        assertEquals(1, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        float[] set = hash._set;
        byte[] states = hash._states;

        assertEquals(1.0f, set[1], 0.0);
        assertEquals(2.0f, set[2], 0.0);
        assertEquals(3.0f, set[0], 0.0);

        assertEquals(1, states[1]);
        assertEquals(1, states[2]);
        assertEquals(1, states[0]);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte)1);

        assertEquals(1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyThrowsException() {
        TFloatDoubleHash hash = new TFloatDoubleHash(1, 0.8f, 0.0f, 0.0);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);
        hash.insertKeyAt(2, 3.0f);

        float[] set = hash._set;
        byte[] states = hash._states;

        assertEquals(1.0f, set[0], 0.0);
        assertEquals(2.0f, set[1], 0.0);
        assertEquals(3.0f, set[2], 0.0);

        assertEquals(1, states[0]);
        assertEquals(1, states[1]);
        assertEquals(1, states[2]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        int index = hash.insertKey(1.0f);

        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.writeExternal(objectOutputStream);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{0});
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.readExternal(objectInputStream);
    }
}