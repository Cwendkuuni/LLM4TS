
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testConstructor1() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertNotNull(tFloatDoubleHash);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(initialCapacity);
        assertNotNull(tFloatDoubleHash);
        assertEquals(initialCapacity, tFloatDoubleHash._set.length);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(initialCapacity, loadFactor);
        assertNotNull(tFloatDoubleHash);
        assertEquals(initialCapacity, tFloatDoubleHash._set.length);
        assertEquals(loadFactor, tFloatDoubleHash._loadFactor, 0.0001);
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0001);
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        float noEntryKey = -1.0f;
        double noEntryValue = -1.0;
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash(initialCapacity, loadFactor, noEntryKey, noEntryValue);
        assertNotNull(tFloatDoubleHash);
        assertEquals(initialCapacity, tFloatDoubleHash._set.length);
        assertEquals(loadFactor, tFloatDoubleHash._loadFactor, 0.0001);
        assertEquals(noEntryKey, tFloatDoubleHash.getNoEntryKey(), 0.0001);
        assertEquals(noEntryValue, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testGetNoEntryKey() {
        float noEntryKey = -1.0f;
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.no_entry_key = noEntryKey;
        assertEquals(noEntryKey, tFloatDoubleHash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        double noEntryValue = -1.0;
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.no_entry_value = noEntryValue;
        assertEquals(noEntryValue, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        int capacity = tFloatDoubleHash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertNotNull(tFloatDoubleHash._set);
        assertEquals(initialCapacity, tFloatDoubleHash._set.length);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);

        assertTrue(tFloatDoubleHash.contains(1.0f));
        assertFalse(tFloatDoubleHash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);

        TFloatProcedure tFloatProcedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value >= 1.0f;
            }
        };

        assertFalse(tFloatDoubleHash.forEach(tFloatProcedure));

        tFloatProcedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value >= 0.0f;
            }
        };

        assertTrue(tFloatDoubleHash.forEach(tFloatProcedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);

        assertEquals(1.0f, tFloatDoubleHash._set[0], 0.0001);

        tFloatDoubleHash.removeAt(0);

        assertEquals(tFloatDoubleHash.no_entry_key, tFloatDoubleHash._set[0], 0.0001);
        assertEquals(0, tFloatDoubleHash._states[0]);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);

        assertEquals(0, tFloatDoubleHash.index(1.0f));
        assertEquals(1, tFloatDoubleHash.index(2.0f));
        assertEquals(-1, tFloatDoubleHash.index(3.0f));
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.insertKey(1.0f);
        tFloatDoubleHash.insertKey(2.0f);
        tFloatDoubleHash.insertKey(3.0f);

        assertEquals(0, tFloatDoubleHash.indexRehashed(1.0f, 0, 0, (byte) 1));
        assertEquals(1, tFloatDoubleHash.indexRehashed(2.0f, 0, 0, (byte) 1));
        assertEquals(2, tFloatDoubleHash.indexRehashed(3.0f, 0, 0, (byte) 1));
        assertEquals(-1, tFloatDoubleHash.indexRehashed(4.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();

        assertEquals(0, tFloatDoubleHash.insertKey(1.0f));
        assertEquals(1, tFloatDoubleHash.insertKey(2.0f));
        assertEquals(2, tFloatDoubleHash.insertKey(3.0f));
        assertEquals(-3, tFloatDoubleHash.insertKey(1.0f));
        assertEquals(3, tFloatDoubleHash.insertKey(4.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();

        assertEquals(0, tFloatDoubleHash.insertKeyRehash(1.0f, 0, 0, (byte) 1));
        assertEquals(1, tFloatDoubleHash.insertKeyRehash(2.0f, 0, 0, (byte) 1));
        assertEquals(2, tFloatDoubleHash.insertKeyRehash(3.0f, 0, 0, (byte) 1));
        assertEquals(-3, tFloatDoubleHash.insertKeyRehash(1.0f, 0, 0, (byte) 1));
        assertEquals(3, tFloatDoubleHash.insertKeyRehash(4.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();

        tFloatDoubleHash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, tFloatDoubleHash._set[0], 0.0001);
        assertEquals(1, tFloatDoubleHash._states[0]);

        tFloatDoubleHash.insertKeyAt(1, 2.0f);
        assertEquals(2.0f, tFloatDoubleHash._set[1], 0.0001);
        assertEquals(1, tFloatDoubleHash._states[1]);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();

        assertEquals(0, tFloatDoubleHash.XinsertKey(1.0f));
        assertEquals(1, tFloatDoubleHash.XinsertKey(2.0f));
        assertEquals(2, tFloatDoubleHash.XinsertKey(3.0f));
        assertEquals(-3, tFloatDoubleHash.XinsertKey(1.0f));
        assertEquals(3, tFloatDoubleHash.XinsertKey(4.0f));
    }

    @Test
    public void testWriteExternal() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.no_entry_key = -1.0f;
        tFloatDoubleHash.no_entry_value = -1.0;
        tFloatDoubleHash.writeExternal(objectOutputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        assertEquals(0, objectInputStream.readByte());
        objectInputStream.readByte();
        objectInputStream.readFloat();
        objectInputStream.readDouble();
    }

    @Test
    public void testReadExternal() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeByte(0);
        objectOutputStream.writeByte(1);
        objectOutputStream.writeFloat(-1.0f);
        objectOutputStream.writeDouble(-1.0);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.readExternal(objectInputStream);

        assertEquals(-1.0f, tFloatDoubleHash.no_entry_key, 0.0001);
        assertEquals(-1.0, tFloatDoubleHash.no_entry_value, 0.0001);
    }
}
