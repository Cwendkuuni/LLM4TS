package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(null, null);
        CoreOperationCompare op = new CoreOperationCompare(new Expression(), new Expression());

        assertTrue(op.equal(context, new Expression(), new Expression()));
        assertFalse(op.equal(context, new Expression(), new Expression()));
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(null, null);
        CoreOperationCompare op = new CoreOperationCompare(new Expression(), new Expression());

        Iterator it = new Iterator();
        Object value = new Object();

        assertFalse(op.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(null, null);
        CoreOperationCompare op = new CoreOperationCompare(new Expression(), new Expression());

        Iterator lit = new Iterator();
        Iterator rit = new Iterator();

        assertFalse(op.findMatch(lit, rit));
    }

    @Test
    public void testEqual() {
        CoreOperationCompare op = new CoreOperationCompare(new Expression(), new Expression());

        Object l = new Object();
        Object r = new Object();

        assertFalse(op.equal(l, r));
    }

}