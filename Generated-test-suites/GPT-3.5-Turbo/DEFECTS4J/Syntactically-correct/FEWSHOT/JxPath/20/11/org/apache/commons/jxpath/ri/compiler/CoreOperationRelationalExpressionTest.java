import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValueGreaterThan() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }

    @Test
    public void testComputeValueGreaterThanOrEqual() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValueLessThan() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testComputeValueLessThanOrEqual() {
        Expression[] args = { new Expression(), new Expression() };
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext()));
    }
}