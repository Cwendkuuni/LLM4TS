import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {
  
    @Test
    public void testContains() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{};
        assertFalse(obj.contains((short) 5));
        
        obj._set = new short[]{5, 10, 15};
        assertTrue(obj.contains((short) 5));
        assertFalse(obj.contains((short) 20));
    }
    
    @Test
    public void testForEach() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short val) {
                return val >= 5;
            }
        };
        assertTrue(obj.forEach(procedure));
        
        obj._set = new short[]{};
        assertTrue(obj.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        int index = 1;
        obj.removeAt(index);
        assertEquals(0, obj._set[index]);
        assertEquals(-1, obj._states[index]);
    }
    
    @Test
    public void testIndex() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        obj._states = new byte[]{1, 1, 1};
        int valIndex = 1;
        assertEquals(valIndex, obj.index(obj._set[valIndex]));
        
        valIndex = 3;
        assertEquals(-1, obj.index((short) 20));
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        obj._states = new byte[]{1, 1, 1};
        int valIndex = 1;
        int index = 0;
        int hash = 0;
        byte state = 1;
        assertEquals(valIndex, obj.indexRehashed(obj._set[valIndex], index, hash, state));
        
        valIndex = 3;
        assertEquals(-1, obj.indexRehashed((short) 20, index, hash, state));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        obj._states = new byte[]{1, 1, 1};
        int val = 20;
        assertEquals(3, obj.insertKey((short) val));
        
        val = 10;
        assertEquals(-2, obj.insertKey((short) val));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        obj._states = new byte[]{1, 1, 1};
        int val = 20;
        int index = 0;
        int hash = 0;
        byte state = 1;
        assertEquals(3, obj.insertKeyRehash((short) val, index, hash, state));
        
        val = 10;
        assertEquals(-2, obj.insertKeyRehash((short) val, index, hash, state));
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 0, 15};
        obj._states = new byte[]{1, 0, 1};
        int index = 1;
        int val = 10;
        obj.insertKeyAt(index, (short) val);
        assertEquals(val, obj._set[index]);
        assertEquals(1, obj._states[index]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAtException() {
        TShortHash obj = new TShortHash();
        obj._set = new short[]{5, 10, 15};
        obj._states = new byte[]{1, 1, 1};
        int index = 0;
        int val = 20;
        obj.insertKeyAt(index, (short) val);
    }
    
}