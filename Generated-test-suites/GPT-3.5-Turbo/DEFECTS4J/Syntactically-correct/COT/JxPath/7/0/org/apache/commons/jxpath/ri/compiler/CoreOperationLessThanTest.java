import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);

        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}

