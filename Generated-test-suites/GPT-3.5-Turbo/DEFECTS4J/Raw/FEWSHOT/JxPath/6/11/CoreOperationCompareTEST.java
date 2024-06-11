// Test cases for CoreOperationCompare

package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {
    private final EvalContext context = new EvalContext();

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return true;
            }
        };
        assertTrue(operation.equal(context, null, null));

        operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
        assertFalse(operation.equal(context, null, null));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return it.hasNext();
            }
        };
        assertTrue(operation.contains(Collections.singletonList("test").iterator(), "test"));
        assertFalse(operation.contains(Collections.emptyList().iterator(), "test"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean contains(Iterator it, Object value) {
                return it.hasNext();
            }

            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return lit.hasNext() || rit.hasNext();
            }
        };
        assertTrue(operation.findMatch(Arrays.asList("test1").iterator(), Arrays.asList("test2").iterator()));
        assertTrue(operation.findMatch(Collections.emptyList().iterator(), Arrays.asList("test2").iterator()));
        assertTrue(operation.findMatch(Arrays.asList("test1").iterator(), Collections.emptyList().iterator()));
        assertTrue(operation.findMatch(Collections.emptyList().iterator(), Collections.emptyList().iterator()));
    }

    @Test
    public void testEqualForObjectPointers() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return l.equals(r);
            }
        };
        Pointer pointer1 = new Pointer(null, "test");
        Pointer pointer2 = new Pointer(null, "test");
        assertTrue(operation.equal(pointer1, pointer2));

        pointer1 = new Pointer(null, "test1");
        pointer2 = new Pointer(null, "test2");
        assertFalse(operation.equal(pointer1, pointer2));
    }

    @Test
    public void testEqualForBooleanValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return (boolean) l == (boolean) r;
            }
        };
        assertTrue(operation.equal(true, true));
        assertFalse(operation.equal(true, false));
        assertFalse(operation.equal(false, true));
        assertTrue(operation.equal(false, false));
    }

    @Test
    public void testEqualForNumberValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return (double) l == (double) r;
            }
        };
        assertTrue(operation.equal(1.0, 1.0));
        assertFalse(operation.equal(1.0, 2.0));
        assertFalse(operation.equal(2.0, 1.0));
        assertTrue(operation.equal(2.0, 2.0));
    }

    @Test
    public void testEqualForStringValues() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null) {
            @Override
            protected boolean equal(Object l, Object r) {
                return l.equals(r);
            }
        };
        assertTrue(operation.equal("test", "test"));
        assertFalse(operation.equal("test", "foo"));
        assertFalse(operation.equal("foo", "test"));
        assertTrue(operation.equal("foo", "foo"));
    }
}