import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = null;
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        int result = expression.getPrecedence();
        assertEquals(3, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        boolean result = expression.isSymmetric();
        assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Object o = null;
        Object result = expression.reduce(o);
        assertEquals(null, result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator it = null;
        Object value = null;
        boolean result = expression.containsMatch(it, value);
        assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator lit = null;
        Iterator rit = null;
        boolean result = expression.findMatch(lit, rit);
        assertEquals(false, result);
    }
    
}