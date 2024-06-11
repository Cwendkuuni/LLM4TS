import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash(10, 0.5f, (short) 0);
        short noEntryValue = tShortHash.getNoEntryValue();
        assertEquals((short)0, noEntryValue);
    }
    
    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash(10);
        int capacity = tShortHash.setUp(20);
        assertEquals(20, capacity);
    }
    
    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        boolean contains = tShortHash.contains((short)1);
        assertTrue(contains);
    }
    
    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        TShortProcedure procedure = new TShortProcedure() {
            int sum = 0;
            public boolean execute(short value) {
                sum += value;
                return true;
            }
        };
        boolean result = tShortHash.forEach(procedure);
        assertTrue(result);
        assertEquals(3, procedure.sum);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        tShortHash.removeAt(1);
        assertFalse(tShortHash.contains((short)2));
    }
    
    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        int index = tShortHash.index((short)2);
        assertEquals(1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKey((short)1);
        tShortHash.insertKey((short)2);
        int index = tShortHash.indexRehashed((short)2, 1, 1, (byte)1);
        assertEquals(1, index);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash(10);
        int index = tShortHash.insertKey((short)1);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash(10);
        int index = tShortHash.insertKeyRehash((short)2, 0, 1, (byte)1);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash(10);
        tShortHash.insertKeyAt(0, (short)1);
        assertTrue(tShortHash.contains((short)1));
    }
}