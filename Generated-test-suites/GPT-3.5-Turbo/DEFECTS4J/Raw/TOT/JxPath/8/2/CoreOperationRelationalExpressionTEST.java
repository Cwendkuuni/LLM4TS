import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression operation;

    @Before
    public void setUp() {
        operation = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("left", 10);
        context.setVariable("right", 5);

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftEqualToRight_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("left", 10);
        context.setVariable("right", 10);

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("left", 5);
        context.setVariable("right", 10);

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftAndRightAreIteratorsWithMatchingElement_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("left", new Object[] {1, 2, 3});
        context.setVariable("right", new Object[] {3, 4, 5});

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftAndRightAreIteratorsWithNoMatchingElement_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("left", new Object[] {1, 2, 3});
        context.setVariable("right", new Object[] {4, 5, 6});

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftIsIteratorContainingMatchingElement_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("left", new Object[] {1, 2, 3});
        context.setVariable("right", 2);

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftIsIteratorContainingNoMatchingElement_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("left", new Object[] {1, 2, 3});
        context.setVariable("right", 5);

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_RightIsIteratorContainingMatchingElement_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("left", 2);
        context.setVariable("right", new Object[] {1, 2, 3});

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_RightIsIteratorContainingNoMatchingElement_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("left", 5);
        context.setVariable("right", new Object[] {1, 2, 3});

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftAndRightAreIteratorsWithMatchingElementInNestedIterators_ReturnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("left", new Object[] {new Object[] {1, 2}, new Object[] {3, 4}});
        context.setVariable("right", new Object[] {2, 5});

        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftAndRightAreIteratorsWithNoMatchingElementInNestedIterators_ReturnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("left", new Object[] {new Object[] {1, 2}, new Object[] {3, 4}});
        context.setVariable("right", new Object[] {5, 6});

        Object result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }
}