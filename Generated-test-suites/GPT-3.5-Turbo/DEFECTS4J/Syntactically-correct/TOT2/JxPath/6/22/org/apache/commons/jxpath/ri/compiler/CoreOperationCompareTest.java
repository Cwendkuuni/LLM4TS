import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqualValues() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        boolean result = compare.equal(context, left, right);

        assertTrue(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);

        Iterator it = new Iterator();
        Object value = new Object();

        boolean result = compare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);

        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        boolean result = compare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testValueComparison() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);

        Object l = new Object();
        Object r = new Object();

        boolean result = compare.equal(l, r);

        assertTrue(result);
    }

}