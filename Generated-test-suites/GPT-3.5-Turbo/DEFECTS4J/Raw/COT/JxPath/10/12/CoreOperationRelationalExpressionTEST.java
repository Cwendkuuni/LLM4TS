import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test case for computeValue method
        EvalContext context = new EvalContext();
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        context.setValue(expression.computeValue(context));

        assertNotNull("Compute Value must not be null", context.getValue());
        assertTrue("Compute Value must be true", (Boolean) context.getValue());
    }

    @Test
    public void testGetPrecedence() {
        // Test case for getPrecedence method
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertEquals("Precedence must be 3", 3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        // Test case for isSymmetric method
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        assertFalse("Symmetric must be false", expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        // Test case for compute method
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        // Test 1: left > right
        assertTrue("Compute must be true if left > right", expression.compute(5, 3));

        // Test 2: left == right
        assertFalse("Compute must be false if left == right", expression.compute(5, 5));

        // Test 3: left < right
        assertFalse("Compute must be false if left < right", expression.compute(3, 5));
    }

    @Test
    public void testReduce() {
        // Test case for reduce method
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        // Test 1: o is SelfContext
        EvalContext evalContext = new EvalContext();
        expression.reduce(evalContext);

        assertEquals("Reduce must return SingleNodePointer", evalContext.getSingleNodePointer(), evalContext.getValue());

        // Test 2: o is Collection
        HashSet<Object> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);

        expression.reduce(set);

        assertTrue("Reduce must return Iterator", set.iterator().equals(expression.reduce(set)));

        // Test 3: o is neither SelfContext nor Collection
        assertEquals("Reduce must return input object", "test", expression.reduce("test"));
    }

    @Test
    public void testContainsMatch() {
        // Test case for containsMatch method
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        HashSet<Object> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);

        // Test 1: Iterator contains matching value
        assertTrue("Contains Match must be true if matching value is found in Iterator", expression.containsMatch(set.iterator(), 2));

        // Test 2: Iterator does not contain matching value
        assertFalse("Contains Match must be false if matching value is not found in Iterator", expression.containsMatch(set.iterator(), 4));
    }

    @Test
    public void testFindMatch() {
        // Test case for findMatch method
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        HashSet<Object> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);

        HashSet<Object> set2 = new HashSet<>();
        set2.add(3);
        set2.add(4);
        set2.add(5);

        HashSet<Object> set3 = new HashSet<>();
        set3.add(6);
        set3.add(7);
        set3.add(8);

        // Test 1: Matching value is found
        assertTrue("Find Match must be true if matching value is found", expression.findMatch(set1.iterator(), set2.iterator()));

        // Test 2: Matching value is not found
        assertFalse("Find Match must be false if matching value is not found", expression.findMatch(set1.iterator(), set3.iterator()));
    }

}