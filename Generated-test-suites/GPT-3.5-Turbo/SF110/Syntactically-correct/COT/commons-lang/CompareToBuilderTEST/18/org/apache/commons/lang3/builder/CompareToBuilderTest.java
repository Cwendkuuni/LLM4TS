
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    // Test case for reflectionCompare() method
    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);

        assertEquals(0, result);
    }

    // Test case for appendSuper() method
    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(10);

        int result = builder.toComparison();

        assertEquals(10, result);
    }

    // Test case for append() method with Comparable objects
    @Test
    public void testAppendComparable() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append("Hello", "World");

        int result = builder.toComparison();

        assertEquals(-1, result);
    }

    // Test case for append() method with array objects
    @Test
    public void testAppendArray() {
        CompareToBuilder builder = new CompareToBuilder();
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        builder.append(arr1, arr2);

        int result = builder.toComparison();

        assertEquals(-1, result);
    }

    // Test case for toComparison() method
    @Test
    public void testToComparison() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10, 5);

        int result = builder.toComparison();

        assertEquals(1, result);
    }
}

