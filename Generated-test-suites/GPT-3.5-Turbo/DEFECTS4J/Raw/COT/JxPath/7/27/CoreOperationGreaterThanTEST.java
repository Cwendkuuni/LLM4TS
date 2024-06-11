
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("2");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("2");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">", symbol);
    }

    @Test
    public void testComputeValueEqual() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant("5");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueNotGreaterThan() {
        Expression arg1 = new Constant("2");
        Expression arg2 = new Constant("5");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueNaN() {
        Expression arg1 = new Constant("NaN");
        Expression arg2 = new Constant("2");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueNull() {
        Expression arg1 = new Constant(null);
        Expression arg2 = new Constant("2");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
}