import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test when the first argument is less than the second argument
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        CoreOperationLessThan expression = new CoreOperationLessThan(arg1, arg2);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Test when the first argument is equal to the second argument
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        expression = new CoreOperationLessThan(arg1, arg2);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        // Test when the first argument is greater than the second argument
        arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        expression = new CoreOperationLessThan(arg1, arg2);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 10;
            }
        };
        CoreOperationLessThan expression = new CoreOperationLessThan(arg1, arg2);
        String symbol = expression.getSymbol();
        assertEquals("<", symbol);
    }
}