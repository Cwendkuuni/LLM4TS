import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    TShortHash tShortHash = new TShortHash();
    
    @Test
    public void testGetNoEntryValue() {
        short result = tShortHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, result);
    }
    
    @Test
    public void testSetUp() {
        int result = tShortHash.setUp(10);
        assertEquals(10, result);
    }
    
    @Test
    public void testContains() {
        boolean result = tShortHash.contains(5);
        assertFalse(result);
    }
    
    @Test
    public void testContains_NotFound() {
        boolean result = tShortHash.contains(10);
        assertFalse(result);
    }
    
    @Test
    public void testForEach() {
        TShortProcedure tShortProcedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return value < 5;
            }
        };
        boolean result = tShortHash.forEach(tShortProcedure);
        assertFalse(result);
    }
    
    @Test
    public void testRemoveAt() {
        tShortHash.setUp(10);
        tShortHash.removeAt(5);
        boolean result = tShortHash.contains(5);
        assertFalse(result);
    }
    
    @Test
    public void testIndex() {
        short[] set = new short[]{1, 3, 5, 7, 9};
        assertTrue(indexExists(set, 5));
    }
    
    @Test
    public void testInsertKey() {
        tShortHash.setUp(10);
        int result = tShortHash.insertKey(5);
        assertTrue(result >= 0);
        assertTrue(tShortHash.contains(5));
    }
    
    @Test
    public void testInsertKey_AlreadyExists() {
        tShortHash.setUp(10);
        int result = tShortHash.insertKey(5);
        assertTrue(result >= 0);
        result = tShortHash.insertKey(5);
        assertTrue(result < 0);
    }
    
    private boolean indexExists(short[] array, short val) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == val) {
                return true;
            }
        }
        return false;
    }

}