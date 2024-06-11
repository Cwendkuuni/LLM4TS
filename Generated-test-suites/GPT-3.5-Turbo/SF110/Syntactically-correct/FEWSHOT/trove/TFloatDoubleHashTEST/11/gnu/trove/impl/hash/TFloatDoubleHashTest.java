package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TFloatDoubleHashTest {
    @Test
    public void testConstructorDefault() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorInitialCapacity() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(10);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorInitialCapacityLoadFactor() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(10, 0.5f);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testConstructorInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(10, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
        assertEquals(2.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int capacity = tFloatDoubleHash.setUp(10);
        assertEquals(10, capacity);
        assertTrue(tFloatDoubleHash._set.length >= capacity);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        assertTrue(tFloatDoubleHash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);

        TFloatProcedure tFloatProcedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };

        assertTrue(tFloatDoubleHash.forEach(tFloatProcedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.removeAt(1);
        assertNull(tFloatDoubleHash._set[1]);
        assertEquals(0, tFloatDoubleHash._states[1]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        int index = tFloatDoubleHash.index(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        int index = tFloatDoubleHash.indexRehashed(1.0f, 0, 123, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int index = tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int index = tFloatDoubleHash.insertKeyRehash(1.0f, 0, 123, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, tFloatDoubleHash._set[0], 0.0);
        assertEquals(1, tFloatDoubleHash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int index = tFloatDoubleHash.XinsertKey(1.0f);
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testXinsertKeyFull() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(1);
        tFloatDoubleHash.XinsertKey(1.0f);
        tFloatDoubleHash.XinsertKey(2.0f);
    }
        
    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        tFloatDoubleHash.writeExternal(out);
        out.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        assertEquals(0, byteArray[0]);
        // Additional assertions for the rest of the byte array
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
        tFloatDoubleHash.writeExternal(out);
        out.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
        tFloatDoubleHash.readExternal(in);

        // Additional assertions for the state of tFloatDoubleHash
    }
}