import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(1);
        args[1] = new TestExpression(2);
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext(null);
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(1);
        args[1] = new TestExpression(2);
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean result = expression.evaluateCompare(0);

        Assert.assertFalse(result);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(1);
        args[1] = new TestExpression(2);
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean result = expression.compute(1, 2);

        Assert.assertFalse(result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(1);
        args[1] = new TestExpression(2);
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        Object result = expression.reduce(new InitialContext(null));

        Assert.assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(new ArrayList<Integer>() {{ add(1); add(2); add(3); }});
        args[1] = new TestExpression(2);
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean result = expression.containsMatch(((List<Integer>)args[0].compute(null)).iterator(), 2);

        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(new ArrayList<Integer>() {{ add(1); add(2); add(3); }});
        args[1] = new TestExpression(new ArrayList<Integer>() {{ add(3); add(4); add(5); }});
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean result = expression.findMatch(((List<Integer>)args[0].compute(null)).iterator(), ((List<Integer>)args[1].compute(null)).iterator());

        Assert.assertTrue(result);
    }
    
    // Additional test cases to cover all public methods of CoreOperationRelationalExpression
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        int result = expression.getPrecedence();

        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, result);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean result = expression.isSymmetric();

        Assert.assertFalse(result);
    }

    // Test helper classes

    private static class TestExpression extends Expression {
        private Object value;

        public TestExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }
    }

    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }
}