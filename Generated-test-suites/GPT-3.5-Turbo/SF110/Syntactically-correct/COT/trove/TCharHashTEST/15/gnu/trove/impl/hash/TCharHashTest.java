import org.junit.*;
import static org.junit.Assert.*;

public class TCharHashTest {
    
    private TCharHash tCharHash;
    
    // Initialize TCharHash before each test
    @Before
    public void setUp() {
        tCharHash = new TCharHash();
    }
    
    // Test for the default constructor
    @Test
    public void testDefaultConstructor() {
        char no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        assertEquals(no_entry_value, tCharHash.getNoEntryValue());
    }
    
    // Test for the constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        tCharHash = new TCharHash(initialCapacity);
        char no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        assertEquals(no_entry_value, tCharHash.getNoEntryValue());
        // Add more assertions if necessary
    }
    
    // Test for the constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        tCharHash = new TCharHash(initialCapacity, loadFactor);
        char no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        assertEquals(no_entry_value, tCharHash.getNoEntryValue());
        // Add more assertions if necessary
    }
    
    // Test for the constructor with initial capacity, load factor, and no_entry_value
    @Test
    public void testConstructorWithAllArguments() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        char no_entry_value = 'x';
        tCharHash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        assertEquals(no_entry_value, tCharHash.getNoEntryValue());
        // Add more assertions if necessary
    }
    
    // Test for the contains method
    @Test
    public void testContains() {
        char[] values = {'a', 'b', 'c'};
        for (char value : values) {
            tCharHash.insertKey(value);
            assertTrue(tCharHash.contains(value));
        }
        assertFalse(tCharHash.contains('d'));
    }
    
    // Test for the forEach method
    @Test
    public void testForEach() {
        char[] values = {'a', 'b', 'c'};
        StringBuilder stringBuilder = new StringBuilder();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }
        };
        for (char value : values) {
            tCharHash.insertKey(value);
        }
        assertTrue(tCharHash.forEach(procedure));
        assertEquals("abc", stringBuilder.toString());
    }
    
    // Test for the removeAt method
    @Test
    public void testRemoveAt() {
        char[] values = {'a', 'b', 'c'};
        for (char value : values) {
            tCharHash.insertKey(value);
        }
        tCharHash.removeAt(1);
        assertFalse(tCharHash.contains('b'));
    }
    
    // Test for the insertKey method
    @Test
    public void testInsertKey() {
        char[] values = {'a', 'b', 'c'};
        for (char value : values) {
            int index = tCharHash.insertKey(value);
            assertEquals(value, tCharHash._set[index]);
        }
    }
    
    // Test for the index method
    @Test
    public void testIndex() {
        char[] values = {'a', 'b', 'c'};
        for (char value : values) {
            tCharHash.insertKey(value);
        }
        assertEquals(0, tCharHash.index('a'));
        assertEquals(1, tCharHash.index('b'));
        assertEquals(2, tCharHash.index('c'));
        assertEquals(-1, tCharHash.index('d'));
    }
    
    // Test for the indexRehashed method
    @Test
    public void testIndexRehashed() {
        char[] values = {'a', 'b', 'c'};
        for (char value : values) {
            tCharHash.insertKey(value);
        }
        assertEquals(2, tCharHash.indexRehashed('a', 0, 0, (byte) 1));
        assertEquals(0, tCharHash.indexRehashed('b', 1, 1, (byte) 1));
        assertEquals(1, tCharHash.indexRehashed('c', 2, 2, (byte) 1));
        assertEquals(-1, tCharHash.indexRehashed('d', 0, 0, (byte) 1));
    }
    
    // Test for the insertKeyRehash method
    @Test
    public void testInsertKeyRehash() {
        char[] values = {'a', 'b', 'c'};
        for (char value : values) {
            tCharHash.insertKey(value);
        }
        int index = tCharHash.insertKeyRehash('d', 0, 0, (byte) 1);
        assertEquals('d', tCharHash._set[index]);
    }
    
    // Test for the insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        char[] values = {'a', 'b', 'c'};
        int[] indexes = {0, 1, 2};
        for (int i = 0; i < values.length; i++) {
            tCharHash.insertKeyAt(indexes[i], values[i]);
            assertEquals(values[i], tCharHash._set[indexes[i]]);
        }
    }
    
}


