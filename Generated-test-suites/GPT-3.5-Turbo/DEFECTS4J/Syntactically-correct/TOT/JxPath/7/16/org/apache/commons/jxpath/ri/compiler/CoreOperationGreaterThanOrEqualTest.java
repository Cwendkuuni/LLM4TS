import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setup() {
        Expression arg1 = new ExpressionImpl();
        Expression arg2 = new ExpressionImpl();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_ValidEqualValues_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl(Double.valueOf(5));
        Expression arg2 = new ExpressionImpl(Double.valueOf(5));
        operation.setArguments(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_ValidGreaterValue_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl(Double.valueOf(7));
        Expression arg2 = new ExpressionImpl(Double.valueOf(5));
        operation.setArguments(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_ValidLesserValue_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl(Double.valueOf(3));
        Expression arg2 = new ExpressionImpl(Double.valueOf(5));
        operation.setArguments(arg1, arg2);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_ValidEqualStrings_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl("hello");
        Expression arg2 = new ExpressionImpl("hello");
        operation.setArguments(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_ValidGreaterStrings_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl("world");
        Expression arg2 = new ExpressionImpl("hello");
        operation.setArguments(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_ValidLesserStrings_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl("hello");
        Expression arg2 = new ExpressionImpl("world");
        operation.setArguments(arg1, arg2);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArgument_ThrowsNullPointerException() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl(null);
        Expression arg2 = new ExpressionImpl(Double.valueOf(5));
        operation.setArguments(arg1, arg2);
        operation.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_InvalidArgumentType_ThrowsIllegalArgumentException() {
        EvalContext context = new EvalContext();
        Expression arg1 = new ExpressionImpl("hello");
        Expression arg2 = new ExpressionImpl(Double.valueOf(5));
        operation.setArguments(arg1, arg2);
        operation.computeValue(context);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSymbol_NotSupportedException() {
        operation.getSymbol();
    }
}