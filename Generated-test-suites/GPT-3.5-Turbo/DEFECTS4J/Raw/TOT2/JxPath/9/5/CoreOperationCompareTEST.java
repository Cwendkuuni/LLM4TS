import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Test for equal method
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        EvalContext context = new EvalContext();
        boolean result = operation.equal(context, expression1, expression2);
        assertTrue(result);
    }

    @Test
    public void testContains() {
        // Test for contains method
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        Iterator it = expression1.getIterator();
        Object value = expression2.compute(new EvalContext());
        boolean result = operation.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Test for findMatch method
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        Iterator lit = expression1.getIterator();
        Iterator rit = expression2.getIterator();
        boolean result = operation.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqualPointers() {
        // Test for equal method with Pointers
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();
        pointer1.setValue("value");
        pointer2.setValue("value");
        
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        boolean result = operation.equal(pointer1, pointer2);
        assertTrue(result);
    }

    @Test
    public void testEqualNumbers() {
        // Test for equal method with Numbers
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        boolean result = operation.equal(10, 10.0);
        assertTrue(result);
    }

    @Test
    public void testEqualStrings() {
        // Test for equal method with Strings
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        boolean result = operation.equal("test", "test");
        assertTrue(result);
    }

    @Test
    public void testEqualNull() {
        // Test for equal method with null values
        CoreOperationCompare operation = new CoreOperationCompare(expression1, expression2);
        boolean result = operation.equal(null, null);
        assertTrue(result);
    }
}