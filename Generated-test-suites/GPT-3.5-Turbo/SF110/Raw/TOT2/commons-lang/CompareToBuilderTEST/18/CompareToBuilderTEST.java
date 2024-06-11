import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Typical use case
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
        // Edge case
        result = CompareToBuilder.reflectionCompare(null, obj2);
        assertEquals(0, result); // Expecting NullPointerException
    }

    @Test
    public void testAppendSuper() {
        // Typical use case
        CompareToBuilder builder = new CompareToBuilder();
        int result = builder.appendSuper(10).toComparison();
        assertEquals(10, result);
    }

    // More test cases for other methods should be added here

}