package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    // Test case for the default constructor
    @Test
    public void testDefaultConstructor() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        // Test initial size
        assertEquals(10, hash._set.length);
    }

    // Test case for the constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TFloatDoubleHash(100);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        // Test initial size
        assertEquals(100, hash._set.length);
    }

    // Test case for the constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TFloatDoubleHash(100, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        // Test initial size
        assertEquals(100, hash._set.length);
    }

    // Test case for the constructor with initial capacity, load factor, no_entry_key, and no_entry_value
    @Test
    public void testConstructorWithAllParameters() {
        hash = new TFloatDoubleHash(100, 0.5f, 1.0f, 2.0);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
        // Test initial size
        assertEquals(100, hash._set.length);
    }

    // Test case for setUp method
    @Test
    public void testSetUp() {
        int initialCapacity = 100;
        int capacity = hash.setUp(initialCapacity);
        assertEquals(initialCapacity, capacity);
        assertEquals(initialCapacity, hash._set.length);
    }

    // Test case for contains method when value exists in the hash
    @Test
    public void testContainsValueExists() {
        float val = 1.0f;
        hash.insertKey(val);
        assertTrue(hash.contains(val));
    }

    // Test case for contains method when value does not exist in the hash
    @Test
    public void testContainsValueDoesNotExist() {
        assertFalse(hash.contains(1.0f));
    }

    // Test case for forEach method when procedure returns true for all values
    @Test
    public void testForEachProcedureReturnsTrue() {
        float val = 1.0f;
        hash.insertKey(val);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    // Test case for forEach method when procedure returns false for some values
    @Test
    public void testForEachProcedureReturnsFalse() {
        float val1 = 1.0f;
        float val2 = 2.0f;
        hash.insertKey(val1);
        hash.insertKey(val2);

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value != val2;
            }
        };

        assertFalse(hash.forEach(procedure));
    }

    // Test case for removeAt method
    @Test
    public void testRemoveAt() {
        float val = 1.0f;
        hash.insertKey(val);

        hash.removeAt(0);

        assertFalse(hash.contains(val));
    }

    // Test case for index method when key exists in the hash
    @Test
    public void testIndexKeyExists() {
        float val = 1.0f;
        hash.insertKey(val);
        assertEquals(0, hash.index(val));
    }

    // Test case for index method when key does not exist in the hash
    @Test
    public void testIndexKeyDoesNotExist() {
        assertEquals(-1, hash.index(1.0f));
    }

    // Test case for indexRehashed method when key exists in the hash
    @Test
    public void testIndexRehashedKeyExists() {
        float val = 1.0f;
        hash.insertKey(val);
        assertEquals(0, hash.indexRehashed(val, 0, 0, (byte) 1));
    }

    // Test case for indexRehashed method when key does not exist in the hash
    @Test
    public void testIndexRehashedKeyDoesNotExist() {
        assertEquals(-1, hash.indexRehashed(1.0f, 0, 0, (byte) 1));
    }

    // Test case for insertKey method when key does not exist in the hash and there are no free slots available
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyNoFreeSlots() {
        // Fill up the hash table
        for (int i = 0; i < hash._set.length; i++) {
            hash.insertKey(i);
        }

        // Insert a new key that should cause an exception
        hash.insertKey(100);
    }

    // Test case for insertKey method when key does not exist in the hash and there are free slots available
    @Test
    public void testInsertKeyWithFreeSlots() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(2, hash.index(2.0f));
    }

    // Test case for XinsertKey method when key does not exist in the hash and there are no free slots available
    @Test(expected = IllegalStateException.class)
    public void testXInsertKeyNoFreeSlots() {
        // Fill up the hash table
        for (int i = 0; i < hash._set.length; i++) {
            hash.XinsertKey(i);
        }

        // Insert a new key that should cause an exception
        hash.XinsertKey(100);
    }

    // Test case for XinsertKey method when key does not exist in the hash and there are free slots available
    @Test
    public void testXInsertKeyWithFreeSlots() {
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);
        assertEquals(2, hash.index(2.0f));
    }

    // Test case for writeExternal method
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutputStream);

        objectOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        byteArrayOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        assertEquals((byte) 0, objectInputStream.readByte());

        objectInputStream.close();
        byteArrayInputStream.close();
    }

    // Test case for readExternal method
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutputStream);

        objectOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        byteArrayOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        objectInputStream.readByte();
        hash.readExternal(objectInputStream);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);

        objectInputStream.close();
        byteArrayInputStream.close();
    }
}