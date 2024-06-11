import static org.junit.Assert.*;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        // Testing reflectionCompare method with different parameters
        assertEquals(0, CompareToBuilder.reflectionCompare("test", "test"));
        assertEquals(1, CompareToBuilder.reflectionCompare("abc", "def"));
        assertEquals(-1, CompareToBuilder.reflectionCompare("xyz", "abc"));
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(10);
        assertEquals(10, builder.toComparison());
    }

    // More test cases can be added for other methods in the CompareToBuilder class

}