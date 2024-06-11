package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression left = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        Expression right = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };

        CoreOperationCompare compare = new CoreOperationCompare(left, right) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return super.equal(context, left, right);
            }
        };

        assertTrue(compare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return super.contains(it, value);
            }
        };

        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        Object value = null;

        assertFalse(compare.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return super.findMatch(lit, rit);
            }
        };

        Iterator left = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        Iterator right = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        assertFalse(compare.findMatch(left, right));
    }

    @Test
    public void testEqualObject() {
        Object l = null;
        Object r = null;

        CoreOperationCompare compare = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return super.equal(l, r);
            }
        };

        assertTrue(compare.equal(l, r));
    }

}