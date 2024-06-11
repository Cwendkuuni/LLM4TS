import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals("Reflection compare failed", 0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder newBuilder = builder.appendSuper(5);
        assertEquals("Append super failed", 5, newBuilder.toComparison());
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder newBuilder = builder.append("abc", "def");
        assertEquals("Append failed", -1, newBuilder.toComparison());
    }

    // Add other test cases for remaining methods as deemed necessary

}