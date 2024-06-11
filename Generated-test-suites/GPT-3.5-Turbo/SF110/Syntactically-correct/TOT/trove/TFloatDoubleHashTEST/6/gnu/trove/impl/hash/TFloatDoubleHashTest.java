import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    // Test case for the constructor with no arguments
    @Test
    public void testDefaultConstructor() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertEquals(10, hash._states.length);
        assertEquals(10, hash._set.length);
    }

    // Test case for the constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TFloatDoubleHash(5);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertEquals(5, hash._states.length);
        assertEquals(5, hash._set.length);
    }

    // Test case for the constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TFloatDoubleHash(100, 0.8f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
        assertEquals(100, hash._states.length);
        assertEquals(100, hash._set.length);
    }

    // Test case for the constructor with initial capacity, load factor, and no_entry_key, no_entry_value
    @Test
    public void testConstructorWithAllArguments() {
        hash = new TFloatDoubleHash(50, 0.5f, 10.0f, 20.0);
        assertEquals(10.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(20.0, hash.getNoEntryValue(), 0.0);
        assertEquals(50, hash._states.length);
        assertEquals(50, hash._set.length);
    }

    // Test case for the contains method with existing value
    @Test
    public void testContainsExistingValue() {
        hash.insertKey(10.0f);
        assertTrue(hash.contains(10.0f));
    }

    // Test case for the contains method with non-existing value
    @Test
    public void testContainsNonExistingValue() {
        assertFalse(hash.contains(5.0f));
    }

    // Test case for the forEach method with a non-empty hash
    @Test
    public void testForEachNonEmptyHash() {
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(60.0, procedure.sum, 0.0);
    }

    // Test case for the forEach method with an empty hash
    @Test
    public void testForEachEmptyHash() {
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
        assertEquals(0.0, procedure.sum, 0.0);
    }

    // Test case for the removeAt method
    @Test
    public void testRemoveAt() {
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(20.0f));
    }

    // Test case for the index method with existing key
    @Test
    public void testIndexExistingKey() {
        hash.insertKey(10.0f);
        assertEquals(0, hash.index(10.0f));
    }

    // Test case for the index method with non-existing key
    @Test
    public void testIndexNonExistingKey() {
        assertEquals(-1, hash.index(5.0f));
    }

    // Test case for the indexRehashed method with existing key
    @Test
    public void testIndexRehashedExistingKey() {
        hash.insertKey(10.0f);
        assertEquals(0, hash.indexRehashed(10.0f, 0, 10, (byte)1));
    }

    // Test case for the indexRehashed method with non-existing key
    @Test
    public void testIndexRehashedNonExistingKey() {
        assertEquals(-1, hash.indexRehashed(5.0f, 0, 5, (byte)1));
    }

    // Test case for the insertKey method with free slot
    @Test
    public void testInsertKeyWithFreeSlot() {
        assertEquals(0, hash.insertKey(10.0f));
    }

    // Test case for the insertKey method with existing key
    @Test
    public void testInsertKeyExistingKey() {
        hash.insertKey(10.0f);
        assertEquals(-1, hash.insertKey(10.0f));
    }

    // Test case for the insertKey method with no free slot
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyNoFreeSlot() {
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);
        hash.insertKey(40.0f);
        hash.insertKey(50.0f);
        hash.insertKey(60.0f);
        hash.insertKey(70.0f);
        hash.insertKey(80.0f);
        hash.insertKey(90.0f);
        hash.insertKey(100.0f);
        hash.insertKey(110.0f);
    }

    // Test case for the insertKeyRehash method with free slot
    @Test
    public void testInsertKeyRehashWithFreeSlot() {
        hash.insertKey(10.0f);
        assertEquals(0, hash.insertKeyRehash(20.0f, 0, 20, (byte)1));
    }

    // Test case for the insertKeyRehash method with existing key
    @Test
    public void testInsertKeyRehashExistingKey() {
        hash.insertKey(10.0f);
        assertEquals(-1, hash.insertKeyRehash(10.0f, 0, 10, (byte)1));
    }

    // Test case for the insertKeyRehash method with no free slot
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashNoFreeSlot() {
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.insertKey(30.0f);
        hash.insertKey(40.0f);
        hash.insertKey(50.0f);
        hash.insertKey(60.0f);
        hash.insertKey(70.0f);
        hash.insertKey(80.0f);
        hash.insertKey(90.0f);
        hash.insertKey(100.0f);
        hash.insertKeyRehash(110.0f, 0, 110, (byte)1);
    }

    // Test case for the insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 10.0f);
        assertEquals(10.0f, hash._set[0], 0.0);
        assertEquals((byte)1, hash._states[0]);
    }

    // Test case for the XinsertKey method with free slot
    @Test
    public void testXinsertKeyWithFreeSlot() {
        assertEquals(0, hash.XinsertKey(10.0f));
    }

    // Test case for the XinsertKey method with existing key
    @Test
    public void testXinsertKeyExistingKey() {
        hash.XinsertKey(10.0f);
        assertEquals(-1, hash.XinsertKey(10.0f));
    }

    // Test case for the XinsertKey method with no free slot
    @Test(expected = IllegalStateException.class)
    public void testXinsertKeyNoFreeSlot() {
        hash.XinsertKey(10.0f);
        hash.XinsertKey(20.0f);
        hash.XinsertKey(30.0f);
        hash.XinsertKey(40.0f);
        hash.XinsertKey(50.0f);
        hash.XinsertKey(60.0f);
        hash.XinsertKey(70.0f);
        hash.XinsertKey(80.0f);
        hash.XinsertKey(90.0f);
        hash.XinsertKey(100.0f);
        hash.XinsertKey(110.0f);
    }

    // Test case for the writeExternal method
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.writeExternal(oos);
        oos.flush();
        byte[] data = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(ois);

        assertTrue(newHash.contains(10.0f));
        assertTrue(newHash.contains(20.0f));
    }

    // Test case for the readExternal method
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        hash.insertKey(10.0f);
        hash.insertKey(20.0f);
        hash.writeExternal(oos);
        oos.flush();
        byte[] data = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        hash.readExternal(ois);

        assertTrue(hash.contains(10.0f));
        assertTrue(hash.contains(20.0f));
    }

}