// Test cases for TFloatCharHash

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        // Initialize the hash with initial capacity of 10 and load factor of 0.75
        hash = new TFloatCharHash(10, 0.75f);
    }

    @Test
    public void testGetNoEntryKey() {
        // Test getting the no entry key
        float noEntryKey = hash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        // Test getting the no entry value
        char noEntryValue = hash.getNoEntryValue();
        assertEquals('\0', noEntryValue);
    }

    @Test
    public void testContains() {
        // Test contains method for typical use case
        assertFalse(hash.contains(1.0f));

        // Test contains method for existing value
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));

        // Test contains method for no entry key
        hash.insertKey(hash.getNoEntryKey());
        assertFalse(hash.contains(hash.getNoEntryKey()));
    }

    @Test
    public void testForEach() {
        // Test forEach method for empty hash
        assertFalse(hash.forEach(value -> {
            fail("Should not execute this procedure");
            return true;
        }));

        // Test forEach method for non-empty hash
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.forEach(value -> {
            if (value == 1.0f) {
                return false;
            }
            return true;
        }));
        assertFalse(hash.forEach(null));
    }

    @Test
    public void testRemoveAt() {
        // Test removeAt method for non-existing index
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(5);
        assertEquals(1.0f, hash._set[0], 0.0f);
        assertEquals(2.0f, hash._set[1], 0.0f);
        assertEquals(2, hash.size());

        // Test removeAt method for existing index
        hash.removeAt(1);
        assertEquals(1.0f, hash._set[0], 0.0f);
        assertEquals(hash.getNoEntryKey(), hash._set[1], 0.0f);
        assertEquals(1, hash.size());
    }

    @Test
    public void testIndex() {
        // Test index method for non-existing key
        assertEquals(-1, hash.index(3.0f));

        // Test index method for existing key
        hash.insertKey(3.0f);
        assertEquals(0, hash.index(3.0f));

        // Test index method for no entry key
        hash.insertKey(hash.getNoEntryKey());
        assertEquals(-1, hash.index(hash.getNoEntryKey()));
    }

    @Test
    public void testInsertKey() {
        // Test insertKey method for non-existing key
        int index = hash.insertKey(4.0f);
        assertEquals(0, index);
        assertEquals(4.0f, hash._set[0], 0.0f);
        assertEquals(1, hash.size());

        // Test insertKey method for existing key
        index = hash.insertKey(4.0f);
        assertEquals(-1, index);
        assertEquals(4.0f, hash._set[0], 0.0f);
        assertEquals(1, hash.size());

        // Test insertKey method for no entry key
        index = hash.insertKey(hash.getNoEntryKey());
        assertEquals(1, index);
        assertEquals(hash.getNoEntryKey(), hash._set[1], 0.0f);
        assertEquals(2, hash.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        // Test insertKeyRehash method for exceeding maximum capacity
        // Setting initial capacity to 1 to trigger the exception
        TFloatCharHash smallHash = new TFloatCharHash(1, 0.75f);
        smallHash.insertKey(1.0f);
        smallHash.insertKey(2.0f);
    }

    @Test
    public void testInsertKeyAt() {
        // Test insertKeyAt method for inserting key
        hash.insertKeyAt(0, 5.0f);
        assertEquals(5.0f, hash._set[0], 0.0f);
        assertEquals(1, hash.size());

        // Test insertKeyAt method for inserting no entry key
        hash.insertKeyAt(1, hash.getNoEntryKey());
        assertEquals(hash.getNoEntryKey(), hash._set[1], 0.0f);
        assertEquals(2, hash.size());
    }

    @Test
    public void testWriteExternal() throws IOException {
        // Test writeExternal method
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutput);
        objectOutput.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        assertEquals(0, bytes[0]);
        assertEquals(0, bytes[1]);
        assertEquals(0, bytes[2]);
        assertEquals(0, bytes[3]);
        assertEquals(0, bytes[4]);
        assertEquals(0, bytes[5]);
        assertEquals(0, bytes[6]);
        assertEquals(0, bytes[7]);
        assertEquals(0, bytes[8]);
        assertEquals(0, bytes[9]);
        assertEquals(63, bytes[10]); // no_entry_key value as float
        assertEquals(0, bytes[11]);
        assertEquals(0, bytes[12]);
        assertEquals(0, bytes[13]);
        assertEquals(0, bytes[14]); // no_entry_value value as char
        assertEquals(0, bytes[15]);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        // Test readExternal method
        byte[] bytes = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 63, 0, 0, 0, 0, 0};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);

        hash.readExternal(objectInput);
        assertEquals(0.0f, hash.no_entry_key, 0.0f);
        assertEquals('\0', hash.no_entry_value);
    }
}