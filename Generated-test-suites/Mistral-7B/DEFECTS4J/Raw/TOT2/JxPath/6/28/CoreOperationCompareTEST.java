package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTestHelper;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest extends CoreOperationTestHelper {

    @Test
    public void testCoreOperationCompareConstructor() {
        Expression arg1 = new ConstantExpression(1);
        Expression arg2 = new ConstantExpression(2);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testEqualBasicTypes() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(1), new ConstantExpression(1)
        );
        assertTrue(coreOperationCompare.equal(null, null, null));
        assertTrue(coreOperationCompare.equal(new EvalContext(), new ConstantExpression(1), new ConstantExpression(1)));
        // Add more test cases for other basic types
    }

    @Test
    public void testEqualPointerObjects() {
        Pointer l = new Pointer("pointer1");
        Pointer r = new Pointer("pointer1");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(l), new ConstantExpression(r));
        assertTrue(coreOperationCompare.equal(null, null, null));
        assertTrue(coreOperationCompare.equal(new EvalContext(), new ConstantExpression(l), new ConstantExpression(r)));
        // Add more test cases for different Pointer objects
    }

    @Test
    public void testEqualCollectionObjects() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new ArrayListIterator<>(list1)),
            new ConstantExpression(new ArrayListIterator<>(list2))
        );
        assertTrue(coreOperationCompare.equal(null, null, null));
        assertTrue(coreOperationCompare.equal(new EvalContext(), new ConstantExpression(new ArrayListIterator<>(list1)), new ConstantExpression(new ArrayListIterator<>(list2))));
    }

    @Test
    public void testContains() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new ArrayListIterator<>(list)),
            new ConstantExpression(5)
        );
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object value = iterator.next();
            assertFalse(coreOperationCompare.contains(new ArrayListIterator<>(list), null));
            assertFalse(coreOperationCompare.contains(new ArrayListIterator<>(list), new ConstantExpression(null)));
            assertTrue(coreOperationCompare.contains(new ArrayListIterator<>(list), new ConstantExpression(value)));
        }
        assertFalse(coreOperationCompare.contains(new ArrayListIterator<>(list), new ConstantExpression(6)));
    }

    @Test
    public void testFindMatch() {
        List<String> list1 = Arrays.asList("apple", "banana", "cherry");
        List<String> list2 = Arrays.asList("orange", "banana", "cherry");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new ArrayListIterator<>(list1)),
            new ConstantExpression(new ArrayListIterator<>(list2))
        );
        assertTrue(coreOperationCompare.findMatch(new ArrayListIterator<>(list1), new ArrayListIterator<>(list2)));
        // Test with different combinations of lists
    }

    @Test
    public void testEqualDifferentObjects() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(l), new ConstantExpression(r)
        );
        assertFalse(coreOperationCompare.equal(null, null, null));
        assertFalse(coreOperationCompare.equal(new EvalContext(), new ConstantExpression(l), new ConstantExpression(r)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualNullContext() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualNullLeftExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, new ConstantExpression(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualNullRightExpression() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new ConstantExpression(1), null);
    }

    @Test
    public void testEqualEmptyCollections() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new ArrayListIterator<>(Collections.emptyList())),
            new ConstantExpression(new ArrayListIterator<>(Collections.emptyList()))
        );
        assertTrue(coreOperationCompare.equal(null, null, null));
        assertTrue(coreOperationCompare.equal(new EvalContext(), new ConstantExpression(new ArrayListIterator<>(Collections.emptyList())), new ConstantExpression(new ArrayListIterator<>(Collections.emptyList()))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMatchMismatchedIteratorTypes() {
        List<String> list1 = Arrays.asList("apple", "banana", "cherry");
        Iterator<Integer> iterator = new ArrayListIterator<>(list1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(iterator),
            new ConstantExpression(new ArrayListIterator<>(Collections.emptyList()))
        );
    }

    @Test
    public void testContainsEmptyIterators() {
        Iterator<Object> iterator1 = Collections.emptyIterator();
        Iterator<Object> iterator2 = Collections.emptyIterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(iterator1),
            new ConstantExpression(iterator2)
        );
        assertFalse(coreOperationCompare.contains(iterator1, null));
        assertFalse(coreOperationCompare.contains(iterator2, null));
    }

    @Test
    public void testFindMatchNonMatchingCollections() {
        List<String> list1 = Arrays.asList("apple", "banana", "cherry");
        List<String> list2 = Arrays.asList("orange", "grape", "cherry");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new ArrayListIterator<>(list1)),
            new ConstantExpression(new ArrayListIterator<>(list2))
        );
        assertFalse(coreOperationCompare.findMatch(new ArrayListIterator<>(list1), new ArrayListIterator<>(list2)));
    }
}