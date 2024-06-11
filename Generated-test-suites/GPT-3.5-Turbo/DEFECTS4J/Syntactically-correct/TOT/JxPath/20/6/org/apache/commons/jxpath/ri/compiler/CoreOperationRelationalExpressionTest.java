import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        EvalContext context = new EvalContext();
        
        context.setContextNodeSet(new Object[]{1, 2, 3});
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
        
        context.setContextNodeSet(new Object[]{3, 2, 1});
        result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_GreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        EvalContext context = new EvalContext();
        
        context.setContextNodeSet(new Object[]{1, 2, 3});
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
        
        context.setContextNodeSet(new Object[]{2, 2, 2});
        result = expression.computeValue(context);
        assertTrue((Boolean) result);
        
        context.setContextNodeSet(new Object[]{3, 2, 1});
        result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        EvalContext context = new EvalContext();
        
        context.setContextNodeSet(new Object[]{1, 2, 3});
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
        
        context.setContextNodeSet(new Object[]{3, 2, 1});
        result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_LessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        EvalContext context = new EvalContext();
        
        context.setContextNodeSet(new Object[]{1, 2, 3});
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
        
        context.setContextNodeSet(new Object[]{2, 2, 2});
        result = expression.computeValue(context);
        assertTrue((Boolean) result);
        
        context.setContextNodeSet(new Object[]{3, 2, 1});
        result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    // Edge cases
    @Test
    public void testComputeValue_EdgeCases() {
        // Empty node set
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        EvalContext context = new EvalContext();
        
        context.setContextNodeSet(new Object[]{});
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
        
        // Single element node set
        context.setContextNodeSet(new Object[]{1});
        result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    // Error scenarios
    @Test
    public void testComputeValue_NullNodeSet() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(null);
        EvalContext context = new EvalContext();
        
        context.setContextNodeSet(null);
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }
}

class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
    
    public CoreOperationRelationalExpressionMock(Expression[] args) {
        super(args);
    }
    
    protected boolean evaluateCompare(int compare) {
        return false;
    }
}