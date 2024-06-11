import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Comparator;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }

    @Test
    public void testReflectionCompareWithNull() {
        Object obj1 = new Object();
        int result = CompareToBuilder.reflectionCompare(obj1, null);
        assertEquals(1, result);
    }

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append("abc", "def");
        int result = builder.toComparison();
        assertTrue(result < 0);
    }

    @Test
    public void testAppendWithNull() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(null, "def");
        int result = builder.toComparison();
        assertTrue(result < 0);
    }

    @Test
    public void testAppendWithArrays() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(new int[]{1, 2, 3}, new int[]{1, 2, 3});
        int result = builder.toComparison();
        assertEquals(0, result);
    }

    @Test
    public void testAppendWithDifferentArrayLength() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(new int[]{1, 2}, new int[]{1, 2, 3});
        int result = builder.toComparison();
        assertTrue(result < 0);
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionCompareWithNullObject() {
        CompareToBuilder.reflectionCompare(null, new Object());
    }

}