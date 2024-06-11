import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Create test objects
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationCompare testCompare = new MockCoreOperationCompare(arg1, arg2);
        EvalContext context = new MockEvalContext();

        // Expected result is true
        boolean result = testCompare.equal(context, arg1, arg2);

        assertTrue(result);
    }

    @Test
    public void testContains() {
        // Create test objects
        Iterator it = new MockIterator();
        Object value = new Object();
        CoreOperationCompare testCompare = new MockCoreOperationCompare(new MockExpression(), new MockExpression());

        // Expected result is false
        boolean result = testCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Create test objects
        Iterator lit = new MockIterator();
        Iterator rit = new MockIterator();
        CoreOperationCompare testCompare = new MockCoreOperationCompare(new MockExpression(), new MockExpression());

        // Expected result is false
        boolean result = testCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare testCompare = new MockCoreOperationCompare(new MockExpression(), new MockExpression());

        // Expected result is true for equal values
        assertTrue(testCompare.equal("test", "test"));
        assertFalse(testCompare.equal("test1", "test2"));
    }

}