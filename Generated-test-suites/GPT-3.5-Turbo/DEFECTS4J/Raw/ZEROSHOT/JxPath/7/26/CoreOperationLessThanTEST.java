import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    // Test computeValue() method
    @Test
    public void testComputeValue() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(5), new Expression(10));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    // Test getSymbol() method
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(5), new Expression(10));
        String symbol = expression.getSymbol();
        assertEquals("<", symbol);
    }

    // Additional test case
    @Test
    public void testComputeValueWithEqualValues() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new Expression(5), new Expression(5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}