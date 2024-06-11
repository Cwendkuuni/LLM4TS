import org.junit.*;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertNotNull(expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(expression.compute(null, null));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertNull(expression.reduce(null));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(expression.containsMatch(null, null));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        assertFalse(expression.findMatch(null, null));
    }

    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

}