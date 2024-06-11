import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test for when both values are equal
        CoreOperationCompare coreOperation = new CoreOperationCompare(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        boolean result = coreOperation.equal(context, new Expression(), new Expression());
        assertTrue(result);

        // Test for when both values are not equal
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(5); // Setting a value for comparison
        arg2.setValue(10); // Setting a different value for comparison
        CoreOperationCompare coreOperation2 = new CoreOperationCompare(arg1, arg2);
        EvalContext context2 = new EvalContext();
        boolean result2 = coreOperation2.equal(context2, arg1, arg2);
        assertFalse(result2);
    }

    @Test
    public void testContains() {
        // Test for when the iterator contains the value
        CoreOperationCompare coreOperation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        boolean result = coreOperation.contains(it, 2);
        assertTrue(result);

        // Test for when the iterator does not contain the value
        Iterator it2 = Arrays.asList("a", "b", "c").iterator();
        boolean result2 = coreOperation.contains(it2, "d");
        assertFalse(result2);
    }

    @Test
    public void testFindMatch() {
        // Test for when a match is found between two iterators
        CoreOperationCompare coreOperation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(3, 4, 5).iterator();
        boolean result = coreOperation.findMatch(lit, rit);
        assertTrue(result);

        // Test for when a match is not found between two iterators
        Iterator lit2 = Arrays.asList("a", "b", "c").iterator();
        Iterator rit2 = Arrays.asList("x", "y", "z").iterator();
        boolean result2 = coreOperation.findMatch(lit2, rit2);
        assertFalse(result2);
    }

    @Test
    public void testEqualValues() {
        // Test for equal values
        CoreOperationCompare coreOperation = new CoreOperationCompare(new Expression(), new Expression());
        boolean result = coreOperation.equal("test", "test");
        assertTrue(result);

        // Test for not equal values
        boolean result2 = coreOperation.equal("test1", "test2");
        assertFalse(result2);
    }
}