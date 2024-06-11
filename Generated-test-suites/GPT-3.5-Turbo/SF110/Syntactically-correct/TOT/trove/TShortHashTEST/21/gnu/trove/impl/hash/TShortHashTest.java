import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {
    
    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash(10, 0.75f, (short) -1);
    }
    
    // Test cases for TShortHash constructor 1
    @Test
    public void testTShortHashConstructor1() {
        assertEquals(10, tShortHash._set.length);
        assertEquals((short) -1, tShortHash.no_entry_value);
    }
    
    // Test cases for TShortHash constructor 2
    @Test
    public void testTShortHashConstructor2() {
        TShortHash tShortHash2 = new TShortHash(20);
        assertEquals(20, tShortHash2._set.length);
        assertEquals((short) -1, tShortHash2.no_entry_value);
    }
    
    // Test cases for TShortHash constructor 3
    @Test
    public void testTShortHashConstructor3() {
        TShortHash tShortHash3 = new TShortHash(30, 0.6f);
        assertEquals(30, tShortHash3._set.length);
        assertEquals((short) -1, tShortHash3.no_entry_value);
    }
    
    // Test cases for TShortHash constructor 4
    @Test
    public void testTShortHashConstructor4() {
        TShortHash tShortHash4 = new TShortHash(40, 0.5f, (short) -10);
        assertEquals(40, tShortHash4._set.length);
        assertEquals((short) -10, tShortHash4.no_entry_value);
    }
    
    // Test case for getNoEntryValue method
    @Test
    public void testGetNoEntryValue() {
        assertEquals( (short) -1, tShortHash.getNoEntryValue() );
    }
    
    // Test case for contains method when value is present
    @Test
    public void testContainsWithValue() {
        tShortHash.insertKey((short) 5);
        assertTrue( tShortHash.contains((short) 5) );
    }
    
    // Test case for contains method when value is not present
    @Test
    public void testContainsWithoutValue() {
        assertFalse( tShortHash.contains((short) 5) );
    }
    
    // Test case for forEach method
    @Test
    public void testForEach() {
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 15);
        
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            public boolean execute(short value) {
                count++;
                return true;
            }
        };
        
        assertTrue( tShortHash.forEach(procedure) );
        assertEquals(3, procedure.count);
    }
    
    // Test case for removeAt method
    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short) 5);
        tShortHash.removeAt(0);
        
        assertFalse( tShortHash.contains((short) 5) );
    }
    
    // Test case for index method when value is present
    @Test
    public void testIndexWithValue() {
        tShortHash.insertKey((short) 5);
        
        assertEquals(0, tShortHash.index((short) 5));
    }
    
    // Test case for index method when value is not present
    @Test
    public void testIndexWithoutValue() {
        assertEquals(-1, tShortHash.index((short) 5));
    }
    
    // Test case for insertKey method when key is inserted in an empty slot
    @Test
    public void testInsertKeyInEmptySlot() {
        int index = tShortHash.insertKey((short) 5);
        
        assertEquals(0, index);
        assertTrue( tShortHash.contains((short) 5) );
    }
    
    // Test case for insertKey method when key is already present
    @Test
    public void testInsertKeyAlreadyPresent() {
        tShortHash.insertKey((short) 5);
        int index = tShortHash.insertKey((short) 5);
        
        assertEquals(-1, index);
    }
    
    // Test case for insertKey method when key is present and removed slot is available
    @Test
    public void testInsertKeyRemovedSlotAvailable() {
        tShortHash.insertKey((short) 5);
        tShortHash.removeAt(0);
        
        int index = tShortHash.insertKey((short) 5);
        
        assertEquals(0, index);
    }
    
    // Test case for insertKey method when key is not present and no empty slots available
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyFullSet() {
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 15);
        tShortHash.insertKey((short) 20);
        tShortHash.insertKey((short) 25);
        tShortHash.insertKey((short) 30);
        tShortHash.insertKey((short) 35);
        tShortHash.insertKey((short) 40);
        tShortHash.insertKey((short) 45);
        tShortHash.insertKey((short) 50);
        
        tShortHash.insertKey((short) 55);
    }
    
}