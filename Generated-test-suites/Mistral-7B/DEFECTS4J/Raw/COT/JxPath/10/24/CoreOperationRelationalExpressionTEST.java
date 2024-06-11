import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});

        Object leftValue = mock(Object.class);
        Object rightValue = mock(Object.class);

        when(arg1.computeValue(EvalContext.newInstance())).thenReturn(leftValue);
        when(arg2.computeValue(EvalContext.newInstance())).thenReturn(rightValue);

        Boolean result = expression.computeValue(EvalContext.newInstance());

        Assert.assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);

        int precedence = expression.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);

        boolean symmetric = expression.isSymmetric();

        Assert.assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsGreaterThanRight() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan();

        boolean result = expression.evaluateCompare(1);

        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsEqualRight() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual();

        boolean result = expression.evaluateCompare(0);

        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsLessThanRight() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan();

        boolean result = expression.evaluateCompare(-1);

        Assert.assertTrue(result);
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsLessThanOrEqualRight() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual();

        boolean result = expression.evaluateCompare(-1);

        Assert.assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testEvaluateCompare_WhenLeftIsEqualToNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new AssertionError("Should not be called with NaN values.");
            }
        };

        expression.evaluateCompare(Double.NaN);
    }

    @Test(expected = AssertionError.class)
    public void testEvaluateCompare_WhenRightIsEqualToNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new AssertionError("Should not be called with NaN values.");
            }
        };

        expression.evaluateCompare(0, Double.NaN);
    }

    @Test
    public void testComputeWithInitialContext() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});

        InitialContext leftInitialContext = mock(InitialContext.class);
        InitialContext rightInitialContext = mock(InitialContext.class);

        when(arg1.computeValue(EvalContext.newInstance())).thenReturn(leftInitialContext);
        when(arg2.computeValue(EvalContext.newInstance())).thenReturn(rightInitialContext);

        Boolean result = expression.computeValue(EvalContext.newInstance());

        Assert.assertEquals(Boolean.FALSE, result);

        // Reset both initial contexts
        ((EvalContext) leftInitialContext).reset();
        ((EvalContext) rightInitialContext).reset();
    }

    @Test
    public void testComputeWithCollectionIterator() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{arg1, arg2});

        Iterator<Object> leftIterator = Collections.singletonList("A").iterator();
        Iterator<Object> rightIterator = Collections.singletonList("A").iterator();

        when(arg1.computeValue(EvalContext.newInstance())).thenReturn(leftIterator);
        when(arg2.computeValue(EvalContext.newInstance())).thenReturn(rightIterator);

        Boolean result = expression.computeValue(EvalContext.newInstance());

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);

        Iterator<Object> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn("A").thenReturn("B");

        Object value = "A";

        boolean result = expression.containsMatch(iterator, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]);

        Iterator<Object> leftIterator = mock(Iterator.class);
        Iterator<Object> rightIterator = mock(Iterator.class);

        when(leftIterator.iterator()).thenReturn(leftIterator);
        when(rightIterator.iterator()).thenReturn(rightIterator);

        HashSet<Object> leftSet = new HashSet<>();
        leftSet.add("A");
        leftSet.add("B");

        when(leftIterator.next()).thenReturn("A");
        when(leftIterator.hasNext()).thenReturn(true).thenReturn(false);

        when(rightIterator.next()).thenReturn("A");
        when(rightIterator.hasNext()).thenReturn(true).thenReturn(false);

        boolean result = expression.findMatch(leftIterator, rightIterator);

        Assert.assertTrue(result);
    }

}