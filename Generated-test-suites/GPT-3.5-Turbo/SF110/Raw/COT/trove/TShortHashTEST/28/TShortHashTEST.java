import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short)1);
        
        boolean result = tShortHash.contains((short)1);
        
        assertTrue(result);
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short)1);
        
        boolean result = tShortHash.forEach(val -> {
            System.out.println(val);
            return true;
        });
        
        assertTrue(result);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        tShortHash.insertKey((short)1);
        
        tShortHash.removeAt(0);
        
        assertFalse(tShortHash.contains((short)1));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash();
        
        int index = tShortHash.insertKey((short)1);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash(1, 0.75f, (short)-1);
        tShortHash.insertKey((short)1);
        
        int index = tShortHash.insertKeyRehash((short)1, 0, 1, (byte)1);
        
        assertEquals(0, index);
    }
    
    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash();
        int capacity = 10;
        
        int result = tShortHash.setUp(capacity);
        
        assertEquals(capacity, result);
        assertNotNull(tShortHash._set);
        assertEquals(capacity, tShortHash._set.length);
    }
}