import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertNotNull(result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.appendSuper(5);
        assertNotNull(result);
    }

    @Test
    public void testAppendObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append("a", "b");
        assertNotNull(result);
    }

    @Test
    public void testAppendArrays() {
        CompareToBuilder builder = new CompareToBuilder();
        CompareToBuilder result = builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        assertNotNull(result);
    }

    @Test
    public void testToComparison() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(5, 10);
        int result = builder.toComparison();
        assertNotNull(result);
    }

}