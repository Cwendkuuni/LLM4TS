import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = coreOperationLessThan.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
        
        String symbol = coreOperationLessThan.getSymbol();
        
        assertEquals("<", symbol);
    }
}