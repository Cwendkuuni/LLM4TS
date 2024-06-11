import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        // Test typical use case
        assertTrue(coreOperationCompare.equal(new EvalContext(), arg1, arg2));

        // Test edge case - both null
        assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));

        // Test edge case - different types
        assertFalse(coreOperationCompare.equal(new EvalContext(), "string", 123));
    }

    @Test
    public void testContains() {
        Expression arg1 = new MockExpression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg1);

        // Test typical use case
        Iterator it = new MockIterator();
        assertTrue(coreOperationCompare.contains(it, "value"));

        // Test edge case - iterator is empty
        Iterator emptyIt = new MockIterator();
        assertFalse(coreOperationCompare.contains(emptyIt, "value"));
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new MockExpression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg1);

        // Test typical use case
        Iterator lit = new MockIterator();
        Iterator rit = new MockIterator();
        assertTrue(coreOperationCompare.findMatch(lit, rit));

        // Test edge case - no match found
        Iterator emptyIt = new MockIterator();
        Iterator singleIt = new MockIterator();
        assertFalse(coreOperationCompare.findMatch(emptyIt, singleIt));
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test typical use case
        assertTrue(coreOperationCompare.equal("value", "value"));

        // Test edge case - null values
        assertTrue(coreOperationCompare.equal(null, null));

        // Test error scenario - different values
        assertFalse(coreOperationCompare.equal("value1", "value2"));
    }

}