package gnu.trove.impl.hash;

import gnu.trove.impl.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertNotNull(hash);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    // Test constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        assertNotNull(hash);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    // Test constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f);
        assertNotNull(hash);
        assertEquals(10, hash.size());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    // Test constructor with initial capacity, load factor, no_entry_key, and no_entry_value
    @Test
    public void testConstructorWithAllParameters() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10, 0.75f, 1.0f, 2.0);
        assertNotNull(hash);
        assertEquals(10, hash.size());
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(2.0, hash.getNoEntryValue(), 0.0);
    }

    // Test getNoEntryKey
    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }

    // Test getNoEntryValue
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    // Test contains method
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    // Test forEach method
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0;

            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(6.0, procedure.sum, 0.0);
    }

    // Test removeAt method
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(2, hash.size());

        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        assertEquals(1, hash.size());
    }

    // Test index method
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    // Test indexRehashed method
    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        assertEquals(0, hash.indexRehashed(1.0f, 0, 12345, (byte)1));
        assertEquals(2, hash.indexRehashed(1.0f, 2, 12345, (byte)1));
        assertEquals(-1, hash.indexRehashed(1.0f, 1, 12345, (byte)1));
    }

    // Test insertKey method
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-2, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(2.0f));
    }

    // Test insertKeyRehash method
    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 12345, (byte)1));
        assertEquals(2, hash.insertKeyRehash(1.0f, 2, 12345, (byte)1));
        assertEquals(-2, hash.insertKeyRehash(1.0f, 0, 12345, (byte)1));
    }

    // Test insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
        hash.insertKeyAt(0, 2.0f);
        assertFalse(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
    }

    // Test writeExternal method
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(objectInputStream);

        assertTrue(newHash.contains(1.0f));
        assertTrue(newHash.contains(2.0f));
        assertFalse(newHash.contains(3.0f));
    }

    // Test readExternal method
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        TFloatDoubleHash newHash = new TFloatDoubleHash();
        newHash.readExternal(objectInputStream);

        assertTrue(newHash.contains(1.0f));
        assertTrue(newHash.contains(2.0f));
        assertFalse(newHash.contains(3.0f));
    }

}