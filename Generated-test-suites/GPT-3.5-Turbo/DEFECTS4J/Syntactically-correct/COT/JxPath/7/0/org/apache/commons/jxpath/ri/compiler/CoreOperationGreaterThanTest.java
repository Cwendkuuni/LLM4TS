
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">", symbol);
    }
}
