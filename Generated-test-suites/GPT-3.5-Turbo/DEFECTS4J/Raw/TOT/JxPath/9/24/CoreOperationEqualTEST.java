import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: two equal arguments
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_NotEqual() {
        // Typical use case: two not equal arguments
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_EqualNull() {
        // Edge case: one argument is null, other is not null
        Expression arg1 = null;
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(null));
        // Reverse argument order should produce the same result
        operation = new CoreOperationEqual(arg2, arg1);
        assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_NullBoth() {
        // Edge case: both arguments are null
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        assertEquals("=", operation.getSymbol());
    }
}