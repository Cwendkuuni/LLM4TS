import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqualMethod() {
        Expression arg1 = new FakeExpression();
        Expression arg2 = new FakeExpression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = coreOperationCompare.equal(context, arg1, arg2);

        assertTrue(result);
    }

    @Test
    public void testContainsMethod() {
        Iterator it = new FakeIterator();
        Object value = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new FakeExpression(), new FakeExpression());

        boolean result = coreOperationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatchMethod() {
        Iterator lit = new FakeIterator();
        Iterator rit = new FakeIterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new FakeExpression(), new FakeExpression());

        boolean result = coreOperationCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualObjectsMethod() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new FakeExpression(), new FakeExpression());

        boolean result = coreOperationCompare.equal(obj1, obj2);

        assertFalse(result);
    }

    // Additional test cases for edge cases and exceptions can be added here

}