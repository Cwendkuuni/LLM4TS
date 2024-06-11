import org.junit.*;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        // Additional assertions for initial capacity
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('\0', hash.getNoEntryValue());
        // Additional assertions for initial capacity and load factor
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValues() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.001);
        assertEquals('a', hash.getNoEntryValue());
        // Additional assertions for initial capacity, load factor, and no entry values
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        // Additional assertions for contains method
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        // Additional assertions for forEach method
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
        // Additional assertions for removeAt method
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertEquals(0, hash.index(1.0f));
        assertEquals(1, hash.index(2.0f));
        // Additional assertions for index method
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertTrue(index >= 0);
        assertTrue(hash.contains(1.0f));
        // Additional assertions for insertKey method
    }

    @Test
    public void testWriteExternal() {
        // Additional assertions for writeExternal method
    }

    @Test
    public void testReadExternal() {
        // Additional assertions for readExternal method
    }

}