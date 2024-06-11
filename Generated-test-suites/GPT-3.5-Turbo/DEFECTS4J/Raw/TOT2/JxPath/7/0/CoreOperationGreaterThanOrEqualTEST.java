import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        assertEquals(false, result); // 5 >= 10 should be false
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(10);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}