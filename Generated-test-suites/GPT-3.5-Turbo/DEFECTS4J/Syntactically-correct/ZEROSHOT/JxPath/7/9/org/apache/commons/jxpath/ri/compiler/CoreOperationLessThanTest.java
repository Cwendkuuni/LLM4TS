import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("arg1"), new Expression("arg2"));

        // Test case 1
        EvalContext context1 = new EvalContext();
        context1.setValue("arg1", 5);
        context1.setValue("arg2", 10);
        Object result1 = operation.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);

        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setValue("arg1", 10);
        context2.setValue("arg2", 5);
        Object result2 = operation.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);

        // Test case 3
        EvalContext context3 = new EvalContext();
        context3.setValue("arg1", 5);
        context3.setValue("arg2", 5);
        Object result3 = operation.computeValue(context3);
        assertEquals(Boolean.FALSE, result3);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("arg1"), new Expression("arg2"));
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}