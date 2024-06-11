import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual_WhenBothInputsAreNull_ExpectTrue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        boolean result = operation.equal(context, null, null);
        assertTrue(result);
    }

    @Test
    public void testEqual_WhenLeftInputIsNull_ExpectFalse() {
        CoreOperationCompare operation = new CoreOperationCompare(null, new Expression());
        EvalContext context = new EvalContext();
        boolean result = operation.equal(context, null, new Expression());
        assertFalse(result);
    }

    @Test
    public void testEqual_WhenRightInputIsNull_ExpectFalse() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), null);
        EvalContext context = new EvalContext();
        boolean result = operation.equal(context, new Expression(), null);
        assertFalse(result);
    }

    @Test
    public void testContains_WhenIteratorContainsValue_ExpectTrue() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = Arrays.asList("a", "b", "c").iterator();
        boolean result = operation.contains(it, "b");
        assertTrue(result);
    }

    @Test
    public void testContains_WhenIteratorDoesNotContainValue_ExpectFalse() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = Arrays.asList("x", "y", "z").iterator();
        boolean result = operation.contains(it, "a");
        assertFalse(result);
    }

    @Test
    public void testFindMatch_WhenMatchExistsBetweenIterators_ExpectTrue() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = Arrays.asList("alpha", "beta", "gamma").iterator();
        Iterator rit = Arrays.asList("delta", "gamma").iterator();
        boolean result = operation.findMatch(lit, rit);
        assertTrue(result);
    }

    @Test
    public void testFindMatch_WhenMatchDoesNotExistBetweenIterators_ExpectFalse() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = Arrays.asList("apple", "banana", "cherry").iterator();
        Iterator rit = Arrays.asList("date", "fig").iterator();
        boolean result = operation.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqual_WhenValuesAreEqual_ExpectTrue() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        boolean result = operation.equal("abc", "abc");
        assertTrue(result);
    }

    @Test
    public void testEqual_WhenValuesAreNotEqual_ExpectFalse() {
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        boolean result = operation.equal("123", "456");
        assertFalse(result);
    }
}