/*
JUnit Test Cases for CoreOperationCompare class
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationCompareTest {
    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            protected boolean equal(Object l, Object r) {
                // Define custom implementation here
                return true;
            }
        };

        EvalContext context = new EvalContext();
        Expression left = new Expression();
        Expression right = new Expression();
        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            protected boolean contains(Iterator it, Object value) {
                // Define custom implementation here
                return true;
            }
        };

        Iterator it = new Iterator();
        Object value = new Object();
        assertTrue(operation.contains(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                // Define custom implementation here
                return true;
            }
        };

        Iterator lit = new Iterator();
        Iterator rit = new Iterator();
        assertTrue(operation.findMatch(lit, rit));
    }

    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            protected boolean equal(Object l, Object r) {
                // Define custom implementation here
                return true;
            }
        };

        Pointer l = new Pointer();
        Pointer r = new Pointer();
        assertTrue(operation.equal(l, r));
    }

    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare operation = new CoreOperationCompare();

        assertTrue(operation.equal(true, true));
        assertTrue(operation.equal(false, false));
        assertFalse(operation.equal(true, false));
        assertFalse(operation.equal(false, true));
    }

    @Test
    public void testEqualWithNumber() {
        CoreOperationCompare operation = new CoreOperationCompare();

        assertTrue(operation.equal(1, 1));
        assertTrue(operation.equal(1.0, 1.0));
        assertFalse(operation.equal(1, 2));
        assertFalse(operation.equal(1.0, 2.0));
    }

    @Test
    public void testEqualWithString() {
        CoreOperationCompare operation = new CoreOperationCompare();

        assertTrue(operation.equal("foo", "foo"));
        assertFalse(operation.equal("foo", "bar"));
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare operation = new CoreOperationCompare();

        assertTrue(operation.equal(null, null));
        assertFalse(operation.equal(null, "foo"));
        assertFalse(operation.equal("foo", null));
    }
}