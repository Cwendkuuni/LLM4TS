import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);

        // Test case 1: arg1 and arg2 are equal
        EvalContext context1 = new EvalContext();
        context1.setSingleNodePointer(new Pointer());
        arg1.compute(context1);
        arg2.compute(context1);
        assertTrue(operation.equal(context1, arg1, arg2));

        // Test case 2: arg1 and arg2 are not equal
        EvalContext context2 = new EvalContext();
        context2.setSingleNodePointer(new Pointer());
        arg1.compute(context2);
        assertFalse(operation.equal(context2, arg1, arg2));

        // Test case 3: arg1 and arg2 are null
        EvalContext context3 = new EvalContext();
        context3.setSingleNodePointer(new Pointer());
        arg1.compute(context3);
        arg2.compute(context3);
        assertTrue(operation.equal(context3, arg1, arg2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare();

        // Test case 1: Iterator contains the value
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Integer value1 = 2;
        assertTrue(operation.contains(iterator1, value1));

        // Test case 2: Iterator does not contain the value
        Iterator<Integer> iterator2 = Arrays.asList(1, 2, 3).iterator();
        Integer value2 = 4;
        assertFalse(operation.contains(iterator2, value2));

        // Test case 3: Empty iterator
        Iterator<Integer> iterator3 = Collections.emptyIterator();
        Integer value3 = 1;
        assertFalse(operation.contains(iterator3, value3));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare();

        // Test case 1: Iterators have a common element
        Iterator<Integer> lit1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> rit1 = Arrays.asList(3, 4, 5).iterator();
        assertTrue(operation.findMatch(lit1, rit1));

        // Test case 2: Iterators do not have a common element
        Iterator<Integer> lit2 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> rit2 = Arrays.asList(4, 5, 6).iterator();
        assertFalse(operation.findMatch(lit2, rit2));

        // Test case 3: One iterator is empty
        Iterator<Integer> lit3 = Collections.emptyIterator();
        Iterator<Integer> rit3 = Arrays.asList(1, 2, 3).iterator();
        assertFalse(operation.findMatch(lit3, rit3));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare();

        // Test case 1: Values are equal
        Object l1 = 1;
        Object r1 = 1;
        assertTrue(operation.equal(l1, r1));

        // Test case 2: Values are not equal
        Object l2 = 1;
        Object r2 = 2;
        assertFalse(operation.equal(l2, r2));

        // Test case 3: Values are null
        Object l3 = null;
        Object r3 = null;
        assertTrue(operation.equal(l3, r3));
    }

}