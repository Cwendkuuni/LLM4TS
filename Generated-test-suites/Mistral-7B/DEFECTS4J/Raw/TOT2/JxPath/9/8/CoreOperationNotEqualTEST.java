import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.ArrayExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationNotEqual;
import org.apache.commons.jxpath.ri.compiler.LiteralExpression;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testCoreOperationNotEqualConstructor() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualComputeValue() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualSymbol() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualComputeValue_Boolean() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualComputeValue_String() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualNullArg() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualNullValues() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualArrayDifferentLengths() {
        // test case here
    }

    @Test(expected = NullPointerException.class)
    public void testCoreOperationNotEqualNullArg() {
        // test case here
    }

    @Test
    public void testCoreOperationNotEqualArraySameLengthsDifferentValues() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        boolean result = coreOperationNotEqual.computeValue(new EvalContext()).booleanValue();
        assertFalse(result);

        arg1 = new LiteralExpression(2);
        coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        result = coreOperationNotEqual.computeValue(new EvalContext()).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testCoreOperationNotEqualArraySameLengthsSameValues() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        boolean result = coreOperationNotEqual.computeValue(new EvalContext()).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testCoreOperationNotEqualArraySameLengthsSameObjects() {
        Expression arg1 = new LiteralExpression(new Object());
        Expression arg2 = new LiteralExpression(new Object());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        boolean result = coreOperationNotEqual.computeValue(new EvalContext()).booleanValue();
        assertTrue(result);
    }

    @Test
    public void testCoreOperationNotEqualArraySameLengthsSameObjectsEquals() {
        Expression arg1 = new LiteralExpression(new Object());
        Expression arg2 = new LiteralExpression(new Object());
        arg1 = new LiteralExpression(arg1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        boolean result = coreOperationNotEqual.computeValue(new EvalContext()).booleanValue();
        assertFalse(result);
    }

    @Test
    public void testCoreOperationNotEqualArraySameLengthsSameObjectsHashCode() {
        Expression arg1 = new LiteralExpression(new Object());
        Expression arg2 = new LiteralExpression(new Object());
        arg1 = new LiteralExpression(arg1.hashCode());
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        boolean result = coreOperationNotEqual.computeValue(new EvalContext()).booleanValue();
        assertFalse(result);
    }
}