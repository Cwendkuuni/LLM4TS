
import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {
  
    @Test
    public void testReflectionCompare() {
        // Test case for reflectionCompare method with two equal objects
        Object lhs = new Object();
        Object rhs = lhs;
        int result = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, result);
    }
    
    @Test
    public void testAppendSuper() {
        // Test case for appendSuper method with a positive value
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(10);
        int result = builder.toComparison();
        assertEquals(10, result);
    }
    
    @Test
    public void testAppend() {
        // Test case for append method with two strings
        CompareToBuilder builder = new CompareToBuilder();
        builder.append("hello", "world");
        int result = builder.toComparison();
        assertEquals(-1, result);
    }
    
    @Test
    public void testAppendArrays() {
        // Test case for append method with two arrays
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 4});
        int result = builder.toComparison();
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        // Test case for NullPointerException when comparing null objects
        CompareToBuilder.builder.append(null, "value");
    }
  
    @Test(expected = ClassCastException.class)
    public void testClassCastException() {
        // Test case for ClassCastException when comparing objects of different classes
        Object lhs = new Integer(10);
        Object rhs = new String("10");
        CompareToBuilder.reflectionCompare(lhs, rhs);
    }
}

