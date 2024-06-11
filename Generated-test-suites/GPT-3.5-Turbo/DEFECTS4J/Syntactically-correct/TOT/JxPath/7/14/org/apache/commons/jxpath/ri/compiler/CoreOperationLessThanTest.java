import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case: l < r
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);
        assertTrue(op.computeValue(context));

        // Edge case: l == r
        arg1 = new Expression();
        arg2 = new Expression();
        arg1.setValue(5);
        arg2.setValue(5);
        op = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);
        assertFalse(op.computeValue(context));

        // Error scenario: l > r
        arg1 = new Expression();
        arg2 = new Expression();
        arg1.setValue(10);
        arg2.setValue(5);
        op = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);
        assertFalse(op.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan op = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", op.getSymbol());
    }
}