
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = {mock(Expression.class), mock(Expression.class)};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        EvalContext context = mock(EvalContext.class);
        Object result = coreOperationRelationalExpression.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testReduceWithSelfContext() {
        Expression[] args = {mock(Expression.class), mock(Expression.class)};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        SelfContext selfContext = mock(SelfContext.class);
        EvalContext context = mock(EvalContext.class);
        when(context.getSingleNodePointer()).thenReturn(selfContext);
        Object result = coreOperationRelationalExpression.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testContainsMatchWithIterator() {
        Expression[] args = {mock(Expression.class), mock(Expression.class)};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        Iterator iterator = mock(Iterator.class);
        Object value = new Object();
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(value);
        boolean result = coreOperationRelationalExpression.containsMatch(iterator, value);
        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatchWithIterators() {
        Expression[] args = {mock(Expression.class), mock(Expression.class)};
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        Iterator lit = mock(Iterator.class);
        Iterator rit = mock(Iterator.class);
        when(lit.hasNext()).thenReturn(true, false);
        when(lit.next()).thenReturn(new Object());
        when(rit.hasNext()).thenReturn(true, false);
        when(rit.next()).thenReturn(new Object());
        boolean result = coreOperationRelationalExpression.findMatch(lit, rit);
        Assert.assertFalse(result);
    }

}
