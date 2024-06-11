import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash() {};
        short no_entry_value = tShortHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
    }
    
    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash() {};
        int capacity = tShortHash.setUp(10);
        assertEquals(10, capacity);
        short[] set = tShortHash._set;
        assertNotNull(set);
        assertEquals(10, set.length);
        for (short val : set) {
            assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, val);
        }
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        assertTrue(tShortHash.contains(2));
        assertFalse(tShortHash.contains(6));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        TShortProcedure procedure = new TShortProcedure() {
            @Override
            public boolean execute(short val) {
                if (val == 3) {
                    return false;
                }
                return true;
            }
        };
        assertTrue(tShortHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        tShortHash.removeAt(2);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[2]);
        assertEquals(0, tShortHash._states[2]);
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        int index = tShortHash.index(3);
        assertEquals(2, index);
        
        index = tShortHash.index(6);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        int index = tShortHash.indexRehashed(3, 2, 1234, (byte)1);
        assertEquals(2, index);
        
        index = tShortHash.indexRehashed(6, 2, 1234, (byte)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        tShortHash.consumeFreeSlot = false;
        
        int index = tShortHash.insertKey(2);
        assertEquals(-2, index);
        
        index = tShortHash.insertKey(6);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        tShortHash.consumeFreeSlot = false;
        
        int index = tShortHash.insertKeyRehash(3, 2, 1234, (byte)1);
        assertEquals(2, index);
        
        index = tShortHash.insertKeyRehash(6, 2, 1234, (byte)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash() {};
        tShortHash._set = new short[]{1, 2, 3, 4, 5};
        tShortHash._states = new byte[]{1, 1, 1, 1, 1};
        
        tShortHash.insertKeyAt(2, 6);
        assertEquals(6, tShortHash._set[2]);
        assertEquals(1, tShortHash._states[2]);
    }
}