import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new CoreConstant(5), new CoreConstant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new CoreConstant(5), new CoreConstant(3));
        assertEquals(">", operation.getSymbol());
    }

    @Test
    public void testComputeValueWithNegativeNumbers() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new CoreConstant(-5), new CoreConstant(-3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithEqualNumbers() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(
            new CoreConstant(5), new CoreConstant(5));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
  
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullArguments() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }

}