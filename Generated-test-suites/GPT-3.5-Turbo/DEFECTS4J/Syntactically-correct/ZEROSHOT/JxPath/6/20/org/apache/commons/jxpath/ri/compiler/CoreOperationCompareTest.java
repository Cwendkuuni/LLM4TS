import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    private CoreOperationCompare compare;

    @Before
    public void setUp() {
        compare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = compare.equal(context, left, right);
        assertTrue(result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        boolean result = compare.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        boolean result = compare.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Object l = new Pointer();
        Object r = new Pointer();
        boolean result = compare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualWithBoolean() {
        Object l = new Boolean(true);
        Object r = new Boolean(false);
        boolean result = compare.equal(l, r);
        assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        Object l = new Integer(5);
        Object r = new Double(5.0);
        boolean result = compare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualWithString() {
        Object l = "hello";
        Object r = "world";
        boolean result = compare.equal(l, r);
        assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        Object l = null;
        Object r = null;
        boolean result = compare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testEqualWithDifferentTypes() {
        Object l = new Integer(5);
        Object r = "5";
        boolean result = compare.equal(l, r);
        assertFalse(result);
    }

}