import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
    }

    @Test
    public void testComputeValue() {
        // Test case 1
        EvalContext context1 = new EvalContext();
        double arg1Value1 = 3.0;
        double arg2Value1 = 2.0;
        operation.args[0] = new ExpressionUtil.DoubleExpression(arg1Value1);
        operation.args[1] = new ExpressionUtil.DoubleExpression(arg2Value1);
        Object result1 = operation.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);

        // Test case 2
        EvalContext context2 = new EvalContext();
        double arg1Value2 = 2.0;
        double arg2Value2 = 3.0;
        operation.args[0] = new ExpressionUtil.DoubleExpression(arg1Value2);
        operation.args[1] = new ExpressionUtil.DoubleExpression(arg2Value2);
        Object result2 = operation.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);

        // Test case 3
        EvalContext context3 = new EvalContext();
        double arg1Value3 = 3.0;
        double arg2Value3 = 3.0;
        operation.args[0] = new ExpressionUtil.DoubleExpression(arg1Value3);
        operation.args[1] = new ExpressionUtil.DoubleExpression(arg2Value3);
        Object result3 = operation.computeValue(context3);
        assertEquals(Boolean.TRUE, result3);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}