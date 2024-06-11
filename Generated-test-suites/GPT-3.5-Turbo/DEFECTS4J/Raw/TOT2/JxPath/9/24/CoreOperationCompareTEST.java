import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test when two values are equal
        Expression arg1 = new Expression("value1");
        Expression arg2 = new Expression("value1");
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        assertTrue(compare.equal(context, arg1, arg2));
    }

    @Test
    public void testNotEqual() {
        // Test when two values are not equal
        Expression arg1 = new Expression("value1");
        Expression arg2 = new Expression("value2");
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        assertFalse(compare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        // Test contains method with a collection and a value
        Iterator iterator = new HashSet<String>().iterator();
        iterator.append("value1");
        iterator.append("value2");

        Object value = "value2";

        CoreOperationCompare compare = new CoreOperationCompare();
        assertTrue(compare.contains(iterator, value));
    }

    @Test
    public void testFindMatch() {
        // Test findMatch method with two sets of values
        Iterator leftIterator = new HashSet<String>().iterator();
        leftIterator.append("value1");
        leftIterator.append("value2");

        Iterator rightIterator = new HashSet<String>().iterator();
        rightIterator.append("value2");
        rightIterator.append("value3");

        CoreOperationCompare compare = new CoreOperationCompare();
        assertTrue(compare.findMatch(leftIterator, rightIterator));
    }
}