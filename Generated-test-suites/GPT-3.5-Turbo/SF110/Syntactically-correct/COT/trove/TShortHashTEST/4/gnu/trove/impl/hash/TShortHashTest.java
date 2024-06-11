import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        TShortHash tShortHash = new TShortHash() {};
        short expected = (short)0;
        assertEquals(expected, tShortHash.getNoEntryValue());
    }
    
    // Test case for constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity) {};
        short expected = (short)0;
        assertEquals(expected, tShortHash.getNoEntryValue());
    }
    
    // Test case for constructor with initial capacity and load factor
    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor) {};
        short expected = (short)0;
        assertEquals(expected, tShortHash.getNoEntryValue());
    }
    
    // Test case for constructor with initial capacity, load factor, and no entry value
    @Test
    public void testConstructorWithAllParameters() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = (short)-1;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, noEntryValue) {};
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }
    
    // Test case for contains method
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash() {};
        assertFalse(tShortHash.contains((short)1));
    }
    
    // Test case for forEach method
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash() {};
        boolean result = tShortHash.forEach(new TShortProcedure() {
            public boolean execute(short value) {
                return value > 0;
            }
        });
        assertTrue(result);
    }
    
    // Test case for removeAt method
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash.removeAt(0);
    }
    
    // Test case for index method
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash() {};
        int index = tShortHash.index((short)1);
        assertEquals(-1, index);
    }
    
    // Test case for indexRehashed method
    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash() {};
        int index = tShortHash.indexRehashed((short)1, 0, 0, (byte)0);
        assertEquals(-1, index);
    }
    
    // Test case for insertKey method
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash() {};
        int index = tShortHash.insertKey((short)1);
        assertEquals(0, index);
    }
    
    // Test case for insertKeyRehash method
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash() {};
        int index = tShortHash.insertKeyRehash((short)1, 0, 0, (byte)0);
        assertEquals(-1, index);
    }
    
    // Test case for insertKeyAt method
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash.insertKeyAt(0, (short)1);
    }
}