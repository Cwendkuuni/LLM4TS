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
        
        // Edge case - comparing null objects
        try {
            CompareToBuilder.reflectionCompare(null, obj2);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        
        // Edge case - comparing objects of different types
        try {
            CompareToBuilder.reflectionCompare("string", obj2);
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testAppendSuper() {
        // Typical use case
        int result = new CompareToBuilder().appendSuper(10).toComparison();
        assertEquals(10, result);
        
        // Edge case - appending super with non-zero value
        result = new CompareToBuilder().appendSuper(5).appendSuper(8).toComparison();
        assertEquals(5, result);
    }
    
    // Additional test cases for other public methods can be added here

}