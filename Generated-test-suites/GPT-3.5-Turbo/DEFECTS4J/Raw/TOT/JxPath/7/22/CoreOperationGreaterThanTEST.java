import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_typicalCase_returnsTrue() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);          // Mock context node
        context.setContextPosition(2);      // Mock context position
        context.setContextSize(3);          // Mock context size
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_leftExpressionReturnsNull_returnsFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new NullExpression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);
        context.setContextPosition(2);
        context.setContextSize(3);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_rightExpressionReturnsNull_returnsFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new NullExpression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);
        context.setContextPosition(2);
        context.setContextSize(3);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_bothExpressionsReturnNull_returnsFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new NullExpression(), new NullExpression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);
        context.setContextPosition(2);
        context.setContextSize(3);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_leftExpressionNotNumeric_returnsFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new NonNumericExpression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);
        context.setContextPosition(2);
        context.setContextSize(3);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_rightExpressionNotNumeric_returnsFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new NonNumericExpression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);
        context.setContextPosition(2);
        context.setContextSize(3);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_bothExpressionsNotNumeric_returnsFalse() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new NonNumericExpression(), new NonNumericExpression());
        EvalContext context = new EvalContext();
        context.setContextNode(1);
        context.setContextPosition(2);
        context.setContextSize(3);
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_returnsGreaterThanSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        Assert.assertEquals(">", symbol);
    }
}