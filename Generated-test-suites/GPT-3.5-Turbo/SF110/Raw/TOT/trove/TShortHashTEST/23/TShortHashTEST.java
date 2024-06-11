import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {
    
    @Test
    public void testConstructor1() {
        TShortHash tShortHash = new TShortHash();
        
        assertEquals(0, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        TShortHash tShortHash = new TShortHash(10);
        
        assertEquals(0, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        TShortHash tShortHash = new TShortHash(10, 0.75f);
        
        assertEquals(0, tShortHash.size());
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        short noEntryValue = 100;
        TShortHash tShortHash = new TShortHash(10, 0.75f, noEntryValue);
        
        assertEquals(0, tShortHash.size());
        assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash();
        
        int capacity = tShortHash.setUp(10);
        
        assertEquals(10, tShortHash._set.length);
        assertEquals(10, capacity);
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash(10);
        
        assertFalse(tShortHash.contains((short) 5));
        
        tShortHash.insertKey((short) 5);
        
        assertTrue(tShortHash.contains((short) 5));
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash(10);
        
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 3);
        
        final StringBuilder sb = new StringBuilder();
        
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short val) {
                sb.append(val);
                return true;
            }
        };
        
        assertTrue(tShortHash.forEach(procedure));
        assertEquals("23", sb.toString());
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash(10);
        
        tShortHash.insertKey((short) 2);
        tShortHash.insertKey((short) 3);
        
        tShortHash.removeAt(0);
        
        assertFalse(tShortHash.contains((short) 2));
        assertTrue(tShortHash.contains((short) 3));
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash(10);
        
        tShortHash.insertKey((short) 5);
        tShortHash.insertKey((short) 10);
        
        assertEquals(1, tShortHash.index((short) 10));
        assertEquals(-1, tShortHash.index((short) 15));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash(10);
        
        int index1 = tShortHash.insertKey((short) 5);
        int index2 = tShortHash.insertKey((short) 10);
        
        assertEquals(0, index1);
        assertEquals(1, index2);
        
        assertFalse(tShortHash.insertKey((short) 5) >= 0);
    }
}