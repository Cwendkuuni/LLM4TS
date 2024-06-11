
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
    
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
        hash.insertKey(3.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
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
        hash.removeAt(1);
        
        assertFalse(hash.contains(2.0f));
    }
    
    // More test cases for other methods...

}
