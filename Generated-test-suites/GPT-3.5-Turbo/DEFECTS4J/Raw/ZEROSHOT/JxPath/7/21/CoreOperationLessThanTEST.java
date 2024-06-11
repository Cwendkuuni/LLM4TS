import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue1() {
        CoreOperationLessThan operation = new CoreOperationLessThan(expression1, expression2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue2() {
        CoreOperationLessThan operation = new CoreOperationLessThan(expression1, expression2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(expression1, expression2);
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}