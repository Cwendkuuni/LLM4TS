import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual operation;

    @Before
    public void setup() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
    }

    @Test
    public void testComputeValue_BothOperandsNull_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_LeftOperandNull_ReturnsFalse() {
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_RightOperandNull_ReturnsFalse() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.0;
            }
        };
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, null);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_BothOperandsNumber_ReturnsCorrectResult() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 10.5;
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return 5.5;
            }
        };
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_BothOperandsString_ReturnsFalse() {
        Expression arg1 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "Hello";
            }
        };
        Expression arg2 = new Expression() {
            @Override
            public Object computeValue(EvalContext context) {
                return "World";
            }
        };
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqual() {
        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }

}