import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import org.junit.Test;

public class TFloatDoubleHashTest {
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        hash.insertKey(10.0f);
        hash.insertKey(15.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(30.0f, procedure.sum, 0.001);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        hash.insertKey(10.0f);
        hash.insertKey(15.0f);
        
        assertEquals(3, hash.size());
        hash.removeAt(1);
        assertEquals(2, hash.size());
        assertFalse(hash.contains(10.0f));
    }
    
    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(5.0f);
        hash.insertKey(10.0f);
        hash.insertKey(15.0f);
        
        assertEquals(1, hash.index(10.0f));
    }
    
    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0, hash.insertKey(5.0f));
        assertEquals(-1, hash.insertKey(5.0f));
    }
    
    // Add more test cases to comprehensively test all methods in TFloatDoubleHash class
    
}