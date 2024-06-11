import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueEqualValues() {
        Expression arg1 = new Constant(2);
        Expression arg2 = new Constant(2);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        assertEquals(">", operation.getSymbol());
    }

    // Edge cases and exception handling scenarios can be tested here

}