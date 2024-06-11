import org.junit.BeforeClass;
import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    private static CoreOperationGreaterThan coreOperationGreaterThan;
    
    @BeforeClass
    public static void setUp() {
        coreOperationGreaterThan = new CoreOperationGreaterThan(new Expression(), new Expression());
    }
    
    @Test
    public void testComputeValue_LargerNumber() {
        EvalContext context = new EvalContext();
        double arg1 = 10.0;
        double arg2 = 5.0;
        context.set(args[0], arg1);
        context.set(args[1], arg2);
        Object result = coreOperationGreaterThan.computeValue(context);
        assert(result.equals(Boolean.TRUE));
    }
    
    @Test
    public void testComputeValue_SmallerNumber() {
        EvalContext context = new EvalContext();
        double arg1 = 5.0;
        double arg2 = 10.0;
        context.set(args[0], arg1);
        context.set(args[1], arg2);
        Object result = coreOperationGreaterThan.computeValue(context);
        assert(result.equals(Boolean.FALSE));
    }
    
    @Test
    public void testComputeValue_SameNumber() {
        EvalContext context = new EvalContext();
        double arg1 = 5.0;
        double arg2 = 5.0;
        context.set(args[0], arg1);
        context.set(args[1], arg2);
        Object result = coreOperationGreaterThan.computeValue(context);
        assert(result.equals(Boolean.FALSE));
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArguments() {
        EvalContext context = new EvalContext();
        double arg1 = 10.0;
        double arg2 = 5.0;
        context.set(args[0], arg1);
        context.set(args[1], null);
        Object result = coreOperationGreaterThan.computeValue(context);
        assert(result.equals(Boolean.TRUE));
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationGreaterThan.getSymbol();
        assert(symbol.equals(">"));
    }
}