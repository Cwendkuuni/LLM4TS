import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.EvalContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        boolean evaluateCompare = expression.evaluateCompare(0);
        assertTrue(evaluateCompare);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        Object left = new InitialContext();
        Object right = new InitialContext();
        boolean computeResult = expression.compute(left, right);
        assertTrue(computeResult);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        Object o = new SelfContext();
        Object reduceResult = expression.reduce(o);
        assertNotNull(reduceResult);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value = null;
        boolean containsMatchResult = expression.containsMatch(it, value);
        assertFalse(containsMatchResult);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(new Expression[2]);
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        boolean findMatchResult = expression.findMatch(lit, rit);
        assertFalse(findMatchResult);
    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }
}