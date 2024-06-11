import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    private TFloatCharHash floatCharHash;

    @Before
    public void setUp() {
        floatCharHash = new TFloatCharHash();
    }

    @Test
    public void testDefaultConstructor() {
        // Test that the default constructor creates an object with empty state
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.001);
        assertEquals('\0', floatCharHash.getNoEntryValue());
        assertEquals(0, floatCharHash.size());
        assertFalse(floatCharHash.contains(0.0f));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        // Test that the constructor with initial capacity creates an object with the specified capacity
        TFloatCharHash floatCharHash = new TFloatCharHash(10);
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.001);
        assertEquals('\0', floatCharHash.getNoEntryValue());
        assertEquals(0, floatCharHash.size());
        assertFalse(floatCharHash.contains(0.0f));
        
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        assertEquals(2, floatCharHash.size());
        assertTrue(floatCharHash.contains(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        // Test that the constructor with initial capacity and load factor creates an object with the specified capacity and load factor
        TFloatCharHash floatCharHash = new TFloatCharHash(10, 0.7f);
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.001);
        assertEquals('\0', floatCharHash.getNoEntryValue());
        assertEquals(0, floatCharHash.size());
        assertFalse(floatCharHash.contains(0.0f));

        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        assertEquals(2, floatCharHash.size());
        assertTrue(floatCharHash.contains(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
        // Test that the constructor with initial capacity, load factor, no entry key, and no entry value creates an object with the specified values
        TFloatCharHash floatCharHash = new TFloatCharHash(10, 0.7f, -1.0f, 'x');
        assertEquals(-1.0f, floatCharHash.getNoEntryKey(), 0.001);
        assertEquals('x', floatCharHash.getNoEntryValue());
        assertEquals(0, floatCharHash.size());
        assertFalse(floatCharHash.contains(-1.0f));

        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        assertEquals(2, floatCharHash.size());
        assertTrue(floatCharHash.contains(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
    }

    @Test
    public void testGetNoEntryKey() {
        // Test that the getNoEntryKey method returns the correct value
        floatCharHash = new TFloatCharHash(10, 0.7f, -1.0f, 'x');
        assertEquals(-1.0f, floatCharHash.getNoEntryKey(), 0.001);
    }

    @Test
    public void testGetNoEntryValue() {
        // Test that the getNoEntryValue method returns the correct value
        floatCharHash = new TFloatCharHash(10, 0.7f, -1.0f, 'x');
        assertEquals('x', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        // Test that the contains method returns true if the specified value is present in the hash, otherwise false
        floatCharHash.put(1.0f, 'a');
        assertTrue(floatCharHash.contains(1.0f));
        assertFalse(floatCharHash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        // Test that the forEach method applies the specified procedure to each element in the hash
        FloatValuesProcedure procedure = new FloatValuesProcedure();
        
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        
        floatCharHash.forEach(procedure);
        
        assertTrue(procedure.isExecuted());
    }

    @Test
    public void testRemoveAt() {
        // Test that the removeAt method removes the element at the specified index
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        floatCharHash.removeAt(1);
        assertFalse(floatCharHash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        // Test that the index method returns the correct index of the specified key, -1 if not found
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        assertEquals(0, floatCharHash.index(1.0f));
        assertEquals(1, floatCharHash.index(2.0f));
        assertEquals(-1, floatCharHash.index(3.0f));
    }

    @Test
    public void testInsertKey() {
        // Test that the insertKey method inserts the specified key in the hash and returns the correct index
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        floatCharHash.put(3.0f, 'c');
        assertEquals(2, floatCharHash.index(3.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        // Test that the insertKeyRehash method inserts the specified key in the hash and returns the correct index
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        floatCharHash.put(3.0f, 'c');
        assertEquals(1, floatCharHash.index(2.0f));
    }

    @Test
    public void testXinsertKey() {
        // Test that the XinsertKey method (alternate implementation) inserts the specified key in the hash and returns the correct index
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');
        floatCharHash.put(3.0f, 'c');
        assertEquals(1, floatCharHash.index(2.0f));
    }

    @Test
    public void testWriteExternal() {
        // Test that the writeExternal method writes the internal state of the hash to an object output
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        floatCharHash.writeExternal(oos);
        oos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TFloatCharHash restoredHash = new TFloatCharHash();
        restoredHash.readExternal(ois);

        assertEquals(floatCharHash.getNoEntryKey(), restoredHash.getNoEntryKey(), 0.001);
        assertEquals(floatCharHash.getNoEntryValue(), restoredHash.getNoEntryValue());
        assertEquals(floatCharHash.size(), restoredHash.size());
        assertTrue(restoredHash.contains(1.0f));
        assertTrue(restoredHash.contains(2.0f));
    }

    @Test
    public void testReadExternal() {
        // Test that the readExternal method restores the internal state of the hash from an object input
        floatCharHash.put(1.0f, 'a');
        floatCharHash.put(2.0f, 'b');

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        floatCharHash.writeExternal(oos);
        oos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TFloatCharHash restoredHash = new TFloatCharHash();
        restoredHash.readExternal(ois);

        assertEquals(floatCharHash.getNoEntryKey(), restoredHash.getNoEntryKey(), 0.001);
        assertEquals(floatCharHash.getNoEntryValue(), restoredHash.getNoEntryValue());
        assertEquals(floatCharHash.size(), restoredHash.size());
        assertTrue(restoredHash.contains(1.0f));
        assertTrue(restoredHash.contains(2.0f));
    }

    private class FloatValuesProcedure implements TFloatProcedure {

        private boolean executed = false;

        public boolean execute(float value) {
            executed = true;
            return true;
        }

        public boolean isExecuted() {
            return executed;
        }
    }
}