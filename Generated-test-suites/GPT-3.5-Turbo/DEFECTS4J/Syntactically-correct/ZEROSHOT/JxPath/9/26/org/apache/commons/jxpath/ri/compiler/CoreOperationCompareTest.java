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
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test null values
        EvalContext context = null;
        Expression left = null;
        Expression right = null;
        boolean result = coreOperationCompare.equal(context, left, right);
        assertTrue(result);

        // Test non-null values
        context = new EvalContext();
        left = new CoreOperation();
        right = new CoreOperation();
        result = coreOperationCompare.equal(context, left, right);
        assertFalse(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test empty iterator
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
        boolean result = coreOperationCompare.contains(it, value);
        assertFalse(result);

        // Test iterator with elements
        it = new Iterator() {
            private final Object[] elements = new Object[]{1, 2, 3};
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        };

        value = 2;
        result = coreOperationCompare.contains(it, value);
        assertTrue(result);

        value = 5;
        result = coreOperationCompare.contains(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test empty iterators
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

        boolean result = coreOperationCompare.findMatch(left, right);
        assertFalse(result);

        // Test iterators with elements
        left = new Iterator() {
            private final Object[] elements = new Object[]{1, 2, 3};
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        };

        right = new Iterator() {
            private final Object[] elements = new Object[]{3, 4, 5};
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        };

        result = coreOperationCompare.findMatch(left, right);
        assertTrue(result);

        left = new Iterator() {
            private final Object[] elements = new Object[]{1, 2, 3};
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        };

        right = new Iterator() {
            private final Object[] elements = new Object[]{4, 5, 6};
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public Object next() {
                return elements[index++];
            }
        };

        result = coreOperationCompare.findMatch(left, right);
        assertFalse(result);
    }

    @Test
    public void testEqual2() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);

        // Test Pointer objects
        Pointer l = new Pointer();
        Pointer r = new Pointer();
        boolean result = coreOperationCompare.equal(l, r);
        assertFalse(result);

        l = new Pointer();
        r = new Pointer();
        r.setValue(null);
        result = coreOperationCompare.equal(l, r);
        assertFalse(result);

        l = new Pointer();
        l.setValue(null);
        r = new Pointer();
        result = coreOperationCompare.equal(l, r);
        assertFalse(result);

        l = new Pointer();
        l.setValue(null);
        r = new Pointer();
        r.setValue(null);
        result = coreOperationCompare.equal(l, r);
        assertTrue(result);

        // Test values other than Pointer objects
        l = new Pointer();
        r = new Pointer();
        result = coreOperationCompare.equal(l, r);
        assertFalse(result);

        l = new Pointer();
        r = new Pointer();
        r.setValue(1);
        result = coreOperationCompare.equal(l, r);
        assertFalse(result);

        l = new Pointer();
        l.setValue(1);
        r = new Pointer();
        result = coreOperationCompare.equal(l, r);
        assertFalse(result);

        l = new Pointer();
        l.setValue(1);
        r = new Pointer();
        r.setValue(1);
        result = coreOperationCompare.equal(l, r);
        assertTrue(result);
    }
}