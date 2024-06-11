import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    // Test computeValue method

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression[] args = new Expression[]{new Constant(5), new Constant(10)};
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        EvalContext context = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Edge case: compare two equal values
        args = new Expression[]{new Constant(5), new Constant(5)};
        expression = new ConcreteCoreOperationRelationalExpression(args);
        context = new EvalContext();
        Assert.assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Edge case: compare two non-equal values
        args = new Expression[]{new Constant(5), new Constant(10)};
        expression = new ConcreteCoreOperationRelationalExpression(args);
        context = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Error scenario: evaluate non-numeric values
        args = new Expression[]{new Constant("foo"), new Constant("bar")};
        expression = new ConcreteCoreOperationRelationalExpression(args);
        context = new EvalContext();
        Assert.assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    // Test getPrecedence method

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);
        Assert.assertEquals(3, expression.getPrecedence());
    }

    // Test isSymmetric method

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);
        Assert.assertFalse(expression.isSymmetric());
    }

    // Test evaluateCompare method

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);
        Assert.assertTrue(expression.evaluateCompare(0));
        Assert.assertTrue(expression.evaluateCompare(-1));
        Assert.assertFalse(expression.evaluateCompare(1));
    }

    // Test compute method

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);
        Assert.assertTrue(expression.compute(5, 10));
        Assert.assertFalse(expression.compute(10, 5));
    }

    // Test reduce method

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);

        // Reduce an Iterator
        Iterator<Object> iterator = Arrays.asList(1, 2, 3).iterator();
        Object result = expression.reduce(iterator);
        Assert.assertTrue(result instanceof Iterator);

        // Reduce a SelfContext
        EvalContext evalContext = new EvalContext();
        SelfContext selfContext = new SelfContext(evalContext);
        result = expression.reduce(selfContext);
        Assert.assertTrue(result instanceof EvalContext);

        // Reduce a Collection
        HashSet<Object> set = new HashSet<>(Arrays.asList(1, 2, 3));
        result = expression.reduce(set);
        Assert.assertTrue(result instanceof Iterator);

        // Reduce an object that is not an Iterator or Collection
        Object object = 5;
        result = expression.reduce(object);
        Assert.assertEquals(object, result);
    }

    // Test containsMatch method

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);
        Iterator<Object> iterator = Arrays.asList(1, 2, 3).iterator();

        // Match found
        Assert.assertTrue(expression.containsMatch(iterator, 2));

        // Match not found
        Assert.assertFalse(expression.containsMatch(iterator, 4));
    }

    // Test findMatch method

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(null);
        Iterator<Object> left = Arrays.asList(1, 2, 3, 4).iterator();
        Iterator<Object> right = Arrays.asList(3, 4, 5).iterator();

        // Match found
        Assert.assertTrue(expression.findMatch(left, right));

        // Match not found
        Assert.assertFalse(expression.findMatch(left, right));
    }

    // Concrete class to test the abstract CoreOperationRelationalExpression

    private class ConcreteCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        ConcreteCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }
    }
}