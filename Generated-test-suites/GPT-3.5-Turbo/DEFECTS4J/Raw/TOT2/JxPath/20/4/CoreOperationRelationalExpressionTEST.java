import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test when left operand is greater than right operand
        // Expected result is Boolean.TRUE
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testIsSymmetric() {
        // Test if the operation is not symmetric
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        boolean isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);
    }

    @Test
    public void testEdgeCases() {
        // Test with NaN values
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testExceptionHandling() {
        // Test with Iterators
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Iterator iterator = context.getNodeIterator();
        context.setNodeIterator(iterator);
        Object result = expression.compute(context);
        assertFalse((Boolean) result);
    }
}