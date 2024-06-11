import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_normalCase() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals(Boolean.TRUE, operationLessThan.computeValue(context));
    }

    @Test
    public void testComputeValue_edgeCase() {
        Expression arg1 = new Constant(10);
        Expression arg2 = new Constant(5);
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(arg1, arg2);
        assertEquals(Boolean.FALSE, operationLessThan.computeValue(context));
    }

    @Test
    public void testComputeValue_errorCase() {
        Expression arg1 = new Constant("a");
        Expression arg2 = new Constant(5);
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(arg1, arg2);
        // Test that an exception is thrown when trying to compare non-numeric values
        assertThrows(Exception.class, () -> operationLessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(null, null);
        assertEquals("<", operationLessThan.getSymbol());
    }
}