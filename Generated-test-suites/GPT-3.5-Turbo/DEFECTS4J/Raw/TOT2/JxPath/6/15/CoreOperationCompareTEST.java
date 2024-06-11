package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();

        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator it = new ArrayList().iterator();
        Object value = "test";

        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Iterator lit = new ArrayList().iterator();
        Iterator rit = new ArrayList().iterator();

        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualObjectObject() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = "test";
        Object r = "test";

        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualPointerPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = new Pointer();
        Object r = new Pointer();

        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualObjectPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = "test";
        Object r = new Pointer();

        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualPointerObject() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = new Pointer();
        Object r = "test";

        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualBooleanValue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = true;
        Object r = false;

        assertFalse(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualDoubleValue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = 10.5;
        Object r = 10.5;

        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testEqualStringValue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = "test";
        Object r = "test";

        assertTrue(coreOperationCompare.equal(l, r));
    }

    @Test
    public void testNonNullEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(new Expression(), new Expression());
        Object l = new Object();
        Object r = new Object();

        assertFalse(coreOperationCompare.equal(l, r));
    }
}