import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical Use Case
        Expression expr1 = new MockExpression(5);
        Expression expr2 = new MockExpression(10);
        CoreOperationRelationalExpression operation = new MockCoreOperationRelationalExpression(expr1, expr2);
        EvalContext context = new MockEvalContext();

        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Edge Case
        expr1 = new MockExpression(10.5);
        expr2 = new MockExpression(10.5);
        operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Error Scenario
        expr1 = new MockExpression("test");
        expr2 = new MockExpression(10);
        operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEvaluateCompare() {
        // Typical Use Case
        Expression expr1 = new MockExpression(5);
        Expression expr2 = new MockExpression(10);
        CoreOperationRelationalExpression operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        boolean result = operation.evaluateCompare(-1);
        Assert.assertTrue(result);

        // Edge Case
        expr1 = new MockExpression(10.5);
        expr2 = new MockExpression(10.5);
        operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        result = operation.evaluateCompare(0);
        Assert.assertTrue(result);

        // Error Scenario
        expr1 = new MockExpression("test");
        expr2 = new MockExpression(10);
        operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        result = operation.evaluateCompare(0);
        Assert.assertFalse(result);
    }

    @Test
    public void testCompute() {
        // Typical Use Case
        Expression expr1 = new MockExpression(5);
        Expression expr2 = new MockExpression(10);
        CoreOperationRelationalExpression operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        boolean result = operation.compute(5, 10);
        Assert.assertTrue(result);

        // Edge Case
        expr1 = new MockExpression(10.5);
        expr2 = new MockExpression(10.5);
        operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        result = operation.compute(10.5, 10.5);
        Assert.assertTrue(result);

        // Error Scenario
        expr1 = new MockExpression("test");
        expr2 = new MockExpression(10);
        operation = new MockCoreOperationRelationalExpression(expr1, expr2);

        result = operation.compute("test", 10);
        Assert.assertFalse(result);
    }

    // Include additional test methods for private methods reduce, containsMatch, and findMatch
    
    @Test
    public void testReduce() {
        // Typical Use Case
        Object result = MockCoreOperationRelationalExpression.invokeReduce(new MockExpression(new SelfContext()));
        Assert.assertTrue(result instanceof EvalContext);

        // Edge Case
        result = MockCoreOperationRelationalExpression.invokeReduce(new MockExpression(new HashSet<Integer>()));
        Assert.assertTrue(result instanceof Iterator);

        // Error Scenario
        result = MockCoreOperationRelationalExpression.invokeReduce(new MockExpression(5));
        Assert.assertEquals(5, result);
    }

    @Test
    public void testContainsMatch() {
        // Typical Use Case
        Iterator<Integer> it = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        CoreOperationRelationalExpression operation = new MockCoreOperationRelationalExpression(null, null);

        boolean result = MockCoreOperationRelationalExpression.invokeContainsMatch(it, 3);
        Assert.assertTrue(result);

        // Edge Case
        it = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        result = MockCoreOperationRelationalExpression.invokeContainsMatch(it, 10);
        Assert.assertFalse(result);

        // Error Scenario
        it = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        result = MockCoreOperationRelationalExpression.invokeContainsMatch(it, "test");
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Typical Use Case
        Iterator<Integer> lit = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        Iterator<Integer> rit = new MockIterator<>(new Integer[]{6, 7, 8, 9, 10});
        CoreOperationRelationalExpression operation = new MockCoreOperationRelationalExpression(null, null);

        boolean result = MockCoreOperationRelationalExpression.invokeFindMatch(lit, rit);
        Assert.assertFalse(result);

        // Edge Case
        lit = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        rit = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        result = MockCoreOperationRelationalExpression.invokeFindMatch(lit, rit);
        Assert.assertTrue(result);

        // Error Scenario
        lit = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        rit = new MockIterator<>(new Integer[]{1, 2, 3, 4, 5});
        result = MockCoreOperationRelationalExpression.invokeFindMatch(null, rit);
        Assert.assertFalse(result);
    }
}