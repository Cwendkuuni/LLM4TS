import static org.junit.Assert.*;
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        char expectedNoEntryValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actualNoEntryValue = tCharHash.getNoEntryValue();
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        char expectedNoEntryValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actualNoEntryValue = tCharHash.getNoEntryValue();
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        char expectedNoEntryValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
        char actualNoEntryValue = tCharHash.getNoEntryValue();
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        char no_entry_value = 'N';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, no_entry_value);
        char expectedNoEntryValue = no_entry_value;
        char actualNoEntryValue = tCharHash.getNoEntryValue();
        assertEquals(expectedNoEntryValue, actualNoEntryValue);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash();
        int expectedCapacity = 10;
        int actualCapacity = tCharHash.setUp(initialCapacity);
        assertEquals(expectedCapacity, actualCapacity);
    }
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        boolean expectedContains = false;
        boolean actualContains = tCharHash.contains(val);
        assertEquals(expectedContains, actualContains);
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char val) {
                return true;
            }
        };
        boolean expectedForEach = true;
        boolean actualForEach = tCharHash.forEach(procedure);
        assertEquals(expectedForEach, actualForEach);
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        int index = 0;
        tCharHash.removeAt(index);
        assertEquals(tCharHash.getNoEntryValue(), tCharHash._set[index]);
        assertEquals(0, tCharHash._states[index]);
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        int expectedIndex = -1;
        int actualIndex = tCharHash.index(val);
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        char key = 'A';
        int index = 0;
        int hash = 1;
        byte state = 1;
        int expectedIndex = -1;
        int actualIndex = tCharHash.indexRehashed(key, index, hash, state);
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        int expectedIndex = 0;
        int actualIndex = tCharHash.insertKey(val);
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        char val = 'A';
        int index = 0;
        int hash = 1;
        byte state = 1;
        int expectedIndex = -1;
        int actualIndex = tCharHash.insertKeyRehash(val, index, hash, state);
        assertEquals(expectedIndex, actualIndex);
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        int index = 0;
        char val = 'A';
        tCharHash.insertKeyAt(index, val);
        assertEquals(val, tCharHash._set[index]);
        assertEquals(1, tCharHash._states[index]);
    }
}