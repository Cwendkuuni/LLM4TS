import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        boolean result = operation.equal(context, left, right);
        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Iterator it = null;
        Object value = null;
        boolean result = operation.contains(it, value);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Iterator lit = null;
        Iterator rit = null;
        boolean result = operation.findMatch(lit, rit);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = null;
        Object r = null;
        boolean result = operation.equal(l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualValues_boolean() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = true;
        Object r = false;
        boolean result = operation.equal(l, r);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualValues_number() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = 1;
        Object r = 1.0;
        boolean result = operation.equal(l, r);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualValues_string() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = "abc";
        Object r = "def";
        boolean result = operation.equal(l, r);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualValues_null() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);
        Object l = null;
        Object r = null;
        boolean result = operation.equal(l, r);
        Assert.assertTrue(result);
    }

}