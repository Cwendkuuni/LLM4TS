import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(2);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        // Expected result: false
        Assert.assertFalse((boolean) expression.computeValue(null));

        // Edge case
        args[0] = new MockExpression(Double.MIN_VALUE);
        args[1] = new MockExpression(Double.MIN_VALUE);
        expression = new MockCoreOperationRelationalExpression(args);
        // Expected result: false
        Assert.assertFalse((boolean) expression.computeValue(null));

        // Error scenario
        args[0] = new MockExpression("2");
        args[1] = new MockExpression(2);
        expression = new MockCoreOperationRelationalExpression(args);
        // Expected result: ClassCastException
        Assert.assertThrows(ClassCastException.class, () -> expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        // Expected result: 3
        Assert.assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        // Expected result: false
        Assert.assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        // Test when compare is less than 0
        Assert.assertTrue(expression.evaluateCompare(-1));

        // Test when compare is equal to 0
        Assert.assertFalse(expression.evaluateCompare(0));

        // Test when compare is greater than 0
        Assert.assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(null);

        // Test when both left and right are iterators and match found
        List<Integer> left = new ArrayList<>();
        left.add(1);
        List<Integer> right = new ArrayList<>();
        right.add(1);
        Iterator lit = left.iterator();
        Iterator rit = right.iterator();
        Assert.assertTrue(expression.compute(lit, rit));

        // Test when both left and right are iterators and no match found
        right.clear();
        right.add(2);
        Assert.assertFalse(expression.compute(lit, rit));

        // Test when only left is an iterator and match found
        right.clear();
        right.add(1);
        Assert.assertTrue(expression.compute(lit, right));

        // Test when only left is an iterator and no match found
        right.clear();
        right.add(2);
        Assert.assertFalse(expression.compute(lit, right));

        // Test when only right is an iterator and match found
        left.clear();
        left.add(1);
        lit = left.iterator();
        right.clear();
        right.add(1);
        rit = right.iterator();
        Assert.assertTrue(expression.compute(lit, rit));

        // Test when only right is an iterator and no match found
        right.clear();
        right.add(2);
        rit = right.iterator();
        Assert.assertFalse(expression.compute(lit, rit));

        // Test when both left and right are doubles
        Assert.assertTrue(expression.compute(1.0, 1.0));
        Assert.assertTrue(expression.compute(1.0, 1.0001));
        Assert.assertTrue(expression.compute(1.0001, 1.0));
        Assert.assertTrue(expression.compute(0.0, -1.0));
        Assert.assertFalse(expression.compute(1.0, 0.0));
        Assert.assertFalse(expression.compute(0.0, 0.0));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(null);

        // Test when o is SelfContext
        Assert.assertNull(expression.reduce(new SelfContext()));

        // Test when o is Collection
        List<String> collection = new ArrayList<>();
        collection.add("1");
        Iterator<String> iterator = collection.iterator();
        Assert.assertEquals(iterator, expression.reduce(collection));

        // Test when o is neither SelfContext nor Collection
        Assert.assertEquals("test", expression.reduce("test"));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(null);

        // Test when match found
        List<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        Assert.assertTrue(expression.containsMatch(list.iterator(), "test2"));

        // Test when match not found
        Assert.assertFalse(expression.containsMatch(list.iterator(), "test3"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(null);

        // Test when match found
        List<String> left = new ArrayList<>();
        left.add("test1");
        left.add("test2");
        List<String> right = new ArrayList<>();
        right.add("test3");
        right.add("test2");
        Iterator lit = left.iterator();
        Iterator rit = right.iterator();
        Assert.assertTrue(expression.findMatch(lit, rit));

        // Test when match not found
        right.clear();
        right.add("test3");
        Assert.assertFalse(expression.findMatch(lit, rit));
    }

    private class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }

        public void addDependentExpression(Expression expr) {
        }

        public Expression getRootExpression() {
            return null;
        }

        public Object getPrefix() {
            return null;
        }

        public Expression[] getAncestors() {
            return null;
        }

        public Expression[] getPath() {
            return null;
        }
    }

    private class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public MockCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }
    }
}