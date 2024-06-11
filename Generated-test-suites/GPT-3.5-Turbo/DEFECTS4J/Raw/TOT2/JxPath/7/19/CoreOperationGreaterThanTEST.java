import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("5"), new Expression("3"));
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("10"), new Expression("5"));

        assertEquals(">", operation.getSymbol());
    }

    @Test
    public void testComputeValueWithDecimals() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("3.5"), new Expression("3.2"));
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueEqualValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("5"), new Expression("5"));
        EvalContext context = new EvalContext();

        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueNegativeValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("-10"), new Expression("-15"));
        EvalContext context = new EvalContext();

        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    // Add more test cases for edge cases and exception handling scenarios as needed
    
}