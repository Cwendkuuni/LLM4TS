import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

    @InjectMocks
    private CoreOperationCompare coreOperationCompare;

    @Test
    public void testEqual() {
        Object leftValue = 5;
        Object rightValue = 5;
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Expression leftExpression = Mockito.mock(Expression.class);
        Expression rightExpression = Mockito.mock(Expression.class);
        Mockito.when(leftExpression.compute(context)).thenReturn(leftValue);
        Mockito.when(rightExpression.compute(context)).thenReturn(rightValue);

        assertTrue(coreOperationCompare.equal(context, leftExpression, rightExpression));
    }

    @Test
    public void testNotEqual() {
        Object leftValue = 5;
        Object rightValue = 6;
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Expression leftExpression = Mockito.mock(Expression.class);
        Expression rightExpression = Mockito.mock(Expression.class);
        Mockito.when(leftExpression.compute(context)).thenReturn(leftValue);
        Mockito.when(rightExpression.compute(context)).thenReturn(rightValue);

        assertFalse(coreOperationCompare.equal(context, leftExpression, rightExpression));
    }

    @Test
    public void testEqualNull() {
        Object leftValue = null;
        Object rightValue = null;
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Expression leftExpression = Mockito.mock(Expression.class);
        Expression rightExpression = Mockito.mock(Expression.class);
        Mockito.when(leftExpression.compute(context)).thenReturn(leftValue);
        Mockito.when(rightExpression.compute(context)).thenReturn(rightValue);

        assertTrue(coreOperationCompare.equal(context, leftExpression, rightExpression));
    }

    @Test(expected = EvaluationException.class)
    public void testErrorEvaluation() {
        Object leftValue = 5;
        Object rightValue = null;
        JXPathContext context = Mockito.mock(JXPathContext.class);
        Expression leftExpression = Mockito.mock(Expression.class);
        Expression rightExpression = Mockito.mock(Expression.class);
        Mockito.when(leftExpression.compute(context)).thenReturn(leftValue);

        coreOperationCompare.equal(context, leftExpression, rightExpression);
    }

    @Test
    public void testIteratorContains() {
        Integer[] array = {1, 2, 3, 4, 5};
        Iterator iterator = Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(1).thenReturn(2).thenReturn(3);

        assertTrue(coreOperationCompare.contains(iterator, 3));
    }

    @Test
    public void testIteratorNotContains() {
        Integer[] array = {1, 2, 3, 4, 5};
        Iterator iterator = Mockito.mock(Iterator.class);
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true)
                .thenReturn(true).thenReturn(false);
        Mockito.when(iterator.next()).thenReturn(1).thenReturn(2).thenReturn(3)
                .thenReturn(3).thenReturn(4);

        assertFalse(coreOperationCompare.contains(iterator, 6));
    }

    @Test
    public void testIteratorsMatch() {
        Integer[] leftArray = {1, 2, 3};
        Integer[] rightArray = {1, 2, 3};
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        Mockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(true)
                .thenReturn(true);
        Mockito.when(leftIterator.next()).thenReturn(1).thenReturn(2).thenReturn(3);
        Mockito.when(rightIterator.hasNext()).thenReturn(true).thenReturn(true)
                .thenReturn(true);
        Mockito.when(rightIterator.next()).thenReturn(1).thenReturn(2).thenReturn(3);

        assertTrue(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }

    @Test
    public void testIteratorsNotMatch() {
        Integer[] leftArray = {1, 2, 3};
        Integer[] rightArray = {2, 3, 4};
        Iterator leftIterator = Mockito.mock(Iterator.class);
        Iterator rightIterator = Mockito.mock(Iterator.class);

        Mockito.when(leftIterator.hasNext()).thenReturn(true).thenReturn(true)
                .thenReturn(true);
        Mockito.when(leftIterator.next()).thenReturn(1).thenReturn(2).thenReturn(3);
        Mockito.when(rightIterator.hasNext()).thenReturn(true).thenReturn(true)
                .thenReturn(true);
        Mockito.when(rightIterator.next()).thenReturn(2).thenReturn(3).thenReturn(4);

        assertFalse(coreOperationCompare.findMatch(leftIterator, rightIterator));
    }
}