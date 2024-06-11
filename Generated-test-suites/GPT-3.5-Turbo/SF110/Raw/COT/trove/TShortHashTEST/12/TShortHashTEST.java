import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash tShortHash = new TShortHash();
        short expectedValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short actualValue = tShortHash.getNoEntryValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        short expectedValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short actualValue = tShortHash.getNoEntryValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        short expectedValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short actualValue = tShortHash.getNoEntryValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
        short expectedValue = no_entry_value;
        short actualValue = tShortHash.getNoEntryValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash();
        int expectedValue = initialCapacity;
        int actualValue = tShortHash.setUp(initialCapacity);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        boolean expectedValue = true;
        tShortHash.insertKey((short) 10);
        boolean actualValue = tShortHash.contains((short) 10);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;
            
            public boolean execute(short value) {
                sum += value;
                return true;
            }
        };
        
        boolean expectedValue = true;
        boolean actualValue = tShortHash.forEach(procedure);
        assertEquals(expectedValue, actualValue);
        int expectedSum = 30;
        int actualSum = procedure.sum;
        assertEquals(expectedSum, actualSum);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        int indexToRemove = 0;
        tShortHash.removeAt(indexToRemove);
        boolean expectedValue = false;
        boolean actualValue = tShortHash.contains((short) 10);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        int expectedValue = 0;
        int actualValue = tShortHash.index((short) 10);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        int index = 0;
        int hash = 10;
        byte state = 1;
        int expectedValue = 0;
        int actualValue = tShortHash.indexRehashed((short) 10, index, hash, state);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        int expectedValue = 0;
        int actualValue = tShortHash.insertKey((short) 10);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash();
        int index = 0;
        int hash = 10;
        byte state = 1;
        int expectedValue = 0;
        int actualValue = tShortHash.insertKeyRehash((short) 10, index, hash, state);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKeyAt(0, (short) 10);
        boolean expectedValue = true;
        boolean actualValue = tShortHash.contains((short) 10);
        assertEquals(expectedValue, actualValue);
    }
}