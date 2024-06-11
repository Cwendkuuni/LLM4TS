import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test when both arguments are integers
        Expression[] args = {new Literal(5), new Literal(10)};
        CoreOperationRelationalExpression expression = new LessThan(args);
        EvalContext context = new DummyEvalContext();
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test when arguments are double values
        Expression[] args2 = {new Literal(2.5), new Literal(3.5)};
        CoreOperationRelationalExpression expression2 = new GreaterThanOrEqual(args2);
        EvalContext context2 = new DummyEvalContext();
        assertEquals(Boolean.FALSE, expression2.computeValue(context2));

        // Test when one argument is InitialContext and the other is Iterator
        Expression[] args3 = {new InitialContext(), new DummyIterator()};
        CoreOperationRelationalExpression expression3 = new LessThan(args3);
        EvalContext context3 = new DummyEvalContext();
        assertEquals(Boolean.FALSE, expression3.computeValue(context3));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new LessThan(null);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterThanOrEqual(null);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        // Test edge cases for evaluateCompare method
        Expression[] args = {new Literal(10), new Literal(10)};
        CoreOperationRelationalExpression expression = new GreaterThanOrEqual(args);
        assertTrue(expression.evaluateCompare(0));

        // Test exception handling for reduce method
        Expression[] args2 = {new SelfContext(), new DummyIterator()};
        CoreOperationRelationalExpression expression2 = new LessThan(args2);
        EvalContext context = new DummyEvalContext();
        assertFalse(expression2.computeValue(context));
    }

    @Test
    public void testContainsMatch() {
        // Test when Iterator contains the matching element
        Iterator left = new DummyIterator();
        Object right = 5;
        CoreOperationRelationalExpression expression = new LessThan(null);
        assertTrue(expression.containsMatch(left, right));

        // Test when Iterator does not contain the matching element
        Iterator left2 = new DummyIterator();
        Object right2 = 10;
        CoreOperationRelationalExpression expression2 = new GreaterThanOrEqual(null);
        assertFalse(expression2.containsMatch(left2, right2));
    }

    @Test
    public void testFindMatch() {
        // Test when matching element is found in the Iterators
        Iterator left = new DummyIterator();
        Iterator right = new DummyIterator();
        CoreOperationRelationalExpression expression = new LessThan(null);
        assertTrue(expression.findMatch(left, right));

        // Test when matching element is not found in the Iterators
        Iterator left2 = new DummyIterator();
        Iterator right2 = new DummyIterator();
        CoreOperationRelationalExpression expression2 = new GreaterThanOrEqual(null);
        assertFalse(expression2.findMatch(left2, right2));
    }
}

class DummyEvalContext extends EvalContext {
    @Override
    public Object getSingleNodePointer() {
        return null;
    }
}

class DummyIterator implements Iterator {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }
}