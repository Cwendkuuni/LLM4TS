import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();

        boolean result = coreOperationCompare.equal(context, left, right);

        // Assert the result
        assertTrue(result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();

        boolean result = coreOperationCompare.contains(it, value);

        // Assert the result
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        boolean result = coreOperationCompare.findMatch(lit, rit);

        // Assert the result
        assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Object l = new Pointer();
        Object r = new Pointer();

        boolean result = coreOperationCompare.equal(l, r);

        // Assert the result
        assertTrue(result);
    }

    @Test
    public void testEqualWithNumbersAndNaN() {
        Object l = 1.0;
        Object r = Double.NaN;

        boolean result = coreOperationCompare.equal(l, r);

        // Assert the result
        assertFalse(result);
    }

    @Test
    public void testEqualWithStrings() {
        Object l = "string1";
        Object r = "string2";

        boolean result = coreOperationCompare.equal(l, r);

        // Assert the result
        assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;

        boolean result = coreOperationCompare.equal(l, r);

        // Assert the result
        assertTrue(result);
    }

}