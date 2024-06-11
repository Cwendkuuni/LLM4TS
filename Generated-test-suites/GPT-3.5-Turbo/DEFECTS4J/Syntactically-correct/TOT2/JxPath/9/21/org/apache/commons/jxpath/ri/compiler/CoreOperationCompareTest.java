import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        // Initialize CoreOperationCompare instance for testing
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        // Test equal method with different data types
        EvalContext context = new EvalContext();
        assertTrue(coreOperationCompare.equal(context, new DummyExpression(), new DummyExpression()));
    }

    @Test
    public void testContains() {
        // Test contains method with Iterator and Object
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        Iterator<String> iterator = list.iterator();
        assertTrue(coreOperationCompare.contains(iterator, "A"));
    }

    @Test
    public void testFindMatch() {
        // Test findMatch method with two Iterators
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        assertFalse(coreOperationCompare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testEqualDifferentDataTypes() {
        // Test equal method with different data types (Boolean)
        assertTrue(coreOperationCompare.equal(true, true));
    }

    @Test
    public void testEqualNull() {
        // Test equal method with null values
        assertFalse(coreOperationCompare.equal(null, "test"));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testNullEvalContext() {
        // Test equal method with null EvalContext
        assertNull(coreOperationCompare.equal(null, new DummyExpression(), new DummyExpression()));
    }

    @Test
    public void testNullExpressions() {
        // Test equal method with null Expressions
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        // Test unsupported operation
        coreOperationCompare.contains(new DummyIterator(), new Object());
    }

    // Dummy classes used for testing

    private static class DummyExpression implements Expression {
        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class DummyIterator implements Iterator {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }
}