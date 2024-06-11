import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        EvalContext context = createContext();
        double arg1Value = 5.0;
        double arg2Value = 10.0;
        Expression arg1 = createExpression(arg1Value);
        Expression arg2 = createExpression(arg2Value);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Edge case - Both arguments are equal
        double equalValue = 0.0;
        Expression equalArg1 = createExpression(equalValue);
        Expression equalArg2 = createExpression(equalValue);
        CoreOperationLessThan equalOperation = new CoreOperationLessThan(equalArg1, equalArg2);
        Object equalResult = equalOperation.computeValue(context);
        assertEquals(Boolean.FALSE, equalResult);

        // Error scenario - Arguments are not numbers
        String nonNumericValue1 = "abc";
        String nonNumericValue2 = "def";
        Expression nonNumericArg1 = createExpression(nonNumericValue1);
        Expression nonNumericArg2 = createExpression(nonNumericValue2);
        CoreOperationLessThan nonNumericOperation = new CoreOperationLessThan(nonNumericArg1, nonNumericArg2);
        Object nonNumericResult = nonNumericOperation.computeValue(context);
        assertNull(nonNumericResult);
    }

    @Test
    public void testGetSymbol() {
        double arg1Value = 5.0;
        double arg2Value = 10.0;
        Expression arg1 = createExpression(arg1Value);
        Expression arg2 = createExpression(arg2Value);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }

    private EvalContext createContext() {
        // implement eval context creation
    }

    private Expression createExpression(double value) {
        // implement expression creation
    }

    private Expression createExpression(String value) {
        // implement expression creation for non-numeric values
    }
}