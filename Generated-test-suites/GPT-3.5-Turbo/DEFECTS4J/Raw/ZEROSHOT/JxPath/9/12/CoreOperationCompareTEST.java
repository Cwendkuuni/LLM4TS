import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null);
        Expression left = new Expression();
        Expression right = new Expression();

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator it = new HashSet<>().iterator();
        Object value = new Object();

        boolean result = coreOperationCompare.contains(it, value);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator lit = new HashSet<>().iterator();
        Iterator rit = new HashSet<>().iterator();

        boolean result = coreOperationCompare.findMatch(lit, rit);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = new Pointer();
        Object r = new Pointer();

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = true;
        Object r = false;

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = 1;
        Object r = 2;

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = "Hello";
        Object r = "World";

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = null;
        Object r = null;

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithDifferentObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = new Object();
        Object r = new Object();

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertFalse(result);
    }

}