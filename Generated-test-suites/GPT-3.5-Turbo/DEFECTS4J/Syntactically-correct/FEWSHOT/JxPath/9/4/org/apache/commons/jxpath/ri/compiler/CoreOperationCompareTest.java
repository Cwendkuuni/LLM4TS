package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;

public class CoreOperationCompareTest {

    @Test
    public void testGetPrecedence() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual() {
        Expression left = null;
        Expression right = null;
        EvalContext context = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        assertTrue(coreOperationCompare.equal(context, left, right));

        left = null;
        right = null;
        context = null;
        coreOperationCompare = new CoreOperationCompare(left, right) {
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
        assertFalse(coreOperationCompare.equal(context, left, right));   
    }

    @Test
    public void testContains() {
        Iterator it = null;
        Object value = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        assertFalse(coreOperationCompare.contains(it, value));

        it = null;
        value = null;
        coreOperationCompare = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Iterator lit = null;
        Iterator rit = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        assertFalse(coreOperationCompare.findMatch(lit, rit));

        lit = null;
        rit = null;
        coreOperationCompare = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualObject() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        assertTrue(coreOperationCompare.equal(l, r));

        l = null;
        r = null;
        coreOperationCompare = new CoreOperationCompare(null, null) {
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
        assertFalse(coreOperationCompare.equal(l, r));
    }
}