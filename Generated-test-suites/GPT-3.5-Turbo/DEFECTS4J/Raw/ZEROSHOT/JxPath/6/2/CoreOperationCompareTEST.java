import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Expression left = null;
        Expression right = null;
        boolean result = operation.equal(context, left, right);
        assertTrue(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Iterator it = null;
        Object value = null;
        boolean result = operation.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Iterator lit = null;
        Iterator rit = null;
        boolean result = operation.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = null;
        Object r = null;
        boolean result = operation.equal(l, r);
        assertTrue(result);
    }
}