import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testNotEqualWithBooleanValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(Boolean.TRUE), new ConstantExpression(Boolean.FALSE));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithSameIntegerValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(5), new ConstantExpression(5));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithDifferentIntegerValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(5), new ConstantExpression(10));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithSameStringValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("hello"), new ConstantExpression("hello"));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithDifferentStringValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("hello"), new ConstantExpression("world"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithNullValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(null), new ConstantExpression("world"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithSameNullValues() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(null), new ConstantExpression(null));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithDifferentStringLengths() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression("hello"), new ConstantExpression("world"));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqualWithInvalidArguments() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new ConstantExpression(5));
        CoreOperationNotEqual coreOperationNotEqual2 = new CoreOperationNotEqual(new ConstantExpression(""), new ConstantExpression(5));
        CoreOperationNotEqual coreOperationNotEqual3 = new CoreOperationNotEqual(new ConstantExpression(5), null);
        CoreOperationNotEqual coreOperationNotEqual4 = new CoreOperationNotEqual(new ConstantExpression(5), new ConstantExpression(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEqualWithMismatchedTypes() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(5), new ConstantExpression("hello"));
    }

    @Test
    public void testNotEqualWithSameObjects() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(new Object()), new ConstantExpression(new Object()));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithIdenticalObjects() {
        String str1 = new String("hello");
        String str2 = str1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(str1), new ConstantExpression(str2));
        assertFalse(coreOperationNotEqual.computeValue(new EvalContext()));
    }

    @Test
    public void testNotEqualWithDifferentObjects() {
        String str1 = new String("hello");
        String str2 = new String("world");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new ConstantExpression(str1), new ConstantExpression(str2));
        assertTrue(coreOperationNotEqual.computeValue(new EvalContext()));
    }
}