import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    private CompareToBuilder compareToBuilder;

    @Before
    public void setUp() {
        compareToBuilder = new CompareToBuilder();
    }

    @Test
    public void testReflectionCompare() {
        // TODO: implement test
    }

    @Test
    public void testReflectionCompareWithTransients() {
        // TODO: implement test
    }

    @Test
    public void testReflectionCompareWithExcludeFields() {
        // TODO: implement test
    }

    @Test
    public void testReflectionCompareWithExcludeFieldsArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendSuper() {
        int expected = 1;
        int actual = compareToBuilder.appendSuper(expected).toComparison();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppend() {
        Object lhs = "test";
        Object rhs = "test";
        int expected = 0;
        int actual = compareToBuilder.append(lhs, rhs).toComparison();
        assertEquals(expected, actual);
    }

    @Test
    public void testAppendWithComparator() {
        // TODO: implement test
    }

    @Test
    public void testAppendLong() {
        // TODO: implement test
    }

    @Test
    public void testAppendInt() {
        // TODO: implement test
    }

    @Test
    public void testAppendShort() {
        // TODO: implement test
    }

    @Test
    public void testAppendChar() {
        // TODO: implement test
    }

    @Test
    public void testAppendByte() {
        // TODO: implement test
    }

    @Test
    public void testAppendDouble() {
        // TODO: implement test
    }

    @Test
    public void testAppendFloat() {
        // TODO: implement test
    }

    @Test
    public void testAppendBoolean() {
        // TODO: implement test
    }

    @Test
    public void testAppendObjectArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendObjectArrayWithComparator() {
        // TODO: implement test
    }

    @Test
    public void testAppendLongArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendIntArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendShortArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendCharArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendByteArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendDoubleArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendFloatArray() {
        // TODO: implement test
    }

    @Test
    public void testAppendBooleanArray() {
        // TODO: implement test
    }

    @Test
    public void testToComparison() {
        int expected = 0;
        int actual = compareToBuilder.toComparison();
        assertEquals(expected, actual);
    }

    @Test
    public void testBuild() {
        int expected = 0;
        int actual = compareToBuilder.build();
        assertEquals(expected, actual);
    }
}