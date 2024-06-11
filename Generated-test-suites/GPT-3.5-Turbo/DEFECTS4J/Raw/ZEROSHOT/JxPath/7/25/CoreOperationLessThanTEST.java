import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };

        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((boolean) result);
    }
    
    @Test
    public void testComputeValueFalse() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 6;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };

        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 4;
            }
        };
        
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals("<", symbol);
    }
}