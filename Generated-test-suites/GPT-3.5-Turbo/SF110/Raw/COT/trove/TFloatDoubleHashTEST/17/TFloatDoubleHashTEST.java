import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                // Do something with value
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    // Add more basic test cases for other methods
}