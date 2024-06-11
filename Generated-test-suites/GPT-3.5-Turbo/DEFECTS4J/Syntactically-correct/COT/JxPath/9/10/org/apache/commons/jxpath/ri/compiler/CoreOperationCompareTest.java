
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null);
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
        EvalContext context = null;
        Expression left = null;
        Expression right = null;
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        Iterator<Object> it = null;
        Object value = null;
        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Iterator<Object> lit = null;
        Iterator<Object> rit = null;
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualObjects() {
        Object l = null;
        Object r = null;
        assertTrue(coreOperationCompare.equal(l, r));
    }

}
