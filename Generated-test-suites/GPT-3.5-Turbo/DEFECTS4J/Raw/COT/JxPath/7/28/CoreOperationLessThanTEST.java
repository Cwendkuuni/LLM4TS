
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1: arg1 < arg2
        arg1.computeValue(context);
        arg2.computeValue(context);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Test case 2: arg1 >= arg2
        arg1.computeValue(context);
        arg2.computeValue(context);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
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
