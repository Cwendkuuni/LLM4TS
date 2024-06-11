package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash hash = new TFloatDoubleHash();

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructor2() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructor3() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructor4() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);

        assertEquals(1.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(20);

        assertEquals(20, capacity);
        assertEquals(20, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final float[] sum = {0.0f};
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(6.0f, sum[0], 0.0001);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.removeAt(1);

        assertEquals(1, hash.size());
        assertEquals(0.0f, hash._set[1], 0.0001);
        assertEquals(0, hash._states[1]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index1 = hash.index(1.0f);
        int index2 = hash.index(2.0f);
        int index3 = hash.index(3.0f);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, index3);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index1 = hash.indexRehashed(1.0f, 0, 0, (byte) 1);
        int index2 = hash.indexRehashed(3.0f, 0, 0, (byte) 0);

        assertEquals(0, index1);
        assertEquals(-1, index2);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);

        int index1 = hash.insertKey(1.0f);
        int index2 = hash.insertKey(2.0f);
        int index3 = hash.insertKey(1.0f);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, index3);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        int index1 = hash.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        int index2 = hash.insertKeyRehash(3.0f, 0, 0, (byte) 1);
        int index3 = hash.insertKeyRehash(3.0f, 1, 0, (byte) 2);

        assertEquals(0, index1);
        assertEquals(2, index2);
        assertEquals(1, index3);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);

        hash.insertKeyAt(0, 1.0f);
        hash.insertKeyAt(1, 2.0f);

        assertEquals(1.0f, hash._set[0], 0.0001);
        assertEquals(2.0f, hash._set[1], 0.0001);
        assertEquals(1, hash._states[0]);
        assertEquals(1, hash._states[1]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);

        int index1 = hash.XinsertKey(1.0f);
        int index2 = hash.XinsertKey(2.0f);
        int index3 = hash.XinsertKey(1.0f);
        int index4 = hash.XinsertKey(3.0f);

        assertEquals(0, index1);
        assertEquals(1, index2);
        assertEquals(-1, index3);
        assertEquals(2, index4);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        hash.writeExternal(oos);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        assertEquals(0, ois.readByte());
        ois.readByte();
        hash.readExternal(ois);

        assertEquals(1.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {0});
        ObjectInputStream ois = new ObjectInputStream(bais);

        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        ois.readByte();
        hash.readExternal(ois);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0001);
    }

}