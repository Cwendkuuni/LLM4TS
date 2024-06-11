import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test for greater than operation
        Expression[] args1 = {new ConstantExpression(5), new ConstantExpression(3)};
        CoreOperationRelationalExpression greaterThan = new GreaterThanExpression(args1);
        EvalContext context1 = new TestEvalContext();
        assertEquals(Boolean.TRUE, greaterThan.computeValue(context1));

        // Test for less than operation
        Expression[] args2 = {new ConstantExpression(2), new ConstantExpression(8)};
        CoreOperationRelationalExpression lessThan = new LessThanExpression(args2);
        EvalContext context2 = new TestEvalContext();
        assertEquals(Boolean.TRUE, lessThan.computeValue(context2));
    }

    @Test
    public void testEvaluateCompare() {
        // Test for equal values
        GreaterThanExpression greaterThan = new GreaterThanExpression(null);
        assertFalse(greaterThan.evaluateCompare(0));

        // Test for different values
        assertTrue(greaterThan.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        // Test when both operands are iterators
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(3, 4, 5).iterator();
        assertTrue(CoreOperationRelationalExpression.compute(left, right));

        // Test when one operand is an iterator
        Iterator left2 = Arrays.asList(1, 2, 3).iterator();
        Object right2 = 3;
        assertTrue(CoreOperationRelationalExpression.compute(left2, right2));
    }

    @Test
    public void testContainsMatch() {
        // Test when a match is found in the iterator
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        Object value = 2;
        assertTrue(CoreOperationRelationalExpression.containsMatch(it, value));

        // Test when a match is not found in the iterator
        Iterator it2 = Arrays.asList(1, 2, 3).iterator();
        Object value2 = 4;
        assertFalse(CoreOperationRelationalExpression.containsMatch(it2, value2));
    }

    @Test
    public void testFindMatch() {
        // Test when there is an intersection between two iterators
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(3, 4, 5).iterator();
        assertTrue(CoreOperationRelationalExpression.findMatch(lit, rit));

        // Test when there is no intersection between two iterators
        Iterator lit2 = Arrays.asList(1, 2, 3).iterator();
        Iterator rit2 = Arrays.asList(4, 5).iterator();
        assertFalse(CoreOperationRelationalExpression.findMatch(lit2, rit2));
    }
}