import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructorNoArgs() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithAllArgs() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('a', hash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        float[] set = hash._set;
        assertEquals(10, set.length);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        assertTrue(hash.forEach(new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        hash.removeAt(0);
        assertEquals(1.0f, hash._set[0], 0.001);
        assertEquals(2, hash.size());
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        int index = hash.index(0.0f);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        int index = hash.indexRehashed(0.0f, 1, 1, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        int index = hash.insertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2.0f, hash._set[1], 0.001);
        assertTrue(hash.consumeFreeSlot);
        assertEquals(3, hash.size());
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        int index = hash.insertKeyRehash(2.0f, 1, 1, (byte) 1);
        assertEquals(0, index);
        assertEquals(2.0f, hash._set[0], 0.001);
        assertTrue(hash.consumeFreeSlot);
        assertEquals(3, hash.size());
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        hash.insertKeyAt(0, 2.0f);
        assertEquals(2.0f, hash._set[0], 0.001);
        assertEquals(1, hash.size());
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{0.0f, 1.0f};
        hash._states = new byte[]{1, 1};
        hash.no_entry_key = 1.0f;
        int index = hash.XinsertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2.0f, hash._set[1], 0.001);
        assertTrue(hash.consumeFreeSlot);
        assertEquals(3, hash.size());
    }

    @Test
    public void testWriteExternal() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        TFloatCharHash hash = new TFloatCharHash();
        hash.no_entry_key = 0.0f;
        hash.no_entry_value = '\0';
        hash.writeExternal(objectOutputStream);

        byte[] bytes = outputStream.toByteArray();
        assertEquals(0, bytes[0]);
        assertEquals(0, bytes[1]);
        assertEquals(0, bytes[2]);
        assertEquals(0, bytes[3]);
        assertEquals(0, bytes[4]);
        assertEquals(0, bytes[5]);
        assertEquals(0, bytes[6]);
        assertEquals(0, bytes[7]);
        assertEquals(63, bytes[8]);
        assertEquals(0, bytes[9]);
    }

    @Test
    public void testReadExternal() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 63, 0});
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TFloatCharHash hash = new TFloatCharHash();
        hash.readExternal(objectInputStream);

        assertEquals(0.0f, hash.no_entry_key, 0.001);
        assertEquals('\0', hash.no_entry_value);
    }

}