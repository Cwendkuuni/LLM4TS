import org.junit.*;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object object1 = new Object();
        Object object2 = new Object();
        int result = CompareToBuilder.reflectionCompare(object1, object2);
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.appendSuper(10);
        assertEquals(builder, result);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append("test", "test");
        assertEquals(builder, result);
    }

    @Test
    public void testAppendPrimitive() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append(10, 20);
        assertEquals(builder, result);
    }

    @Test
    public void testToComparison() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(4, 3);
        int result = builder.toComparison();
        assertEquals(1, result);
    }
    
    // Add more test cases for edge cases and error scenarios here

}