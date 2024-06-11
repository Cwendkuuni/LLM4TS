import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        int precedence = expression.getPrecedence();

        assertEquals(precedence, org.apache.commons.jxpath.ri.compiler.CoreOperation.RELATIONAL_EXPR_PRECEDENCE);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        boolean symmetric = expression.isSymmetric();

        assertFalse(symmetric);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        Object left = new Object();
        Object right = new Object();

        boolean result = expression.compute(left, right);

        assertFalse(result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        Object o = new Object();
        Object result = expression.reduce(o);

        assertEquals(result, o);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        Iterator it = new IteratorMock();
        Object value = new Object();

        boolean result = expression.containsMatch(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(args);

        Iterator lit = new IteratorMock();
        Iterator rit = new IteratorMock();

        boolean result = expression.findMatch(lit, rit);

        assertFalse(result);
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {
        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private class IteratorMock implements Iterator {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }

        public void remove() {
        }
    }
}