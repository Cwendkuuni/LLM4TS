package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;
    
    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey());
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.001);
    }
    
    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        };
        
        assertTrue(tFloatDoubleHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.removeAt(0);
        assertEquals(0.0f, tFloatDoubleHash._set[0], 0.001);
    }
    
    @Test
    public void testIndex() {
        int result = tFloatDoubleHash.index(1.0f);
        assertEquals(-1, result);
    }
    
    @Test
    public void testIndexRehashed() {
        int result = tFloatDoubleHash.indexRehashed(1.0f, 0, 5, (byte) 1);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKey() {
        int result = tFloatDoubleHash.insertKey(1.0f);
        assertEquals(0, result);
    }
    
    @Test
    public void testInsertKeyRehash() {
        int result = tFloatDoubleHash.insertKeyRehash(1.0f, 0, 5, (byte) 1);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, tFloatDoubleHash._set[0], 0.001);
        assertEquals((byte) 1, tFloatDoubleHash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        int result = tFloatDoubleHash.XinsertKey(1.0f);
        assertEquals(0, result);
    }
    
    @Test
    public void testWriteExternal() {
        // to be implemented
    }
    
    @Test
    public void testReadExternal() {
        // to be implemented
    }
}