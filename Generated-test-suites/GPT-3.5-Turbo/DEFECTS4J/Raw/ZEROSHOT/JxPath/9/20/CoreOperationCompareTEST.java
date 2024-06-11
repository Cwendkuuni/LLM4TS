import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();
        EvalContext context = new EvalContext();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        boolean result = coreOperationCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Object l = new Pointer();
        Object r = new Pointer();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqualWithBoolean() {
        Object l = true;
        Object r = false;

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }

    @Test
    public void testEqualWithNumbers() {
        Object l = 10;
        Object r = 10;

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqualWithStrings() {
        Object l = "test";
        Object r = "test";

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);

        assertTrue(result);
    }

    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);

        assertFalse(result);
    }
}