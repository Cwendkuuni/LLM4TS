import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_LessThan_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 10);
        
        Object result = operation.computeValue(context);
        
        assert(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_GreaterThan_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 5);
        
        Object result = operation.computeValue(context);
        
        assert(result.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Equal_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);
        
        Object result = operation.computeValue(context);
        
        assert(result.equals(Boolean.TRUE));
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqualSymbol() {
        assert(operation.getSymbol().equals(">="));
    }
}