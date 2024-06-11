package gnu.trove.impl.hash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;
    
    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }
    
    @Test
    public void testGetNoEntryKey() {
        float expected = 0.0f;
        float actual = tFloatDoubleHash.getNoEntryKey();
        assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        double expected = 0.0;
        double actual = tFloatDoubleHash.getNoEntryValue();
        assertEquals(expected, actual, 0.0);
    }
    
    @Test
    public void testSetUp() {
        int expected = 10;
        int actual = tFloatDoubleHash.setUp(10);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(10.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            
            @Override
            public boolean execute(float value) {
                System.out.println("Value: " + value);
                return true;
            }
        };
        
        assertTrue(tFloatDoubleHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.removeAt(5);
    }
    
    @Test
    public void testIndex() {
        int expected = -1;
        int actual = tFloatDoubleHash.index(5.0f);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexRehashed() {
        int expected = -1;
        int actual = tFloatDoubleHash.indexRehashed(5.0f, 0, 1, (byte) 0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInsertKey() {
        int expected = 5;
        int actual = tFloatDoubleHash.insertKey(5.0f);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInsertKeyRehash() {
        int expected = -1;
        int actual = tFloatDoubleHash.insertKeyRehash(5.0f, 0, 1, (byte) 0);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash.insertKeyAt(5, 5.0f);
    }
    
    @Test
    public void testXinsertKey() {
        int expected = 5;
        int actual = tFloatDoubleHash.XinsertKey(5.0f);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testWriteExternal() {
        try {
            tFloatDoubleHash.writeExternal(null);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testReadExternal() {
        try {
            tFloatDoubleHash.readExternal(null);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}