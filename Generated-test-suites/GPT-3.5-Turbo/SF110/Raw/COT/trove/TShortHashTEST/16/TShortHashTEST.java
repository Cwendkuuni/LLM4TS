
import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash hash = new TShortHash();
        
        // Check initial capacity
        assertEquals(10, hash.capacity());
        
        // Check load factor
        assertEquals(0.5, hash.loadFactor(), 0.001);
        
        // Check no entry value
        assertEquals(0, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TShortHash hash = new TShortHash(20);
        
        // Check initial capacity
        assertEquals(20, hash.capacity());
        
        // Check load factor
        assertEquals(0.5, hash.loadFactor(), 0.001);
        
        // Check no entry value
        assertEquals(0, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TShortHash hash = new TShortHash(20, 0.75f);
        
        // Check initial capacity
        assertEquals(20, hash.capacity());
        
        // Check load factor
        assertEquals(0.75, hash.loadFactor(), 0.001);
        
        // Check no entry value
        assertEquals(0, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        TShortHash hash = new TShortHash(20, 0.75f, (short) -1);
        
        // Check initial capacity
        assertEquals(20, hash.capacity());
        
        // Check load factor
        assertEquals(0.75, hash.loadFactor(), 0.001);
        
        // Check no entry value
        assertEquals(-1, hash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        
        // Add some values
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        // Check if values are contained
        assertTrue(hash.contains((short) 10));
        assertTrue(hash.contains((short) 20));
        assertTrue(hash.contains((short) 30));
        
        // Check for a value that doesn't exist
        assertFalse(hash.contains((short) 40));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        
        // Add some values
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        // Create a procedure to accumulate the values
        final short[] accum = {0};
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                accum[0] += value;
                return true;
            }
        };
        
        // Execute the procedure
        assertTrue(hash.forEach(procedure));
        
        // Check the accumulated sum
        assertEquals(60, accum[0]);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        
        // Add some values
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        // Remove a value
        hash.removeAt(1);
        
        // Check if the value was removed
        assertFalse(hash.contains((short) 20));
        
        // Check the capacity after removal
        assertEquals(10, hash.capacity());
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        
        // Add some values
        hash.insertKey((short) 10);
        hash.insertKey((short) 20);
        hash.insertKey((short) 30);
        
        // Check the index of a value
        assertEquals(1, hash.index((short) 20));
        
        // Check the index of a non-existing value
        assertEquals(-1, hash.index((short) 40));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        
        // Add a value
        int index1 = hash.insertKey((short) 10);
        
        // Check the index of the added value
        assertEquals(0, index1);
        
        // Add another value that causes rehashing
        int index2 = hash.insertKey((short) 20);
        
        // Check the index of the second added value
        assertEquals(1, index2);
    }
}
