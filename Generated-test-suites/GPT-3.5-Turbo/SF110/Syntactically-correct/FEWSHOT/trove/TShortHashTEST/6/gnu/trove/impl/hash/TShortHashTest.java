import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        assertFalse(tShortHash.contains((short) 10));
        
        tShortHash.insertKey((short) 10);
        assertTrue(tShortHash.contains((short) 10));
        
        assertFalse(tShortHash.contains((short) 20));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        tShortHash.insertKey((short) 30);

        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                System.out.println(value);
                return true;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);

        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short) 10));
        assertTrue(tShortHash.contains((short) 20));
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short) 10);
        tShortHash.insertKey((short) 20);
        
        assertEquals(0, tShortHash.index((short) 10));
        assertEquals(1, tShortHash.index((short) 20));
        assertEquals(-1, tShortHash.index((short) 30));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        
        assertEquals(0, tShortHash.insertKey((short) 10));
        assertTrue(tShortHash.contains((short) 10));
        
        assertEquals(1, tShortHash.insertKey((short) 20));
        assertTrue(tShortHash.contains((short) 20));
    }    
}