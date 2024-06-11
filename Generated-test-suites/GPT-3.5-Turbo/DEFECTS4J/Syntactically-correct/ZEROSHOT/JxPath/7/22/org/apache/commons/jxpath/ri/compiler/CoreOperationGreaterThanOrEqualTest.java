import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 5;
            }
        };
        Expression arg2 = new Expression() {
            public Object computeValue(EvalContext context) {
                return 2;
            }
        };
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        boolean result = (boolean) operation.computeValue(null);
        Assert.assertTrue(result);
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
                return 2;
            }
        };
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }
}