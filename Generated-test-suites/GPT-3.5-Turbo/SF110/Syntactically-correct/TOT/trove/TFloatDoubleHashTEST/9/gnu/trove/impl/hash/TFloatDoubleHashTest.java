import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash hash;

    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TFloatDoubleHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
        // Test that the capacity is correctly set
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TFloatDoubleHash(10, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
        // Test that the capacity and load factor are correctly set
        assertEquals(10, hash._set.length);
        assertEquals(0.75f, hash._loadFactor, 0);
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
        hash = new TFloatDoubleHash(10, 0.75f, -1.0f, -1.0);
        assertEquals(-1.0f, hash.getNoEntryKey(), 0);
        assertEquals(-1.0, hash.getNoEntryValue(), 0);
        // Test that the capacity, load factor, no entry key, and no entry value are correctly set
        assertEquals(10, hash._set.length);
        assertEquals(0.75f, hash._loadFactor, 0);
    }

    @Test
    public void testContains() {
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };
        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        assertEquals(1, hash._size);
        hash.removeAt(0);
        // Test that the entry at index 0 is removed
        assertEquals(0, hash._size);
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        assertEquals(2, hash.index(3.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
        assertEquals(1, hash.insertKey(2.0f));
        assertEquals(2, hash.insertKey(3.0f));
        assertEquals(-4, hash.insertKey(4.0f));
    }

    @Test
    public void testXInsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(-1, hash.XinsertKey(1.0f));
        assertEquals(1, hash.XinsertKey(2.0f));
        assertEquals(2, hash.XinsertKey(3.0f));
        assertEquals(-4, hash.XinsertKey(4.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        hash.readExternal(objectInputStream);

        assertEquals(0.0f, hash.getNoEntryKey(), 0);
        assertEquals(0.0, hash.getNoEntryValue(), 0);
    }

}