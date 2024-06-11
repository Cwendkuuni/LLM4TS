import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private Expression left;
    private Expression right;
    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        left = new Expression();
        right = new Expression();
        coreOperationCompare = new CoreOperationCompare(left, right);
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
        context.reset();

        Object l = new Object();
        Object r = new Object();

        assertFalse(coreOperationCompare.equal(context, left, right));

        left.compute(context);
        assertFalse(coreOperationCompare.equal(context, left, right));

        right.compute(context);
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new InitialContext();
        right = new SelfContext();
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new SelfContext();
        right = new InitialContext();
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new InitialContext();
        right = new InitialContext();
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new Iterator();
        assertFalse(coreOperationCompare.equal(context, left, right));

        right = new Iterator();
        assertFalse(coreOperationCompare.equal(context, left, right));

        left = new Iterator();
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();

        assertFalse(coreOperationCompare.contains(it, value));

        it = new Iterator();
        assertFalse(coreOperationCompare.contains(it, value));

        value = new Object();
        assertFalse(coreOperationCompare.contains(it, value));

        assertFalse(coreOperationCompare.contains(it, value));

        it = new Iterator();
        value = null;
        assertFalse(coreOperationCompare.contains(it, value));

        value = new Object();
        assertFalse(coreOperationCompare.contains(it, value));

        it = new Iterator();
        value = new Object();
        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        assertFalse(coreOperationCompare.findMatch(lit, rit));

        lit = new Iterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        rit = new Iterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        lit = new HashSet().iterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        rit = new HashSet().iterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        lit = new HashSet().iterator();
        rit = new HashSet().iterator();
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqual() {
        Object l = new Object();
        Object r = new Object();

        assertFalse(coreOperationCompare.equal(l, r));

        l = new Pointer();
        r = new Pointer();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Pointer();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Object();
        r = new Pointer();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Object();
        r = new Object();
        assertFalse(coreOperationCompare.equal(l, r));

        l = null;
        r = null;
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Boolean();
        r = new Boolean();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Boolean();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Boolean();
        r = new Object();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Object();
        r = new Boolean();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Number();
        r = new Number();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Number();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Number();
        r = new Object();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Object();
        r = new Number();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new String();
        r = new String();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new String();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new String();
        r = new Object();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Object();
        r = new String();
        assertFalse(coreOperationCompare.equal(l, r));

        l = new Object();
        r = new Object();
        assertTrue(coreOperationCompare.equal(l, r));
    }
}