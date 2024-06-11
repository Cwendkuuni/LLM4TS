import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical use cases
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new ExpressionStub(5),
                new ExpressionStub(10));
        EvalContext context = new EvalContextStub();
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        CoreOperationLessThan operation2 = new CoreOperationLessThan(
                new ExpressionStub(10),
                new ExpressionStub(5));
        assertEquals(Boolean.FALSE, operation2.computeValue(context));

        // Edge cases
        CoreOperationLessThan operation3 = new CoreOperationLessThan(
                new ExpressionStub(Double.MAX_VALUE),
                new ExpressionStub(Double.MIN_VALUE));
        assertEquals(Boolean.FALSE, operation3.computeValue(context));

        CoreOperationLessThan operation4 = new CoreOperationLessThan(
                new ExpressionStub(Double.MIN_VALUE),
                new ExpressionStub(Double.MAX_VALUE));
        assertEquals(Boolean.TRUE, operation4.computeValue(context));

        CoreOperationLessThan operation5 = new CoreOperationLessThan(
                new ExpressionStub(0),
                new ExpressionStub(-1));
        assertEquals(Boolean.FALSE, operation5.computeValue(context));

        // Error scenarios
        CoreOperationLessThan operation6 = new CoreOperationLessThan(
                new ExpressionStub("a"),
                new ExpressionStub(5));
        assertEquals(Boolean.FALSE, operation6.computeValue(context));

        CoreOperationLessThan operation7 = new CoreOperationLessThan(
                new ExpressionStub(5),
                new ExpressionStub("b"));
        assertEquals(Boolean.FALSE, operation7.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Since getSymbol() method returns a constant value, it does not require any test cases
        // In order to satisfy full coverage, return value can be validated as follows:
        
        CoreOperationLessThan operation = new CoreOperationLessThan(
                new ExpressionStub(5),
                new ExpressionStub(10));
        assertEquals("<", operation.getSymbol());
    }
}