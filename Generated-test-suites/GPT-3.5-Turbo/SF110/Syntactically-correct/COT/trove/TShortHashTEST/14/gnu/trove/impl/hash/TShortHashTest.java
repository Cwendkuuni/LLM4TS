import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        
        assertTrue(hash.contains((short) 1));
        assertFalse(hash.contains((short) 2));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.insertKey((short) 2);
        
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            
            public boolean execute(short value) {
                count++;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(2, procedure.count);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        hash.removeAt(0);
        
        assertFalse(hash.contains((short) 1));
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 1);
        
        assertEquals(0, hash.index((short) 1));
        assertEquals(-1, hash.index((short) 2));
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash();
        
        hash._set = new short[3];
        hash._states = new byte[3];
        hash._states[0] = 1;
        hash._states[1] = 1;
        hash._states[2] = 1;
        hash._set[0] = 1;
        hash._set[1] = 2;
        hash._set[2] = 3;
        
        assertEquals(0, hash.indexRehashed((short) 1, 0, 0, (byte) 1));
        assertEquals(1, hash.indexRehashed((short) 2, 0, 0, (byte) 1));
        assertEquals(2, hash.indexRehashed((short) 3, 0, 0, (byte) 1));
        assertEquals(-1, hash.indexRehashed((short) 4, 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        
        assertEquals(0, hash.insertKey((short) 1));
        assertEquals(-1, hash.insertKey((short) 1));
        assertEquals(1, hash.insertKey((short) 2));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash();
        
        hash._set = new short[3];
        hash._states = new byte[3];
        hash._states[0] = 1;
        hash._states[1] = 1;
        hash._states[2] = 1;
        hash._set[0] = 1;
        hash._set[1] = 2;
        hash._set[2] = 3;
        
        assertEquals(0, hash.insertKeyRehash((short) 1, 0, 0, (byte) 1));
        assertEquals(1, hash.insertKeyRehash((short) 2, 0, 0, (byte) 1));
        assertEquals(2, hash.insertKeyRehash((short) 3, 0, 0, (byte) 1));
        assertEquals(-1, hash.insertKeyRehash((short) 4, 0, 0, (byte) 1));
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash();
        hash.insertKeyAt(0, (short) 1);
        
        assertEquals(1, hash._set[0]);
        assertEquals(1, hash._states[0]);
    }
}