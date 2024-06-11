import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual expression;

    @Before
    public void setUp() {
        expression = new CoreOperationNotEqual(null, null);
    }

    @Test
    public void computeValue_shouldReturnTrue_whenArgumentsAreNotEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        expression = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setArg(arg1, "value1");
        context.setArg(arg2, "value2");

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenArgumentsAreEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        expression = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setArg(arg1, "value");
        context.setArg(arg2, "value");

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void getSymbol_shouldReturnNotEqual() {
        String symbol = expression.getSymbol();
        assertEquals("!=", symbol);
    }

    @Test
    public void computeValue_shouldReturnFalse_whenBothArgumentsAreNull() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        expression = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setArg(arg1, null);
        context.setArg(arg2, null);

        Object result = expression.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void computeValue_shouldReturnTrue_whenOneArgumentIsNull() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        expression = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setArg(arg1, null);
        context.setArg(arg2, "value");

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void computeValue_shouldReturnTrue_whenOneArgumentIsStringAndOtherIsInteger() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        expression = new CoreOperationNotEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setArg(arg1, "10");
        context.setArg(arg2, 10);

        Object result = expression.computeValue(context);

        assertTrue((Boolean) result);
    }

}