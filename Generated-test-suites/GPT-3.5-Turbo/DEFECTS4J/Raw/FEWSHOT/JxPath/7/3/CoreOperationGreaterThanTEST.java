import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
  
    @Test
    public void testComputeValue() {
        // Test 1: l > r -> true
        EvalContext context1 = new EvalContext();
        Expression arg1 = new ConstantLiteral("5");
        Expression arg2 = new ConstantLiteral("3");
        CoreOperationGreaterThan op1 = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(Boolean.TRUE, op1.computeValue(context1));
        
        // Test 2: l > r -> false
        EvalContext context2 = new EvalContext();
        Expression arg3 = new ConstantLiteral("2");
        Expression arg4 = new ConstantLiteral("4");
        CoreOperationGreaterThan op2 = new CoreOperationGreaterThan(arg3, arg4);
        assertEquals(Boolean.FALSE, op2.computeValue(context2));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan op1 = new CoreOperationGreaterThan(null, null);
        assertEquals(">", op1.getSymbol());
        
        CoreOperationGreaterThan op2 = new CoreOperationGreaterThan(null, null);
        assertNotEquals("<", op2.getSymbol());
    }
}