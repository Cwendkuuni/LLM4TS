import static org.junit.Assert.*;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        TShortHash hash = new TShortHash();
        assertNotNull(hash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testInitialCapacityConstructor() {
        TShortHash hash = new TShortHash(10);
        assertNotNull(hash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        // Test that the _set array is correctly initialized
        assertEquals(10, hash._set.length);
        for (int i = 0; i < hash._set.length; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[i]);
        }
    }
    
    @Test
    public void testInitialCapacityLoadFactorConstructor() {
        TShortHash hash = new TShortHash(10, 0.7f);
        assertNotNull(hash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        // Test that the _set array is correctly initialized
        assertEquals(10, hash._set.length);
        for (int i = 0; i < hash._set.length; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[i]);
        }
    }
    
    @Test
    public void testFullConstructor() {
        short noEntryValue = 5;
        TShortHash hash = new TShortHash(10, 0.7f, noEntryValue);
        assertNotNull(hash);
        assertEquals(noEntryValue, hash.getNoEntryValue());
        // Test that the _set array is correctly initialized
        assertEquals(10, hash._set.length);
        for (int i = 0; i < hash._set.length; i++) {
            assertEquals(noEntryValue, hash._set[i]);
        }
    }
    
    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHash();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        assertEquals(10, hash._set.length);
        for (int i = 0; i < hash._set.length; i++) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[i]);
        }
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        
        // Add some values to the hash
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        
        // Define a procedure that sums up all values in the hash
        TShortProcedure sumProcedure = new TShortProcedure() {
            int sum = 0;
            
            public boolean execute(short value) {
                sum += value;
                return true;
            }
            
            public int getSum() {
                return sum;
            }
        };
        
        // Apply the procedure to the hash
        assertTrue(hash.forEach(sumProcedure));
        
        // Test that the sum is correct
        assertEquals(6, sumProcedure.getSum());
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        
        // Add a value to the hash
        hash.insertKey((short) 1);
        
        // Test that the value is present
        assertTrue(hash.contains((short) 1));
        
        // Remove the value
        hash.removeAt(0);
        
        // Test that the value is no longer present
        assertFalse(hash.contains((short) 1));
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        
        // Add some values to the hash
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        hash.insertKey((short) 3);
        
        // Test that the index is correct for each value
        assertEquals(0, hash.index((short) 1));
        assertEquals(1, hash.index((short) 2));
        assertEquals(2, hash.index((short) 3));
        
        // Test that the index is correct for a value not in the hash
        assertEquals(-1, hash.index((short) 4));
    }
    
    @Test 
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        
        // Insert a value into the hash
        int index = hash.insertKey((short) 1);
        
        // Test that the value is present at the correct index
        assertEquals(0, index);
        assertEquals(1, hash._set[index]);
        
        // Test that inserting the same value returns a negative index
        index = hash.insertKey((short) 1);
        assertTrue(index < 0);
        assertEquals(-(index + 1), hash._set[-(index + 1)]);
        
        // Test that inserting a different value returns a different index
        index = hash.insertKey((short) 2);
        assertTrue(index >= 0);
        assertEquals(2, hash._set[index]);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(3);
        
        // Insert multiple values with the same hash
        int index1 = hash.insertKey((short) 1);  // will be at index 1
        int index2 = hash.insertKey((short) 4);  // will be at index 1 - 1 = 0
        int index3 = hash.insertKey((short) 7);  // will be at index 1 - 1 - 1 = -1
        
        // Test that the values are present at the correct indices
        assertEquals(4, hash._set[index2]);
        assertEquals(7, hash._set[hash._set.length + index3]);
        
        // Test that inserting the same values returns negative indices
        index1 = hash.insertKey((short) 1);
        assertTrue(index1 < 0);
        assertEquals(-(index1 + 1), hash._set[-(index1 + 1)]);
        
        index2 = hash.insertKey((short) 4);
        assertTrue(index2 < 0);
        assertEquals(-(index2 + 1), hash._set[-(index2 + 1)]);
        
        index3 = hash.insertKey((short) 7);
        assertTrue(index3 < 0);
        assertEquals(-(index3 + 1), hash._set[hash._set.length + index3]);
        
        // Test that inserting a different value returns a positive index
        int index4 = hash.insertKey((short) 2);
        assertTrue(index4 >= 0);
        assertEquals(2, hash._set[index4]);
    }
}