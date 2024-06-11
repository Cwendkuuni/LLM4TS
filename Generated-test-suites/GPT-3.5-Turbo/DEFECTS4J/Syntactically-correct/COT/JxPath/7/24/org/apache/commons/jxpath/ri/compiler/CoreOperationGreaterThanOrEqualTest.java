import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test case 1: arg1 > arg2
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        Object result = operation.computeValue(context);
        Assert.assertEquals(true, result);
        
        // Test case 2: arg1 < arg2
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        result = operation.computeValue(context);
        Assert.assertEquals(false, result);
        
        // Test case 3: arg1 = arg2
        operation.args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        operation.args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return 3;
            }
        };
        result = operation.computeValue(context);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }
}