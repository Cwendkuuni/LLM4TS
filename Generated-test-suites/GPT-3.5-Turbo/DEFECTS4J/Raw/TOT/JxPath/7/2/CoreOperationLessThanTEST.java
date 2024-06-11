
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_TypicalCase() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        // setting up the context and expressions for typical case
        double arg1 = 5.0;
        double arg2 = 10.0;
        context.setVariableValue("arg1", arg1);
        context.setVariableValue("arg2", arg2);
        expression.addArgument(new Expression(arg1));
        expression.addArgument(new Expression(arg2));

        Object result = expression.computeValue(context);

        // the result should be true since arg1 (5.0) is less than arg2 (10.0)
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_EdgeCase() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        // setting up the context and expressions for edge case
        double arg1 = -999999999.9999999;
        double arg2 = 999999999999.9999999;
        context.setVariableValue("arg1", arg1);
        context.setVariableValue("arg2", arg2);
        expression.addArgument(new Expression(arg1));
        expression.addArgument(new Expression(arg2));

        Object result = expression.computeValue(context);

        // the result should be true since arg1 (-999999999.9999999) is less than arg2 (999999999999.9999999)
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_ErrorCase() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        // setting up the context and expressions for error case
        double arg1 = Double.NaN;
        double arg2 = 10.0;
        context.setVariableValue("arg1", arg1);
        context.setVariableValue("arg2", arg2);
        expression.addArgument(new Expression(arg1));
        expression.addArgument(new Expression(arg2));

        Object result = expression.computeValue(context);

        // the result should be false since arg1 (NaN) is not less than arg2 (10.0)
        assertEquals(Boolean.FALSE, result);
    }
}
