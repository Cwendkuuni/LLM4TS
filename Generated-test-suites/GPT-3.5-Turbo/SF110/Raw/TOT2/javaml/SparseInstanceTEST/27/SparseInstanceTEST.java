import org.junit.Test;
import static org.junit.Assert.*;

public class SparseInstanceTest {

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        
        assertEquals(5, instance.noAttributes());
    }
    
    @Test
    public void testSparseInstanceConstructor() {
        SparseInstance instance = new SparseInstance(3);
        
        assertNull(instance.classValue());
        assertEquals(0, instance.size());
    }
    
    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 3.0);
        
        assertEquals(3.0, instance.value(1), 0.001);
    }

    // Additional test cases here for all public methods
    
    @Test
    public void testEdgeCases() {
        // Test edge cases here
    }

    @Test
    public void testExceptions() {
        // Test exception handling scenarios here
    }
}