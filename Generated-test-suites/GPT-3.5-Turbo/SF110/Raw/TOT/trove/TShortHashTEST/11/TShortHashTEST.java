import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash hash;
    
    @Before
    public void setUp() {
        hash = new TShortHash();
    }
    
    @Test
    public void testConstructor1() {
        assertEquals("Default constructor should initialize _set array with default value", Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
    }
    
    @Test
    public void testConstructor2() {
        hash = new TShortHash(10);
        assertEquals("Constructor with initialCapacity should initialize _set array with default value", Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
        assertEquals("Constructor with initialCapacity should set the specified initial capacity", 10, hash._states.length);
    }
    
    @Test
    public void testConstructor3() {
        hash = new TShortHash(10, 0.5f);
        assertEquals("Constructor with initialCapacity and loadFactor should initialize _set array with default value", Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
        assertEquals("Constructor with initialCapacity and loadFactor should set the specified initial capacity", 10, hash._states.length);
        assertEquals("Constructor with initialCapacity and loadFactor should set the specified load factor", 0.5f, hash._loadFactor, 0.001f);
    }
    
    @Test
    public void testConstructor4() {
        hash = new TShortHash(10, 0.5f, (short) -1);
        assertEquals("Constructor with initialCapacity, loadFactor, and no_entry_value should initialize _set array with no_entry_value", -1, hash._set[0]);
        assertEquals("Constructor with initialCapacity, loadFactor, and no_entry_value should set the specified initial capacity", 10, hash._states.length);
        assertEquals("Constructor with initialCapacity, loadFactor, and no_entry_value should set the specified load factor", 0.5f, hash._loadFactor, 0.001f);
        assertEquals("Constructor with initialCapacity, loadFactor, and no_entry_value should set the specified no_entry_value", -1, hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        hash = new TShortHash(10);
        hash.setUp(20);
        assertEquals("setUp should update the length of _set array", 20, hash._set.length);
    }
    
    @Test
    public void testContains() {
        hash.insertKey((short)5);
        assertTrue("contains should return true when the specified value is in the set", hash.contains((short)5));
        assertFalse("contains should return false when the specified value is not in the set", hash.contains((short)10));
    }
    
    @Test
    public void testForEach() {
        hash.insertKey((short)1);
        hash.insertKey((short)2);
        hash.insertKey((short)3);
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;
            public boolean execute(short value) {
                sum += value;
                return true;
            }
        };
        assertTrue("forEach should return true when the procedure is executed for every value in the set", hash.forEach(procedure));
        assertEquals("forEach should execute the procedure for every value in the set", 6, procedure.sum);
    }
    
    @Test
    public void testRemoveAt() {
        hash.insertKey((short)5);
        hash.removeAt(0);
        assertEquals("removeAt should remove the value at the specified index in the _set array", Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
        assertEquals("removeAt should update the state at the specified index in the _states array", (byte) 0, hash._states[0]);
    }
    
    @Test
    public void testIndex() {
        hash.insertKey((short)5);
        assertEquals("index should return the index of the specified value in the _set array", 0, hash.index((short)5));
        assertEquals("index should return -1 when the specified value is not in the _set array", -1, hash.index((short)10));
    }
    
    @Test
    public void testInsertKey() {
        int index = hash.insertKey((short)5);
        assertEquals("insertKey should return the index where the value was inserted in the _set array when it is not already present in the set", 0, index);
        assertEquals("insertKey should update the value at the specified index in the _set array when it is not already present in the set", (short)5, hash._set[0]);
        
        index = hash.insertKey((short)5);
        assertEquals("insertKey should return the negation of the index where the value was already present in the _set array", -1, index);
        
        // Test inserting a value when the _set array is full
        for (int i = 1; i <= 10; i++) {
            hash.insertKey((short)i);
        }
        index = hash.insertKey((short)11);
        assertEquals("insertKey should throw an IllegalStateException and not insert the value when the _set array is full", -1, index);
    }
}