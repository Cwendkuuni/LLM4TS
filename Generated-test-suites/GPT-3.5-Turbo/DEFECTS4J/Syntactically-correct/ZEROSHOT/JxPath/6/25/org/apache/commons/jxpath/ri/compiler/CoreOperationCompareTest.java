import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Create test object
        Expression left = new Expression();
        Expression right = new Expression();
        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        // Create mock context
        EvalContext context = new EvalContext();

        // Test equal() method
        boolean result = compare.equal(context, left, right);
        assertTrue(result);
    }

    @Test
    public void testContains() {
        // Create test objects
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare compare = new CoreOperationCompare();

        // Test contains() method
        boolean result = compare.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        // Create test objects
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare compare = new CoreOperationCompare();

        // Test findMatch() method
        boolean result = compare.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqualValues() {
        // Create test objects
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare compare = new CoreOperationCompare();

        // Test equal() method
        boolean result = compare.equal(l, r);
        assertTrue(result);
    }

}