import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatDoubleHashTest {
    
    @Test
    public void testTFloatDoubleHashConstructor() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }
    
    @Test
    public void testInsertAndContains() {
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
            boolean result = true;
            public boolean execute(float value) {
                if (value == 1.0f) {
                    result = false;
                }
                return true;
            }
        };
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }
    
    // Additional test cases can be added for other methods
    
}