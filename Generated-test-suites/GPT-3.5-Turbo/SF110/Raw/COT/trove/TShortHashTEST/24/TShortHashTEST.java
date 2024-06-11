import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        
        assertTrue(tShortHash.contains((short) 1));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 3);
        
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
        assertEquals(3, procedure.count);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 3);
        
        tShortHash.removeAt(1);
        
        assertFalse(tShortHash.contains((short) 2));
        assertTrue(tShortHash.contains((short) 1));
        assertTrue(tShortHash.contains((short) 3));
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 1);
        
        assertEquals(0, tShortHash.index((short) 1));
        assertEquals(-1, tShortHash.index((short) 2));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        
        int index = tShortHash.insertKey((short) 1);
        
        assertTrue(tShortHash.contains((short) 1));
        assertEquals(0, index);
        
        index = tShortHash.insertKey((short) 1);
        
        assertFalse(tShortHash.contains((short) 1));
        assertEquals(-1, index);
    }
}