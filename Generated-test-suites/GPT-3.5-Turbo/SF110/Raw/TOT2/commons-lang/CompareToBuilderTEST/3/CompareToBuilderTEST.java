import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        int superCompareTo = 5;
        builder.appendSuper(superCompareTo);
        int result = builder.toComparison();
        assertEquals(5, result);
    }

    @Test
    public void testAppendObjectWithComparator() {
        CompareToBuilder builder = new CompareToBuilder();
        Object obj1 = "abc";
        Object obj2 = "def";
        Comparator<String> comparator = (a, b) -> a.compareTo(b);
        builder.append(obj1, obj2, comparator);
        int result = builder.toComparison();
        assertEquals(-3, result);
    }

    // Add more test cases for other methods

}