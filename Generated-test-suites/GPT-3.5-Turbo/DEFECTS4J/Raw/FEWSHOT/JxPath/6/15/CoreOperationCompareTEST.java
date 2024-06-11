package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        Expression arg1 = new Constant(1);
        Expression arg2 = new Constant(1);
        CoreOperationCompare operation = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext(null);

        assertTrue(operation.equal(context, arg1, arg2));

        arg1 = new Constant(1);
        arg2 = new Constant(2);
        operation = new CoreOperationCompare(arg1, arg2);

        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare();
        Iterator it = Arrays.asList(1, 2, 3).iterator();
        
        assertTrue(operation.contains(it, 2));
        assertFalse(operation.contains(it, 4));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare();
        Iterator lit = Arrays.asList(1, 2, 3).iterator();
        Iterator rit = Arrays.asList(4, 5, 2).iterator();

        assertTrue(operation.findMatch(lit, rit));
        assertFalse(operation.findMatch(lit, rit));
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare();
        assertTrue(operation.equal("foo", "foo"));
        assertFalse(operation.equal("foo", "bar"));

        assertTrue(operation.equal(1, 1));
        assertFalse(operation.equal(1, 2));

        assertTrue(operation.equal(true, true));
        assertFalse(operation.equal(true, false));

        assertTrue(operation.equal(1.0, 1.0));
        assertFalse(operation.equal(1.0, 2.0));

        assertFalse(operation.equal(null, "foo"));
        assertFalse(operation.equal("foo", null));
        assertTrue(operation.equal(null, null));
    }
}