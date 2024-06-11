import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new MockExpression("3");
        Expression arg2 = new MockExpression("5");
        
        Expression[] args = {arg1, arg2};
        
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueWithNaN() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new MockExpression("NaN");
        Expression arg2 = new MockExpression("5");
        
        Expression[] args = {arg1, arg2};
        
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueWithIterator() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new MockExpression("1,2,3");
        Expression arg2 = new MockExpression("4");
        
        Expression[] args = {arg1, arg2};
        
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueWithSameValue() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new MockExpression("2");
        Expression arg2 = new MockExpression("2");
        
        Expression[] args = {arg1, arg2};
        
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValueWithLeftNull() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new MockExpression("null");
        Expression arg2 = new MockExpression("5");
        
        Expression[] args = {arg1, arg2};
        
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValueWithRightNull() {
        EvalContext context = new EvalContext();
        
        Expression arg1 = new MockExpression("5");
        Expression arg2 = new MockExpression("null");
        
        Expression[] args = {arg1, arg2};
        
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        
        Object result = expression.computeValue(context);
        
        Assert.assertEquals(Boolean.FALSE, result);
    }
    
    private class MockExpression extends Expression {
        
        private String value;
        
        public MockExpression(String value) {
            this.value = value;
        }
        
        @Override
        public Object compute(EvalContext context) {
            return value;
        }
        
    }
    
    private class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        
        public MockCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }
        
        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 1;
        }
        
    }
}