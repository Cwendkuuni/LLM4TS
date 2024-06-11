package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private EvalContext evalContext;

    @Before
    public void setup() {
        Expression arg1 = new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return Arrays.asList("foo", "bar", "baz").iterator();
            }
        };

        Expression arg2 = new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return "baz";
            }
        };

        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        evalContext = new EvalContext();
    }

    @Test
    public void testEqual() {
        boolean result = coreOperationCompare.equal(evalContext, new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return "foo";
            }
        }, new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return "foo";
            }
        });

        assertEquals(true, result);
    }

    @Test
    public void testContains() {
        boolean result = coreOperationCompare.contains(Arrays.asList("foo", "bar", "baz").iterator(), "baz");
        assertEquals(true, result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = Arrays.asList("foo", "bar", "baz").iterator();
        Iterator rit = Arrays.asList("baz").iterator();

        boolean result = coreOperationCompare.findMatch(lit, rit);
        assertEquals(true, result);
    }

    @Test
    public void testEqualWithPointers() {
        Object l = new Pointer() {
            @Override
            public Object getBaseValue() {
                return "foo";
            }

            @Override
            public Object getValue() {
                return "foo";
            }
        };

        Object r = new Pointer() {
            @Override
            public Object getBaseValue() {
                return "foo";
            }

            @Override
            public Object getValue() {
                return "foo";
            }
        };

        boolean result = coreOperationCompare.equal(l, r);
        assertEquals(true, result);
    }
}