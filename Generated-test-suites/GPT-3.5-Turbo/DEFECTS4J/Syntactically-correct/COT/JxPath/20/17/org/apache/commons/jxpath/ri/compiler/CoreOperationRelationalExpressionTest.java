
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        args[0] = new Expression();
        args[1] = new Expression();
        args[0].compute(context);
        args[1].compute(context);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValueGreaterThanOrEqualTo() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        args[0] = new Expression();
        args[1] = new Expression();
        args[0].compute(context);
        args[1].compute(context);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValueLessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        args[0] = new Expression();
        args[1] = new Expression();
        args[0].compute(context);
        args[1].compute(context);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValueLessThanOrEqualTo() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        args[0] = new Expression();
        args[1] = new Expression();
        args[0].compute(context);
        args[1].compute(context);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        args[0] = new Expression();
        args[1] = new Expression();
        args[0].compute(context);
        args[1].compute(context);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
}
