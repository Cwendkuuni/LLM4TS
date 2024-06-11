import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Typical use case: Objects are equal
        Object obj1 = new Object();
        Object obj2 = obj1;
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);

        // Edge case: Objects are null
        obj1 = null;
        obj2 = null;
        result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
        
        // Error scenario: One object is null
        obj1 = new Object();
        obj2 = null;
        try {
            CompareToBuilder.reflectionCompare(obj1, obj2);
        } catch (NullPointerException e) {
            // Exception is thrown
            assertNotNull(e);
        }
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        int result1 = builder.appendSuper(0).toComparison();
        assertEquals(0, result1);

        CompareToBuilder builder2 = new CompareToBuilder();
        int result2 = builder2.appendSuper(1).toComparison();
        assertEquals(1, result2);
        
        CompareToBuilder builder3 = new CompareToBuilder();
        int result3 = builder2.appendSuper(-1).toComparison();
        assertEquals(-1, result3);
    }
    
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        int result1 = builder.append("Hello", "Hello").toComparison();
        assertEquals(0, result1);

        CompareToBuilder builder2 = new CompareToBuilder();
        int result2 = builder2.append("Hello", "World").toComparison();
        assertTrue(result2 < 0);
        
        CompareToBuilder builder3 = new CompareToBuilder();
        int result3 = builder3.append("World", "Hello").toComparison();
        assertTrue(result3 > 0);
    }
}