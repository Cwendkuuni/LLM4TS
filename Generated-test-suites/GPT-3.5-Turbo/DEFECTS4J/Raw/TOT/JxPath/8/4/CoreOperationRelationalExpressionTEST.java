import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test with two unequal values
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression(5), new DummyExpression(10)});
        boolean result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with two equal values
        expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression(10), new DummyExpression(10)});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with left value as InitialContext
        expression = new CoreOperationRelationalExpression(new Expression[]{new InitialContext(), new DummyExpression(10)});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with right value as InitialContext
        expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression(10), new InitialContext()});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with both left and right values as Iterator
        Iterator iterator1 = Arrays.asList(1,2,3).iterator();
        Iterator iterator2 = Arrays.asList(4,5,6).iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression(iterator1), new DummyExpression(iterator2)});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with left value as Iterator and right value not in the Iterator
        iterator1 = Arrays.asList("a", "b", "c").iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression(iterator1), new DummyExpression("d")});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with right value as Iterator and left value not in the Iterator
        iterator1 = Arrays.asList("a", "b", "c").iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression("d"), new DummyExpression(iterator1)});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);

        // Test with left value and right value both as Double
        expression = new CoreOperationRelationalExpression(new Expression[]{new DummyExpression(2.5), new DummyExpression(2.0)});
        result = (Boolean) expression.computeValue(new DummyEvalContext());
        assertFalse(result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        // Test with compare = 0
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        boolean result = expression.evaluateCompare(0);
        assertTrue(result);

        // Test with compare < 0
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.evaluateCompare(-1);
        assertTrue(result);

        // Test with compare > 0
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.evaluateCompare(1);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        // Test with left and right as equal Double values
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        boolean result = expression.compute(2.5, 2.5);
        assertFalse(result);

        // Test with left smaller Double value and right larger Double value
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.compute(2.0, 2.5);
        assertTrue(result);

        // Test with left larger Double value and right smaller Double value
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.compute(2.5, 2.0);
        assertFalse(result);

        // Test with left and right as equal Collection values
        HashSet left = new HashSet(Arrays.asList("a", "b", "c"));
        HashSet right = new HashSet(Arrays.asList("a", "b", "c"));
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.compute(left, right);
        assertFalse(result);

        // Test with left as Iterator value and right not in Iterator
        Iterator iterator = Arrays.asList("a", "b", "c").iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.compute(iterator, "d");
        assertFalse(result);

        // Test with right as Iterator value and left not in Iterator
        iterator = Arrays.asList("a", "b", "c").iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.compute("d", iterator);
        assertFalse(result);

        // Test with left and right as Double values
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.compute(2.5, 2.0);
        assertFalse(result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        Object result = expression.reduce(new SelfContext());
        assertNull(result);

        result = expression.reduce(Arrays.asList(1, 2, 3));
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        // Test with iterator containing matching value
        Iterator iterator = Arrays.asList("a", "b", "c").iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        boolean result = expression.containsMatch(iterator, "b");
        assertTrue(result);

        // Test with iterator not containing matching value
        iterator = Arrays.asList("a", "b", "c").iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.containsMatch(iterator, "d");
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Test with left iterator containing match in right iterator
        Iterator left = Arrays.asList(1, 2, 3).iterator();
        Iterator right = Arrays.asList(3, 4, 5).iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{});
        boolean result = expression.findMatch(left, right);
        assertTrue(result);

        // Test with left iterator not containing match in right iterator
        left = Arrays.asList(1, 2, 3).iterator();
        right = Arrays.asList(4, 5, 6).iterator();
        expression = new CoreOperationRelationalExpression(new Expression[]{});
        result = expression.findMatch(left, right);
        assertFalse(result);
    }

    // Dummy classes for testing purposes
    private static class DummyExpression extends Expression {
        public DummyExpression(Object value) {
            super();
        }

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }

    private static class DummyEvalContext extends EvalContext {
        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }
}