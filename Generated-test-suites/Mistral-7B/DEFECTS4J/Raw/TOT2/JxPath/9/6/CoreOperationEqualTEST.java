import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueDifferentTypesIntAndLong() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1L);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueDifferentTypesDoubleAndInt() {
        Expression arg1 = new LiteralExpression(1.0);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueStringAndString() {
        Expression arg1 = new LiteralExpression("hello");
        Expression arg2 = new LiteralExpression("hello");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueBooleanAndBoolean() {
        Expression arg1 = new LiteralExpression(true);
        Expression arg2 = new LiteralExpression(true);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueNullArg1() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        coreOperationEqual.computeValue(context);
    }

    @Test(expected = ClassCastException.class)
    public void testComputeValueNullArg2() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = null;
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        coreOperationEqual.computeValue(context);
    }

    @Test
    public void testComputeValueSameNameVariableAndArg() {
        Expression arg1 = new VariableExpression("variable");
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext("variable", 1);

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueSameNameVariableAndResult() {
        Expression arg1 = new VariableExpression("variable");
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        context.setVariable("variable", Boolean.TRUE);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        Object previousResult = result;
        result = coreOperationEqual.computeValue(context);
        assertEquals(previousResult, result);
    }

    @Test
    public void testComputeValueVariableNull() {
        Expression arg1 = new VariableExpression("variable");
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        context.setVariable("variable", null);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueEmptyVariables() {
        Expression arg1 = new LiteralExpression(1);
        Expression arg2 = new LiteralExpression(1);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        context.setVariables(new EvalContext.Variables());
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
}