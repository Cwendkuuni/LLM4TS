
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test with arg1 > arg2
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        arg1.computeValue(context);
        arg2.computeValue(context);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test with arg1 <= arg2
        Expression arg3 = new Expression();
        Expression arg4 = new Expression();
        CoreOperationGreaterThan operation2 = new CoreOperationGreaterThan(arg3, arg4);
        arg3.computeValue(context);
        arg4.computeValue(context);
        assertEquals(Boolean.FALSE, operation2.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operation.getSymbol());
    }
}
