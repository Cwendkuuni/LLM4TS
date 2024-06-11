import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: arg1 is less than arg2
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setNode(new Node());
        context.setValue(5.0);
        double arg1 = (double) operation.args[0].computeValue(context);
        context.setValue(10.0);
        double arg2 = (double) operation.args[1].computeValue(context);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Edge case: arg1 is equal to arg2
        CoreOperationLessThanOrEqual operation2 = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        context.setValue(5.0);
        double arg1 = (double) operation2.args[0].computeValue(context);
        context.setValue(5.0);
        double arg2 = (double) operation2.args[1].computeValue(context);
        Object result2 = operation2.computeValue(context);
        assertEquals(Boolean.TRUE, result2);

        // Error scenario: when arg1 is not numeric
        CoreOperationLessThanOrEqual operation3 = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        context.setValue("not numeric");
        double arg1 = (double) operation3.args[0].computeValue(context);
        context.setValue(10.0);
        double arg2 = (double) operation3.args[1].computeValue(context);
        Object result3 = operation3.computeValue(context);
        assertEquals(Boolean.FALSE, result3);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        assertEquals("<=", symbol);
    }

}