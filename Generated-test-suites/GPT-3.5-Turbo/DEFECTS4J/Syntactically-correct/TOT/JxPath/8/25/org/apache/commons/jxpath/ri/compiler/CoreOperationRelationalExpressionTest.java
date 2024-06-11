import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression operation;

    @Before
    public void setup() {
        operation = new CoreOperationRelationalExpressionMock(new Expression[2]);
    }

    // Test computeValue() method

    @Test
    public void testComputeValue_LeftValueLessThanRightValue() {
        EvalContext context = new EvalContext();
        context.setNodeSetIterator(getIterator(createList(1), false));

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftValueGreaterThanRightValue() {
        EvalContext context = new EvalContext();
        context.setNodeSetIterator(getIterator(createList(1), true));

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LeftValueEqualToRightValue() {
        EvalContext context = new EvalContext();
        context.setNodeSetIterator(getIterator(createList(1), true));

        Object result = operation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    // Test reduce() method

    @Test
    public void testReduce_InputSelfContext_ReturnSingleNodePointer() {
        EvalContext context = new EvalContext();
        SelfContext selfContext = new SelfContext(context);
        Object result = operation.reduce(selfContext);

        assertSame(context.getSingleNodePointer(), result);
    }

    @Test
    public void testReduce_InputCollection_ReturnIterator() {
        List<Integer> list = createList(1, 2, 3);
        Object result = operation.reduce(list);

        assertTrue(result instanceof Iterator);
        Iterator iterator = (Iterator) result;
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testReduce_InputNotReducible_ReturnSameObject() {
        Object obj = new Object();
        Object result = operation.reduce(obj);

        assertSame(obj, result);
    }

    // Test containsMatch() method

    @Test
    public void testContainsMatch_InputIteratorContainsMatch_ReturnTrue() {
        Iterator<Integer> iterator = getIterator(createList(1, 2, 3), false);
        boolean result = operation.containsMatch(iterator, 2);

        assertTrue(result);
    }

    @Test
    public void testContainsMatch_InputIteratorDoesNotContainMatch_ReturnFalse() {
        Iterator<Integer> iterator = getIterator(createList(1, 2, 3), false);
        boolean result = operation.containsMatch(iterator, 4);

        assertFalse(result);
    }

    // Test findMatch() method

    @Test
    public void testFindMatch_InputIteratorContainsMatch_ReturnTrue() {
        Iterator<Integer> leftIterator = getIterator(createList(1, 2, 3), false);
        Iterator<Integer> rightIterator = getIterator(createList(3, 4, 5), true);
        boolean result = operation.findMatch(leftIterator, rightIterator);

        assertTrue(result);
    }

    @Test
    public void testFindMatch_InputIteratorDoesNotContainMatch_ReturnFalse() {
        Iterator<Integer> leftIterator = getIterator(createList(1, 2, 3), false);
        Iterator<Integer> rightIterator = getIterator(createList(4, 5, 6), true);
        boolean result = operation.findMatch(leftIterator, rightIterator);

        assertFalse(result);
    }

    // Mock class to bypass abstract methods in CoreOperationRelationalExpression

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }

    // Helper methods

    private <T> List<T> createList(T... elements) {
        List<T> list = new ArrayList<>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

    private <T> Iterator<T> getIterator(List<T> list, boolean reset) {
        Iterator<T> iterator = list.iterator();
        if (reset) {
            iterator.next();
        }
        return iterator;
    }
}