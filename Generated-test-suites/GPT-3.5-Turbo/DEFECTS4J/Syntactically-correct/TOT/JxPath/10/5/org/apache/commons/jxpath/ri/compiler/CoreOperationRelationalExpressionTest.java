import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test for greater than operation
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        args[0].computeValue(context);
        args[1].computeValue(context);
        assertTrue(operation.computeValue(context));

        // Test for less than operation
        args = new Expression[]{new Expression(), new Expression()};
        operation = new ConcreteOperation(args);
        context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        args[0].computeValue(context);
        args[1].computeValue(context);
        assertFalse(operation.computeValue(context));

        // Test for greater than or equal to operation
        args = new Expression[]{new Expression(), new Expression()};
        operation = new ConcreteOperation(args);
        context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        args[0].computeValue(context);
        args[1].computeValue(context);
        assertTrue(operation.computeValue(context));

        // Test for less than or equal to operation
        args = new Expression[]{new Expression(), new Expression()};
        operation = new ConcreteOperation(args);
        context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        args[0].computeValue(context);
        args[1].computeValue(context);
        assertFalse(operation.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
        assertEquals(3, operation.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
        assertFalse(operation.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
        assertTrue(operation.evaluateCompare(0));
        assertFalse(operation.evaluateCompare(-1));
        assertFalse(operation.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
        Object left = "2";
        Object right = 2;
        assertFalse(operation.compute(left, right));
        assertTrue(operation.compute(right, left));
    }

    @Test
    public void testReduce() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
        Object o = new SelfContext();
        assertEquals(o, operation.reduce(o));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
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
        Object value = "value";
        assertFalse(operation.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = {new Expression(), new Expression()};
        CoreOperationRelationalExpression operation = new ConcreteOperation(args);
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
        assertFalse(operation.findMatch(lit, rit));
    }
}