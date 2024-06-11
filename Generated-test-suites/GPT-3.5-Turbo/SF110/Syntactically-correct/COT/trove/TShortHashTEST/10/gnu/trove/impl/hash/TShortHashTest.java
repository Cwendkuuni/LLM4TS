
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash();
        
        short noEntryValue = tShortHash.getNoEntryValue();
        
        // Assertion
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        
        // Add a value to the set
        tShortHash.insertKey((short) 10);
        
        boolean contains = tShortHash.contains((short) 10);
        
        // Assertion
        assertTrue(contains);
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        
        // Add values to the set
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        tShortHash.insertKey((short) 30);
        
        final List<Short> list = new ArrayList<>();
        
        boolean success = tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                list.add(value);
                return true;
            }
        });
        
        // Assertions
        assertTrue(success);
        assertEquals(Arrays.asList((short) 10, (short) 20, (short) 30), list);
    }
    
    // Edge Cases
    
    @Test
    public void testEmptySet() {
        TShortHash tShortHash = new TShortHash();
        
        // Assertion
        assertFalse(tShortHash.contains((short) 10));
    }
    
    @Test
    public void testOneElementSet() {
        TShortHash tShortHash = new TShortHash();
        
        // Add a value to the set
        tShortHash.insertKey((short) 10);
        
        // Assertions
        assertTrue(tShortHash.contains((short) 10));
        assertFalse(tShortHash.contains((short) 20));
    }
    
    @Test
    public void testMaximumCapacitySet() {
        TShortHash tShortHash = new TShortHash(4);
        
        // Add values to the set
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        tShortHash.insertKey((short) 30);
        tShortHash.insertKey((short) 40);
        
        // Assertion
        assertTrue(tShortHash.contains((short) 10));
        assertTrue(tShortHash.contains((short) 20));
        assertTrue(tShortHash.contains((short) 30));
        assertTrue(tShortHash.contains((short) 40));
    }
    
    // Exception Handling
    
    @Test(expected = IllegalStateException.class)
    public void testDuplicateValue() {
        TShortHash tShortHash = new TShortHash();
        
        // Add a value to the set
        tShortHash.insertKey((short) 10);
        
        // Add the same value again
        tShortHash.insertKey((short) 10);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemoveNonExistingValue() {
        TShortHash tShortHash = new TShortHash();
        
        // Add a value to the set
        tShortHash.insertKey((short) 10);
        
        // Remove a non-existing value
        tShortHash.removeAt(1);
    }
}
