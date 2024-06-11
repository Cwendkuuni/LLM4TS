package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression expr1 = new Expression();
        Expression expr2 = new Expression();
        CoreOperationCompare op = new CoreOperationCompare(expr1, expr2);

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new Pointer());

        assertFalse(op.equal(context, expr1, expr2));
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator();
        Object value = new Object();
        CoreOperationCompare op = new CoreOperationCompare();

        assertFalse(op.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        CoreOperationCompare op = new CoreOperationCompare();

        assertFalse(op.findMatch(lit, rit));
    }

    @Test
    public void testEqualValues() {
        Object l = new Object();
        Object r = new Object();
        CoreOperationCompare op = new CoreOperationCompare();

        assertFalse(op.equal(l, r));
    }
}