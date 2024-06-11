import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }

    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new Literal(5);
        Expression arg2 = new Literal(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }

}