import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {
    
    @Test
    public void testReflectionCompare() {
        // Typical use case
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
        
        // Edge case: Null object
        try {
            CompareToBuilder.reflectionCompare(null, obj2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Exception handled
        }
        
        // Edge case: Different classes
        try {
            CompareToBuilder.reflectionCompare(new String(), obj2);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Exception handled
        }
    }
    
    @Test
    public void testAppendSuper() {
        // Typical use case
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(10);
        int result = builder.toComparison();
        assertEquals(10, result);
        
        // Edge case: Multiple appendSuper()
        builder.appendSuper(20);
        result = builder.toComparison();
        assertEquals(10, result);
    }
    
    // Add test cases for other public methods in CompareToBuilder class
    
}