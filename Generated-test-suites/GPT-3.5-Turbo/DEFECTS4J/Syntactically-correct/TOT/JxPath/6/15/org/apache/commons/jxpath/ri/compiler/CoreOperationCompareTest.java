import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    // Test equal() method with two equal non-Iterator values
    @Test
    public void testEqualWithEqualNonIteratorValues() {
        EvalContext context = new EvalContext(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, "value", "value");
        assertTrue(result);
    }

    // Test equal() method with two equal Iterator values
    @Test
    public void testEqualWithEqualIteratorValues() {
        EvalContext context = new EvalContext(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, new Object[]{1, 2, 3}.iterator(), new Object[]{1, 2, 3}.iterator());
        assertTrue(result);
    }

    // Test equal() method with two equal Pointer values
    @Test
    public void testEqualWithEqualPointerValues() {
        EvalContext context = new EvalContext(null);
        Pointer pointer1 = new Pointer("pointer1");
        Pointer pointer2 = new Pointer("pointer1");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, pointer1, pointer2);
        assertTrue(result);
    }

    // Test equal() method with one InitialContext value and one non-InitialContext value
    @Test
    public void testEqualWithInitialContextAndNonInitialContext() {
        EvalContext context = new EvalContext(new InitialContext());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, context, 123);
        assertFalse(result);
    }

    // Test equal() method with one SelfContext value and one non-SelfContext value
    @Test
    public void testEqualWithSelfContextAndNonSelfContext() {
        EvalContext context = new EvalContext(new SelfContext());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, context, "value");
        assertFalse(result);
    }

    // Test contains() method with an Iterator that contains the specified value
    @Test
    public void testContainsWithIteratorContainingValue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.contains(new Object[]{1, 2, 3}.iterator(), 2);
        assertTrue(result);
    }

    // Test contains() method with an Iterator that does not contain the specified value
    @Test
    public void testContainsWithIteratorNotContainingValue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.contains(new Object[]{1, 2, 3}.iterator(), 4);
        assertFalse(result);
    }

    // Test findMatch() method with Iterators that have a matching value
    @Test
    public void testFindMatchWithMatchingIterators() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.findMatch(new Object[]{1, 2, 3}.iterator(), new Object[]{3, 4, 5}.iterator());
        assertTrue(result);
    }

    // Test findMatch() method with Iterators that do not have a matching value
    @Test
    public void testFindMatchWithNonMatchingIterators() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.findMatch(new Object[]{1, 2, 3}.iterator(), new Object[]{4, 5, 6}.iterator());
        assertFalse(result);
    }

    // Test equal() method with two unequal non-Iterator values
    @Test
    public void testEqualWithUnequalNonIteratorValues() {
        EvalContext context = new EvalContext(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, "value1", "value2");
        assertFalse(result);
    }

    // Test equal() method with two unequal Iterator values
    @Test
    public void testEqualWithUnequalIteratorValues() {
        EvalContext context = new EvalContext(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, new Object[]{1, 2, 3}.iterator(), new Object[]{4, 5, 6}.iterator());
        assertFalse(result);
    }

    // Test equal() method with two unequal Pointer values
    @Test
    public void testEqualWithUnequalPointerValues() {
        EvalContext context = new EvalContext(null);
        Pointer pointer1 = new Pointer("pointer1");
        Pointer pointer2 = new Pointer("pointer2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, pointer1, pointer2);
        assertFalse(result);
    }

    // Test equal() method with null and non-null values
    @Test
    public void testEqualWithNullAndNonNullValues() {
        EvalContext context = new EvalContext(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, null, "value");
        assertFalse(result);
    }

    // Test equal() method with non-boolean types
    @Test
    public void testEqualWithNonBooleanTypes() {
        EvalContext context = new EvalContext(null);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        boolean result = coreOperationCompare.equal(context, 123, "123");
        assertFalse(result);
    }

}