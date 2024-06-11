
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();

        // Test with equal arguments
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(10);
        arg2.setValue(10);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with not equal arguments
        arg1.setValue(10);
        arg2.setValue(20);
        result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
