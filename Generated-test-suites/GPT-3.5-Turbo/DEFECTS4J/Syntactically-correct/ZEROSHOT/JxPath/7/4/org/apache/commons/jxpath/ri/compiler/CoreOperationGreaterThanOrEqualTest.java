import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = new EvalContext();
        
        // Test case 1
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Object result1 = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result1);
        
        // Test case 2
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        Object result2 = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}