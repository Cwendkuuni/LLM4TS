import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ConstantExpression(10), new ConstantExpression(5));
        assertEquals(Boolean.FALSE, op.computeValue(context));
        
        CoreOperationLessThanOrEqual op2 = new CoreOperationLessThanOrEqual(new ConstantExpression(5), new ConstantExpression(10));
        assertEquals(Boolean.TRUE, op2.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual op = new CoreOperationLessThanOrEqual(new ConstantExpression(10), new ConstantExpression(5));
        assertEquals("<=", op.getSymbol());
    }
}