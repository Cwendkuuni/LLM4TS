
package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        assertFalse(tShortHash.contains((short)10));
        
        tShortHash.insertKey((short)10);
        assertTrue(tShortHash.contains((short)10));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        assertFalse(tShortHash.forEach(element -> element > 10));
        
        tShortHash.insertKey((short)5);
        tShortHash.insertKey((short)10);
        assertTrue(tShortHash.forEach(element -> element > 4));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short)10);
        tShortHash.insertKey((short)20);
        
        assertTrue(tShortHash.contains((short)20));
        tShortHash.removeAt(1);
        assertFalse(tShortHash.contains((short)20));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        assertEquals(0, tShortHash.insertKey((short)10));
        assertEquals(1, tShortHash.insertKey((short)20));
        // Duplicate element
        assertEquals(-2, tShortHash.insertKey((short)10));
    }
}
