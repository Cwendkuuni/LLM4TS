package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        assertNotNull(coreOperationCompare);
    }

    @Test
    public void testGetPrecedence() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual() {
        Expression left = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression right = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        EvalContext context = new EvalContext(null, null);

        assertTrue(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Iterator it = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        Object value = null;

        assertFalse(coreOperationCompare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        Expression arg1 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression arg2 = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);

        Iterator lit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        Iterator rit = new Iterator() {
            public boolean hasNext() {
                return false;
            }

            public Object next() {
                return null;
            }
        };

        assertFalse(coreOperationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqual() {
        Expression l = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        Expression r = new Expression() {
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(l, r);

        assertTrue(coreOperationCompare.equal(l, r));
    }
}