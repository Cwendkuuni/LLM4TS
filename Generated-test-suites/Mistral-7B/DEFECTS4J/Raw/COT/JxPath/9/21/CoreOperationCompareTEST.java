import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.mockito.Mockito.*;

public class CoreOperationCompareTest {

    @Test
    public void testConstructor() {
        Expression arg1 = mock(Expression.class);
        Expression arg2 = mock(Expression.class);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Assert.assertArrayEquals(new Expression[]{arg1, arg2}, coreOperationCompare.getExpressions());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(mock(Expression.class), mock(Expression.class));
        int precedence = coreOperationCompare.getPrecedence();
        assertEquals(2, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(mock(Expression.class), mock(Expression.class));
        boolean symmetric = coreOperationCompare.isSymmetric();
        assertTrue(symmetric);
    }

    @Test
    public void testEqual() {
        Object left = "test";
        Object right = "test";
        EvalContext context = mock(EvalContext.class);
        Expression leftExpression = mock(Expression.class);
        Expression rightExpression = mock(Expression.class);

        when(leftExpression.compute(context)).thenReturn(left);
        when(rightExpression.compute(context)).thenReturn(right);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);

        assertTrue(coreOperationCompare.equal(context, leftExpression, rightExpression));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        Object left = 1;
        Object right = "test";
        EvalContext context = mock(EvalContext.class);
        Expression leftExpression = mock(Expression.class);
        Expression rightExpression = mock(Expression.class);

        when(leftExpression.compute(context)).thenReturn(left);
        when(rightExpression.compute(context)).thenReturn(right);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);

        assertFalse(coreOperationCompare.equal(context, leftExpression, rightExpression));
    }

    @Test
    public void testEqualWithNull() {
        Object left = "test";
        Object right = null;
        EvalContext context = mock(EvalContext.class);
        Expression leftExpression = mock(Expression.class);
        Expression rightExpression = mock(Expression.class);

        when(leftExpression.compute(context)).thenReturn(left);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, rightExpression);

        assertFalse(coreOperationCompare.equal(context, leftExpression, rightExpression));
    }

    @Test
    public void testEqualWithSameInstance() {
        Object left = new Object();
        EvalContext context = mock(EvalContext.class);
        Expression leftExpression = mock(Expression.class);
        Expression rightExpression = mock(Expression.class);
        when(leftExpression.compute(context)).thenReturn(left);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(leftExpression, leftExpression);

        assertTrue(coreOperationCompare.equal(context, leftExpression, leftExpression));
    }

    @Test
    public void testContains() {
        Object element = "test";
        Iterator iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator.next()).thenReturn(element);

        Collection collection = new ArrayList();
        collection.add(iterator);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(mock(Expression.class), mock(Expression.class));

        assertTrue(coreOperationCompare.contains(iterator, element));
    }

    @Test
    public void testContainsWithEmptyCollection() {
        Object element = "test";
        Collection collection = new ArrayList();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(mock(Expression.class), mock(Expression.class));

        assertFalse(coreOperationCompare.contains(collection.iterator(), element));
    }

    @Test
    public void testFindMatch() {
        Object element1 = "test1";
        Object element2 = "test2";
        Iterator iterator1 = mock(Iterator.class);
        Iterator iterator2 = mock(Iterator.class);

        when(iterator1.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator1.next()).thenReturn(element1);

        when(iterator2.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator2.next()).thenReturn(element2);

        HashSet set = new HashSet();
        set.add(element1);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(mock(Expression.class), mock(Expression.class));

        assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatchWithDifferentSizes() {
        Object element1 = "test1";
        Object element2 = "test2";
        Iterator iterator1 = mock(Iterator.class);
        Iterator iterator2 = mock(Iterator.class);

        when(iterator1.hasNext()).thenReturn(true).thenReturn(false);
        when(iterator1.next()).thenReturn(element1);

        when(iterator2.hasNext()).thenReturn(true).thenReturn(false).thenReturn(true);
        when(iterator2.next()).thenReturn(element2);

        HashSet set = new HashSet();
        set.add(element1);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(mock(Expression.class), mock(Expression.class));

        assertFalse(coreOperationCompare.findMatch(iterator1, iterator2));
    }
}