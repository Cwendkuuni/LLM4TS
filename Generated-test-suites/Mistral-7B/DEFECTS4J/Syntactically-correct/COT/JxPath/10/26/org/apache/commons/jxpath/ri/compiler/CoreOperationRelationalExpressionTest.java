import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationArgument;
import org.apache.commons.jxpath.ri.compiler.CoreOperationConstant;
import org.apache.commons.jxpath.ri.compiler.CoreOperationIterator;
import org.apache.commons.jxpath.ri.compiler.CoreOperationSelfContext;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);
        EvalContext context = mock(EvalContext.class);
        Object leftValue = mock(Object.class);
        Object rightValue = mock(Object.class);

        when(leftValue.equals(rightValue)).thenReturn(false);

        when(expression.compute(leftValue, rightValue)).thenCallRealMethod();

        when(context.getSingleNodePointer()).thenReturn(leftValue);
        when(expression.computeValue(context)).thenCallRealMethod();

        Assert.assertFalse(((Boolean) expression.computeValue(context)).booleanValue());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        int precedence = expression.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        boolean symmetric = expression.isSymmetric();

        Assert.assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsGreaterThanRight() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Assert.assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsLessThanRight() {
        CoreOperationRelationalExpression expression = new CoreRelationLessThan(new CoreOperationArgument[2]);

        Assert.assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testEvaluateCompare_WhenLeftIsEqualToRight() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThanOrEqual(new CoreOperationArgument[2]);

        Assert.assertTrue(expression.evaluateCompare(0));
    }

    @Test(expected = AssertionError.class)
    public void testEvaluateCompare_WhenCompareIsInvalid() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        expression.evaluateCompare(2);
    }

    @Test
    public void testCompute_WhenBothValuesAreIterators() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Iterator leftIterator = mock(Iterator.class);
        Iterator rightIterator = mock(Iterator.class);

        when(leftIterator.hasNext()).thenReturn(true);
        when(rightIterator.hasNext()).thenReturn(false);

        Assert.assertFalse(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testCompute_WhenLeftIteratorContainsMatchingValue() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Iterator leftIterator = createMockIteratorWithValues(1, 2, 3);
        Iterator rightIterator = createMockIteratorWithValues(3);

        Assert.assertTrue(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testCompute_WhenRightIteratorContainsMatchingValue() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Iterator leftIterator = createMockIteratorWithValues(1, 2, 3);
        Iterator rightIterator = createMockIteratorWithValues(2, 3);

        Assert.assertFalse(expression.compute(leftIterator, rightIterator));
    }

    @Test
    public void testCompute_WhenBothValuesAreCollections() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Collection leftCollection = new ArrayList();
        Collection rightCollection = new ArrayList();

        leftCollection.add(1);
        leftCollection.add(2);
        leftCollection.add(3);

        rightCollection.add(3);
        rightCollection.add(2);

        Assert.assertFalse(expression.compute(leftCollection.iterator(), rightCollection.iterator()));
    }

    @Test
    public void testCompute_WhenLeftCollectionContainsMatchingValue() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Collection leftCollection = new ArrayList();
        Collection rightCollection = new ArrayList();

        leftCollection.add(3);
        leftCollection.add(2);
        leftCollection.add(1);

        Assert.assertTrue(expression.compute(leftCollection.iterator(), rightCollection.iterator()));
    }

    @Test
    public void testCompute_WhenRightCollectionContainsMatchingValue() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThanOrEqual(new CoreOperationArgument[2]);

        Collection leftCollection = new ArrayList();
        Collection rightCollection = new ArrayList();

        leftCollection.add(3);
        leftCollection.add(2);
        leftCollection.add(1);

        rightCollection.add(3);
        rightCollection.add(2);

        Assert.assertTrue(expression.compute(leftCollection.iterator(), rightCollection.iterator()));
    }

    @Test
    public void testCompute_WhenBothValuesAreSelfContexts() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        EvalContext leftContext = mock(EvalContext.class);
        EvalContext rightContext = mock(EvalContext.class);

        CoreOperationSelfContext leftSelfContext = new CoreOperationSelfContext(mock(InfoSetUtil.class), 0);
        CoreOperationSelfContext rightSelfContext = new CoreOperationSelfContext(mock(InfoSetUtil.class), 0);

        when(leftContext.getSingleNodePointer()).thenReturn(leftSelfContext);
        when(rightContext.getSingleNodePointer()).thenReturn(rightSelfContext);

        Assert.assertFalse(expression.compute(leftContext, rightContext));
    }

    @Test
    public void testCompute_WhenBothValuesAreInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        EvalContext leftContext = mock(EvalContext.class);
        EvalContext rightContext = mock(EvalContext.class);

        InitialContext leftInitialContext = mock(InitialContext.class);
        InitialContext rightInitialContext = mock(InitialContext.class);

        when(leftContext.getSingleNodePointer()).thenReturn(leftInitialContext);
        when(rightContext.getSingleNodePointer()).thenReturn(rightInitialContext);

        Assert.assertFalse(expression.compute(leftContext, rightContext));
    }

    @Test
    public void testCompute_WhenBothValuesAreConstants() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Object leftConstant = mock(CoreOperationConstant.class);
        Object rightConstant = mock(CoreOperationConstant.class);

        when(leftConstant.doubleValue()).thenReturn(1.0);
        when(rightConstant.doubleValue()).thenReturn(2.0);

        Assert.assertFalse(expression.compute(leftConstant, rightConstant));
    }

    @Test
    public void testCompute_WhenLeftIsGreaterThanRight() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Object left = 1.0;
        Object right = 0.5;

        Assert.assertTrue(expression.compute(left, right));
    }

    @Test
    public void testCompute_WhenLeftIsLessThanRight() {
        CoreOperationRelationalExpression expression = new CoreRelationLessThan(new CoreOperationArgument[2]);

        Object left = 0.5;
        Object right = 1.0;

        Assert.assertTrue(expression.compute(left, right));
    }

    @Test
    public void testCompute_WhenLeftIsEqualToRight() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThanOrEqual(new CoreOperationArgument[2]);

        Object left = 0.5;
        Object right = 0.5;

        Assert.assertTrue(expression.compute(left, right));
    }

    @Test
    public void testCompute_WhenBothValuesAreNaN() {
        CoreOperationRelationalExpression expression = new CoreRelationGreaterThan(new CoreOperationArgument[2]);

        Object left = Double.NaN;
        Object right = Double.NaN;

        Assert.assertFalse(expression.compute(left, right));
    }

    private Iterator createMockIteratorWithValues(Object... values) {
        Iterator iterator = mock(Iterator.class);

        when(iterator.hasNext()).thenReturn(true);

        for (Object value : values) {
            when(iterator.next()).thenReturn(value);
        }

        return iterator;
    }
}