import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
  
    private TShortHash tShortHash;
  
    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }
  
    @Test
    public void testDefaultConstructor() {
        assertEquals(0, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertFalse(tShortHash.contains((short) 0)); // Edge case: The set is empty
    }
  
    @Test
    public void testConstructorWithCapacity() {
        tShortHash = new TShortHash(10);
        assertEquals(0, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertFalse(tShortHash.contains((short) 0)); // Edge case: The set is empty
    }
  
    @Test
    public void testConstructorWithCapacityAndLoadFactor() {
        tShortHash = new TShortHash(10, 0.7f);
        assertEquals(0, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        assertFalse(tShortHash.contains((short) 0)); // Edge case: The set is empty
    }
  
    @Test
    public void testConstructorWithCapacityAndLoadFactorAndNoEntryValue() {
        tShortHash = new TShortHash(10, 0.7f, (short) -1);
        assertEquals(0, tShortHash.size());
        assertEquals(-1, tShortHash.getNoEntryValue());
        assertFalse(tShortHash.contains((short) 0)); // Edge case: The set is empty
    }
  
    @Test
    public void testSetUp() {
        int expectedCapacity = 20;
        int actualCapacity = tShortHash.setUp(expectedCapacity);
        assertEquals(expectedCapacity, actualCapacity);
        assertEquals(expectedCapacity, tShortHash._set.length);
        assertEquals(expectedCapacity, tShortHash._states.length);
    }
  
    @Test
    public void testContains() {
        tShortHash.insertKey((short) 5);
        assertTrue(tShortHash.contains((short) 5)); // Typical case: Element exists in the set
        assertFalse(tShortHash.contains((short) 0)); // Typical case: Element does not exist in the set
    }
  
    @Test
    public void testForEach() {
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 6);
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value > 5; // Stop the iteration if value <= 5
            }
        };
        assertFalse(tShortHash.forEach(procedure)); // Typical case: Procedure stops the iteration
    }
  
    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 5);
        tShortHash.removeAt(0);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[0]);
        assertEquals(0, tShortHash.size());
    }
  
    @Test
    public void testIndex() {
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 6);
        assertEquals(-1, tShortHash.index((short) 0)); // Typical case: Element does not exist in the set
        assertEquals(0, tShortHash.index((short) 5)); // Typical case: Element exists in the set
        assertEquals(1, tShortHash.index((short) 6)); // Typical case: Element exists in the set
    }
  
    @Test
    public void testIndexRehashed() {
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 6);
        int index = tShortHash.index((short) 6);
        int rehashedIndex = tShortHash.indexRehashed((short) 6, index, 1, (byte) 1);
        assertEquals(index, rehashedIndex); // Typical case: Returns the same index as the original
        index = tShortHash.index((short) 5);
        rehashedIndex = tShortHash.indexRehashed((short) 5, index, 1, (byte) 1);
        assertEquals(index, rehashedIndex); // Typical case: Returns the same index as the original
        index = tShortHash.index((short) 0);
        rehashedIndex = tShortHash.indexRehashed((short) 0, index, 1, (byte) 0);
        assertEquals(-1, rehashedIndex); // Typical case: Returns -1 as element does not exist
    }
  
    @Test
    public void testInsertKey() {
        int index = tShortHash.insertKey((short) 5);
        assertEquals(0, index); // Typical case: Element is inserted at index 0
        assertEquals(1, tShortHash.size());
        assertTrue(tShortHash.contains((short) 5));
    }
  
    @Test
    public void testInsertKeyRehash() {
        tShortHash = new TShortHash(1);
        tShortHash.insertKey((short) 5);
        int index = tShortHash.insertKeyRehash((short) 6, 0, 1, (byte) 1);
        assertEquals(1, index); // Typical case: Element is inserted at index 1 after rehashing
        assertEquals(2, tShortHash.size());
        assertTrue(tShortHash.contains((short) 6));
    }
  
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFullSet() {
        tShortHash = new TShortHash(1);
        tShortHash.insertKey((short) 5);
        tShortHash.insertKeyRehash((short) 6, 0, 1, (byte) 1); // Error scenario: Inserting into a full set
    }
  
    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 5);
        assertEquals(5, tShortHash._set[0]);
        assertEquals(1, tShortHash._states[0]);
    }
}