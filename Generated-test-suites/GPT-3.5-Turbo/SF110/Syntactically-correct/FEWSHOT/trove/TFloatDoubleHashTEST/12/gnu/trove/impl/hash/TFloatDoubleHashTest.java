package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructorNoArgs() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f);
        assertEquals(10, hash.capacity());
        assertEquals(0.8f, hash.loadFactor(), 0.0);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        assertEquals(10, hash.capacity());
        assertEquals(0.8f, hash.loadFactor(), 0.0);
        assertEquals(3.14f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.718, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        assertFalse(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        assertFalse(hash.contains(3.0f));
    }

    @Test
    public void testForEachWithProcedure() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        final double[] result = {0.0};
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                result[0] += value;
                return true;
            }
        };

        hash.forEach(procedure);

        assertEquals(0.0, result[0], 0.0);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;
        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0);
        assertEquals(0, hash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        int index = hash.index(1.0f);

        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        int index = hash.indexRehashed(1.0f, 0, 0, (byte) 0);

        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        int index = hash.insertKey(2.0f);

        assertEquals(1, index);
        assertEquals(2.0f, hash._set[1], 0.0);
        assertEquals(1, hash._states[1]);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        int index = hash.insertKeyRehash(2.0f, 0, 0, (byte) 0);

        assertEquals(1, index);
        assertEquals(2.0f, hash._set[1], 0.0);
        assertEquals(1, hash._states[1]);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash.insertKeyAt(0, 2.0f);
        assertEquals(2.0f, hash._set[0], 0.0);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash._set[0] = 1.0f;
        hash._states[0] = 1;

        int index = hash.XinsertKey(2.0f);

        assertEquals(1, index);
        assertEquals(2.0f, hash._set[1], 0.0);
        assertEquals(1, hash._states[1]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.8f, 3.14f, 2.718);
        hash.writeExternal(objectOutput);

        objectOutput.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        assertEquals(0, bytes[0]);
        // Additional assertions for specific values written
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        byte[] bytes = new byte[8];
        bytes[0] = 0;
        // Additional code to write specific values to the byte array

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.readExternal(objectInput);

        // Assertions for specific values read
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
}