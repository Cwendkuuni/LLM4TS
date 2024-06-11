package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    private TFloatDoubleHash hash;
    
    @Before
    public void setUp() {
        hash = new TFloatDoubleHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        float expected = 0.0f;
        float result = hash.getNoEntryKey();
        assertEquals(expected, result, 0.001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        double expected = 0.0;
        double result = hash.getNoEntryValue();
        assertEquals(expected, result, 0.001);
    }
    
    @Test
    public void testContains() {
        float key = 1.0f;
        boolean expected = false;
        boolean result = hash.contains(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        };
        boolean expected = true;
        boolean result = hash.forEach(procedure);
        assertEquals(expected, result);
    }
    
    @Test
    public void testRemoveAt() {
        int index = 0;
        hash.removeAt(index);
        // TODO: Assert expected result
    }
    
    @Test
    public void testIndex() {
        float key = 1.0f;
        int expected = -1;
        int result = hash.index(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testIndexRehashed() {
        float key = 1.0f;
        int index = 0;
        int hash = 1;
        byte state = 1;
        int expected = -1;
        int result = hash.indexRehashed(key, index, hash, state);
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsertKey() {
        float val = 1.0f;
        int expected = 0;
        int result = hash.insertKey(val);
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsertKeyRehash() {
        float val = 1.0f;
        int index = 0;
        int hash = 1;
        byte state = 1;
        int expected = -1;
        int result = hash.insertKeyRehash(val, index, hash, state);
        assertEquals(expected, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        int index = 0;
        float val = 1.0f;
        hash.insertKeyAt(index, val);
        // TODO: Assert expected result
    }
    
    @Test
    public void testXinsertKey() {
        float key = 1.0f;
        int expected = 0;
        int result = hash.XinsertKey(key);
        assertEquals(expected, result);
    }
    
    @Test
    public void testWriteExternal() {
        // TODO: Implement test
    }
    
    @Test
    public void testReadExternal() {
        // TODO: Implement test
    }
}