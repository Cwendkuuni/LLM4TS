import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression[] args1 = new Expression[]{new TestExpression(5), new TestExpression(10)};
        CoreOperationRelationalExpression expression1 = new TestCoreOperationRelationalExpression(args1);
        assertEquals(Boolean.TRUE, expression1.computeValue(new TestEvalContext()));

        // Edge case
        Expression[] args2 = new Expression[]{new TestExpression(Double.NaN), new TestExpression(10)};
        CoreOperationRelationalExpression expression2 = new TestCoreOperationRelationalExpression(args2);
        assertEquals(Boolean.FALSE, expression2.computeValue(new TestEvalContext()));

        // Error scenario
        Expression[] args3 = new Expression[]{new TestExpression("hello"), new TestExpression(10)};
        CoreOperationRelationalExpression expression3 = new TestCoreOperationRelationalExpression(args3);
        assertEquals(Boolean.FALSE, expression3.computeValue(new TestEvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[]{new TestExpression(5), new TestExpression(10)};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        assertEquals(2, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[]{new TestExpression(5), new TestExpression(10)};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[]{new TestExpression(5), new TestExpression(10)};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        // Typical use case
        Expression[] args1 = new Expression[]{new TestExpression(5), new TestExpression(10)};
        CoreOperationRelationalExpression expression1 = new TestCoreOperationRelationalExpression(args1);
        assertTrue(expression1.compute(5, 10));

        // Edge case
        Expression[] args2 = new Expression[]{new TestExpression(Double.NaN), new TestExpression(10)};
        CoreOperationRelationalExpression expression2 = new TestCoreOperationRelationalExpression(args2);
        assertFalse(expression2.compute(Double.NaN, 10));

        // Error scenario
        Expression[] args3 = new Expression[]{new TestExpression("hello"), new TestExpression(10)};
        CoreOperationRelationalExpression expression3 = new TestCoreOperationRelationalExpression(args3);
        assertFalse(expression3.compute("hello", 10));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[]{new TestExpression(5), new TestExpression(10)};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        assertEquals(5, expression.reduce(5));
        assertSame(args[0], expression.reduce(args[0]));
    }

    @Test
    public void testContainsMatch() {
        // Typical use case
        Expression[] args1 = new Expression[]{new TestExpression(5)};
        CoreOperationRelationalExpression expression1 = new TestCoreOperationRelationalExpression(args1);
        Iterator it1 = expression1.new TestIterator(new Object[]{1, 2, 3, 4, 5});
        assertTrue(expression1.containsMatch(it1, 5));

        // Edge case
        Expression[] args2 = new Expression[]{new TestExpression("hello")};
        CoreOperationRelationalExpression expression2 = new TestCoreOperationRelationalExpression(args2);
        Iterator it2 = expression2.new TestIterator(new Object[]{"hello", "world"});
        assertTrue(expression2.containsMatch(it2, "hello"));

        // Error scenario
        Expression[] args3 = new Expression[]{new TestExpression(10)};
        CoreOperationRelationalExpression expression3 = new TestCoreOperationRelationalExpression(args3);
        Iterator it3 = expression3.new TestIterator(new Object[]{1, 2, 3, 4, 5});
        assertFalse(expression3.containsMatch(it3, 10));
    }

    @Test
    public void testFindMatch() {
        // Typical use case
        Expression[] args1 = new Expression[]{new TestExpression(5)};
        CoreOperationRelationalExpression expression1 = new TestCoreOperationRelationalExpression(args1);
        Iterator lit1 = expression1.new TestIterator(new Object[]{1, 2, 3, 4, 5});
        Iterator rit1 = expression1.new TestIterator(new Object[]{5, 6, 7, 8, 9});
        assertTrue(expression1.findMatch(lit1, rit1));

        // Edge case
        Expression[] args2 = new Expression[]{new TestExpression(10)};
        CoreOperationRelationalExpression expression2 = new TestCoreOperationRelationalExpression(args2);
        Iterator lit2 = expression2.new TestIterator(new Object[]{1, 2, 3, 4, 5});
        Iterator rit2 = expression2.new TestIterator(new Object[]{5, 6, 7, 8, 9});
        assertFalse(expression2.findMatch(lit2, rit2));

        // Error scenario
        Expression[] args3 = new Expression[]{new TestExpression("hello")};
        CoreOperationRelationalExpression expression3 = new TestCoreOperationRelationalExpression(args3);
        Iterator lit3 = expression3.new TestIterator(new Object[]{"world"});
        Iterator rit3 = expression3.new TestIterator(new Object[]{5, 6, 7, 8, 9});
        assertFalse(expression3.findMatch(lit3, rit3));
    }

    /********** Helper Classes **********/

    // TestExpression class for testing purposes
    private class TestExpression extends Expression {
        private Object value;

        public TestExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }

    // TestCoreOperationRelationalExpression class for testing purposes
    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }

        // TestIterator class for testing purposes
        private class TestIterator implements Iterator {
            private Object[] elements;
            private int index;

            public TestIterator(Object[] elements) {
                this.elements = elements;
                this.index = 0;
            }

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        }
    }

    // TestEvalContext class for testing purposes
    private class TestEvalContext extends EvalContext {
        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }
}