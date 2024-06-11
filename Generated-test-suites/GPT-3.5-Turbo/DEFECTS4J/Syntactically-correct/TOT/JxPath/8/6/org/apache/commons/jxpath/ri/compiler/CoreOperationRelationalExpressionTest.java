import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValueLessThan() {
        // Typical use case
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Edge case
        context.getSingleNodePointer().setValue(-1);
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Error scenario
        context.getSingleNodePointer().setValue("test");
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueLessThanOrEqualTo() {
        // Typical use case
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Edge case
        context.getSingleNodePointer().setValue(0);
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Error scenario
        context.getSingleNodePointer().setValue("test");
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueGreaterThan() {
        // Typical use case
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Edge case
        context.getSingleNodePointer().setValue(1);
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Error scenario
        context.getSingleNodePointer().setValue("test");
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueGreaterThanOrEqualTo() {
        // Typical use case
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Edge case
        context.getSingleNodePointer().setValue(0);
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Error scenario
        context.getSingleNodePointer().setValue("test");
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

}