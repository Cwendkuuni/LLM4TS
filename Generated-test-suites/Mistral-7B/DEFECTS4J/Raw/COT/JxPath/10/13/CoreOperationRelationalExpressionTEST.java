import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
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
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(1), new ConstantExpression(2));
        EvalContext context = mock(EvalContext.class);

        when(context.getSingleNodePointer()).thenReturn(1);
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        when(context.getSingleNodePointer()).thenReturn(2);
        result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(mock(Expression.class), mock(Expression.class));

        int precedence = expression.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(mock(Expression.class), mock(Expression.class));

        boolean symmetric = expression.isSymmetric();

        Assert.assertEquals(false, symmetric);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEvaluateCompareNegative() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(mock(Expression.class), mock(Expression.class));

        expression.evaluateCompare(-1);
    }

    @Test
    public void testEvaluateCompareEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(mock(Expression.class), mock(Expression.class));

        boolean result = expression.evaluateCompare(0);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testEvaluateCompareGreater() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(mock(Expression.class), mock(Expression.class));

        boolean result = expression.evaluateCompare(1);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testEvaluateCompareLess() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThan(mock(Expression.class), mock(Expression.class));

        boolean result = expression.evaluateCompare(-1);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testEvaluateCompareLessOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationLessThanOrEqual(mock(Expression.class), mock(Expression.class));

        boolean result = expression.evaluateCompare(0);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testComputeWithInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(mock(Expression.class), mock(Expression.class));

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(mock(InitialContext.class));

        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeWithIterators() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(mock(Expression.class), mock(Expression.class));

        Collection collection1 = new ArrayList();
        collection1.add(1);
        collection1.add(2);
        Iterator iterator1 = collection1.iterator();

        Collection collection2 = new ArrayList();
        collection2.add(2);
        collection2.add(3);
        Iterator iterator2 = collection2.iterator();

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(iterator1);

        boolean result = expression.compute(iterator1, iterator2);

        Assert.assertEquals(false, result);

        when(context.getSingleNodePointer()).thenReturn(iterator2);

        result = expression.compute(iterator1, iterator2);

        Assert.assertEquals(false, result);

        when(context.getSingleNodePointer()).thenReturn(iterator1);

        result = expression.compute(iterator2, iterator1);

        Assert.assertEquals(false, result);

        when(context.getSingleNodePointer()).thenReturn(iterator2);

        result = expression.compute(iterator1, iterator2);

        Assert.assertEquals(false, result);

        when(context.getSingleNodePointer()).thenReturn(iterator1);

        result = expression.compute(iterator2, iterator1);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testComputeWithDoubleValues() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(mock(Expression.class), mock(Expression.class));

        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(mock(Double.class, 1.0));

        double result = (Double) expression.computeValue(context);

        Assert.assertEquals(1.0, result, 0.001);
    }
}