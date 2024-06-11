import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.parser.jxpath.functions.FunctionCall;
import org.junit.Test;
import org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_whenBothArgumentsAreSameBooleanFalse_thenResultShouldBeBooleanTrue() {
        Expression arg1 = new FunctionCall("constant", Boolean.FALSE);
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgumentsAreSameBooleanTrue_thenResultShouldBeBooleanFalse() {
        Expression arg1 = new FunctionCall("constant", Boolean.TRUE);
        Expression arg2 = arg1;
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsBooleanFalseAndSecondArgumentIsBooleanTrue_thenResultShouldBeBooleanTrue() {
        Expression arg1 = new FunctionCall("constant", Boolean.FALSE);
        Expression arg2 = new FunctionCall("constant", Boolean.TRUE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsBooleanTrueAndSecondArgumentIsBooleanFalse_thenResultShouldBeBooleanFalse() {
        Expression arg1 = new FunctionCall("constant", Boolean.TRUE);
        Expression arg2 = new FunctionCall("constant", Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenOneArgumentIsNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new FunctionCall("constant", (Object) null);
        Expression arg2 = new FunctionCall("constant", Boolean.FALSE);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenBothArgumentsAreNull_thenThrowIllegalArgumentException() {
        Expression arg1 = new FunctionCall("constant", (Object) null);
        Expression arg2 = new FunctionCall("constant", (Object) null);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        coreOperationNotEqual.computeValue(new EvalContext());
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsNumberAndSecondArgumentIsNumberWithSameValue_thenResultShouldBeBooleanFalse() {
        Expression arg1 = new FunctionCall("constant", 1);
        Expression arg2 = new FunctionCall("constant", 1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsNumberAndSecondArgumentIsNumberWithDifferentValue_thenResultShouldBeBooleanTrue() {
        Expression arg1 = new FunctionCall("constant", 1);
        Expression arg2 = new FunctionCall("constant", 2);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsStringAndSecondArgumentIsStringWithSameValue_thenResultShouldBeBooleanFalse() {
        Expression arg1 = new FunctionCall("constant", "test");
        Expression arg2 = new FunctionCall("constant", "test");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsStringAndSecondArgumentIsStringWithDifferentValue_thenResultShouldBeBooleanTrue() {
        Expression arg1 = new FunctionCall("constant", "test1");
        Expression arg2 = new FunctionCall("constant", "test2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsFunctionAndSecondArgumentIsNumber_thenResultShouldBeBooleanBasedOnFunctionReturnValueAndNumber() {
        Expression arg1 = new FunctionCall("function1", new FunctionCall("constant", 1));
        Expression arg2 = new FunctionCall("constant", 1);
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        // Replace with assertions depending on the implementation of function1()
        // For example, if function1() always returns Boolean.TRUE:
        // assertTrue(result instanceof Boolean);
        // Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_whenFirstArgumentIsNumberAndSecondArgumentIsFunction_thenResultShouldBeBooleanBasedOnNumberAndFunctionReturnValue() {
        Expression arg1 = new FunctionCall("constant", 1);
        Expression arg2 = new FunctionCall("function1", new FunctionCall("constant", 1));
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        Object result = coreOperationNotEqual.computeValue(new EvalContext());
        // Replace with assertions depending on the implementation of function1()
        // For example, if function1() always returns Boolean.TRUE:
        // assertTrue(result instanceof Boolean);
        // Assert.assertEquals(Boolean.FALSE, result);
    }
}