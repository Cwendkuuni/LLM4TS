import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        // Test for null values
        EvalContext context = new EvalContext();
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));

        // Test for equal values
        arg1 = new Expression("value");
        arg2 = new Expression("value");
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));

        // Test for different values
        arg2 = new Expression("different");
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        // Test scenario when Iterator has no elements
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = Collections.emptyIterator();
        Object value = new Object();
        assertFalse(coreOperationCompare.contains(it, value));

        // Test scenario when Iterator has the desired value
        List<String> list = Arrays.asList("value");
        it = list.iterator();
        assertTrue(coreOperationCompare.contains(it, "value"));

        // Test scenario when Iterator does not contain the desired value
        assertFalse(coreOperationCompare.contains(it, "different"));
    }

    @Test
    public void testFindMatch() {
        // Test scenario when both Iterators are empty
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = Collections.emptyIterator();
        Iterator rit = Collections.emptyIterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        // Test scenario when both Iterators has a matching value
        List<String> leftList = Arrays.asList("value");
        List<String> rightList = Arrays.asList("value");
        lit = leftList.iterator();
        rit = rightList.iterator();
        assertTrue(coreOperationCompare.findMatch(lit, rit));

        // Test scenario when no matching value is found
        rightList = Arrays.asList("different");
        rit = rightList.iterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());

        // Test scenario when comparing two equal Pointers
        assertTrue(coreOperationCompare.equal(new Pointer(), new Pointer()));

        // Test scenario when comparing two different Pointers
        assertFalse(coreOperationCompare.equal(new Pointer(), new Pointer()));

        // Test scenario when comparing two Booleans
        assertTrue(coreOperationCompare.equal(true, true));

        // Test scenario when comparing two different Booleans
        assertFalse(coreOperationCompare.equal(true, false));

        // Test scenario when comparing two Numbers
        assertTrue(coreOperationCompare.equal(5, 5));

        // Test scenario when comparing two different Numbers
        assertFalse(coreOperationCompare.equal(5, 10));

        // Test scenario when comparing two Strings
        assertTrue(coreOperationCompare.equal("value", "value"));

        // Test scenario when comparing two different Strings
        assertFalse(coreOperationCompare.equal("value", "different"));
    }
}