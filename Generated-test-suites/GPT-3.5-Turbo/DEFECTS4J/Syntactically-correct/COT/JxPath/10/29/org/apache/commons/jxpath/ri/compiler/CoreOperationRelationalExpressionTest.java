
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        // Test case 1
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(null));

        // Test case 2
        args[0] = new MockExpression(3);
        args[1] = new MockExpression(5);
        expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertEquals(Boolean.FALSE, expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertTrue(expression.evaluateCompare(-1));
        Assert.assertFalse(expression.evaluateCompare(0));
        Assert.assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(5);
        args[1] = new MockExpression(3);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertFalse(expression.compute(5, 3));
        Assert.assertTrue(expression.compute(3, 5));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(new SelfContext());
        args[1] = new MockExpression(new SelfContext());
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Object result = expression.reduce(args[0].computeValue(null));
        Assert.assertEquals(new SelfContext(), result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(new IteratorWrapper(1, 2, 3));
        args[1] = new MockExpression(2);
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertTrue(expression.containsMatch(new IteratorWrapper(1, 2, 3), 2));
        Assert.assertFalse(expression.containsMatch(new IteratorWrapper(1, 2, 3), 4));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression(new IteratorWrapper(1, 2, 3));
        args[1] = new MockExpression(new IteratorWrapper(4, 5, 6));
        CoreOperationRelationalExpression expression = new MockCoreOperationRelationalExpression(args);
        Assert.assertTrue(expression.findMatch(new IteratorWrapper(1, 2, 3), new IteratorWrapper(4, 5, 2)));
        Assert.assertFalse(expression.findMatch(new IteratorWrapper(1, 2, 3), new IteratorWrapper(4, 5, 6)));
    }
}

class MockExpression extends Expression {
    private Object value;

    public MockExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return value;
    }
}

class MockCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

    public MockCoreOperationRelationalExpression(Expression[] args) {
        super(args);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        return compare < 0;
    }
}

class IteratorWrapper implements Iterator<Object> {
    private Object[] elements;
    private int index;

    public IteratorWrapper(Object... elements) {
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

