import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
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
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{arg1, arg2});

        Object leftValue = mock(Object.class);
        Object rightValue = mock(Object.class);
        EvalContext context = mock(EvalContext.class);

        when(arg1.computeValue(context)).thenReturn(leftValue);
        when(arg2.computeValue(context)).thenReturn(rightValue);

        Boolean result = expression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        leftValue = Boolean.TRUE;
        rightValue = Boolean.FALSE;
        result = expression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        Assert.assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        Assert.assertFalse(expression.isSymmetric());
    }

    @Test(expected = AssertionError.class)
    public void testEvaluateCompareNegativeCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        expression.evaluateCompare(-1);
    }

    @Test
    public void testEvaluateCompareZeroCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        expression.evaluateCompare(0);
        Assert.assertTrue(expression.evaluateCompare(0) == expression.evaluateCompare(0));
    }

    @Test
    public void testEvaluateComparePositiveCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[0]);
        expression.evaluateCompare(1);
        Assert.assertTrue(expression.evaluateCompare(1) != expression.evaluateCompare(1));
    }

    @Test
    public void testReduceNull() {
        Object o = null;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{new SelfContext()});
        Object result = expression.reduce(o);
        Assert.assertEquals(new SelfContext(), result);
    }

    @Test
    public void testReduceInitialContext() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{new InitialContext()});
        Object result = expression.reduce(new InitialContext());
        Assert.assertEquals(new InitialContext(), result);
    }

    @Test
    public void testReduceCollection() {
        Collection collection = new ArrayList();
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{new Iterator() {
            @Override
            public boolean hasNext() {
                return collection.size() > 0;
            }

            @Override
            public Object next() {
                return collection.remove(0);
            }
        }, new SelfContext()});
        Object result = expression.reduce(collection.iterator());
        Assert.assertEquals(collection.size() > 0 ? collection.remove(0) : null, result);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = mock(Iterator.class);
        Object value = mock(Object.class);

        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(new Object());

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{iterator});
        boolean result = expression.containsMatch(iterator, value);
        Assert.assertFalse(result);

        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(value);

        result = expression.containsMatch(iterator, value);
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        Iterator iter1 = mock(Iterator.class);
        Iterator iter2 = mock(Iterator.class);

        Object value = mock(Object.class);
        when(iter1.hasNext()).thenReturn(true);
        when(iter1.next()).thenReturn(new Object());

        when(iter2.hasNext()).thenReturn(true);
        when(iter2.next()).thenReturn(value);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new Expression[]{iter1, iter2});
        boolean result = expression.findMatch(iter1, iter2);
        Assert.assertFalse(result);

        when(iter1.hasNext()).thenReturn(false);
        when(iter1.next()).thenReturn(value);

        result = expression.findMatch(iter1, iter2);
        Assert.assertTrue(result);
    }

}