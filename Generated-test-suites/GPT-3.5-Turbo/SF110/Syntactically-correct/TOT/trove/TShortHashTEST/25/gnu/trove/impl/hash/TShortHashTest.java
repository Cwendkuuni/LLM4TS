import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {
    
    private TShortHash tShortHash;
    
    @Before
    public void setUp() {
        tShortHash = new TShortHash(10, 0.8f, (short)-1);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(-1, tShortHash.getNoEntryValue());
    }
    
    @Test
    public void testContains() {
        assertFalse(tShortHash.contains((short)5));
        
        tShortHash.insertKey((short)5);
        assertTrue(tShortHash.contains((short)5));
    }
    
    @Test
    public void testForEach() {
        IntCounter counter = new IntCounter();
        assertTrue(tShortHash.forEach(counter));
        assertEquals(0, counter.getCount());
        
        tShortHash.insertKey((short)5);
        assertTrue(tShortHash.forEach(counter));
        assertEquals(1, counter.getCount());
    }
    
    @Test
    public void testRemoveAt() {
        tShortHash.insertKey((short)5);
        assertTrue(tShortHash.contains((short)5));
        
        tShortHash.removeAt(0);
        assertFalse(tShortHash.contains((short)5));
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, tShortHash.index((short)5));
        
        tShortHash.insertKey((short)5);
        assertEquals(0, tShortHash.index((short)5));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tShortHash.insertKey((short)5));
        assertTrue(tShortHash.contains((short)5));
        
        assertEquals(-1, tShortHash.insertKey((short)5));
    }
    
    private class IntCounter implements TShortProcedure {
        private int count;
        
        public int getCount() {
            return count;
        }
        
        @Override
        public boolean execute(short value) {
            count++;
            return true;
        }
    }
}