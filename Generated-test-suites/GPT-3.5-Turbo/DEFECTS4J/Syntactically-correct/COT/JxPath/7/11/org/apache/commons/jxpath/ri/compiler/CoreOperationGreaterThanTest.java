
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(expression1, expression2);
        
        // Basic test case
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.class, result.getClass());
        
        // Edge case: arg1 is greater than arg2
        context.setNodeSet(arg1);
        context.setNodeSet(arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Edge case: arg1 is equal to arg2
        context.setNodeSet(arg1);
        context.setNodeSet(arg1); // arg2 equals arg1
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        // Edge case: arg1 is less than arg2
        context.setNodeSet(arg2);
        context.setNodeSet(arg1);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(expression1, expression2);
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
}
