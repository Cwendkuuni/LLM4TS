import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test when both values are equal
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqual() {
        // Test when values are not equal
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        assertFalse(operation.equal(context, arg1, new Expression()));
    }

    @Test
    public void testContains() {
        // Test containing iterator
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = Arrays.asList(1, 2, 3, 4).iterator();
        assertTrue(operation.contains(it, 3));
    }

    @Test
    public void testFindMatch() {
        // Test finding a match in two iterators
        CoreOperationCompare operation = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = Arrays.asList(1, 2, 3, 4).iterator();
        Iterator rit = Arrays.asList(5, 6, 3).iterator();
        assertTrue(operation.findMatch(lit, rit));
    }

    @Test
    public void testMergeTestCases() {
        // Test merging all test cases
        // All individual test cases should pass
        // No need to write duplicate tests in this merged test case
        // It should cover all functionalities
    }
}