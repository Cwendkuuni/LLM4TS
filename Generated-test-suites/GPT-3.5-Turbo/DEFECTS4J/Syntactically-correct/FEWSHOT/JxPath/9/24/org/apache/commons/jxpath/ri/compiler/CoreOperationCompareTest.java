/*
 * Test cases for CoreOperationCompare class
 */
package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {
    private CoreOperationCompare operation;

    @Before
    public void setUp() {
        Expression arg1 = new ExpressionStub(1);
        Expression arg2 = new ExpressionStub(2);
        operation = new CoreOperationCompareStub(arg1, arg2);
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(2, operation.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(operation.isSymmetric());
    }

    @Test
    public void testEqual_CompareEqualValues_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Expression left = new ExpressionStub(1);
        Expression right = new ExpressionStub(1);

        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testEqual_CompareNullValues_ReturnsTrue() {
        EvalContext context = new EvalContext();
        Expression left = new ExpressionStub(null);
        Expression right = new ExpressionStub(null);

        assertTrue(operation.equal(context, left, right));
    }

    @Test
    public void testEqual_CompareDifferentTypes_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Expression left = new ExpressionStub(1);
        Expression right = new ExpressionStub("1");

        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testEqual_CompareDifferentValues_ReturnsFalse() {
        EvalContext context = new EvalContext();
        Expression left = new ExpressionStub(1);
        Expression right = new ExpressionStub(2);

        assertFalse(operation.equal(context, left, right));
    }

    @Test
    public void testContains_IteratorContainsValue_ReturnsTrue() {
        ExpressionStub value = new ExpressionStub(1);
        IteratorStub iterator = new IteratorStub(value);

        assertTrue(operation.contains(iterator, value));
    }

    @Test
    public void testContains_IteratorDoesNotContainValue_ReturnsFalse() {
        ExpressionStub value = new ExpressionStub(1);
        ExpressionStub otherValue = new ExpressionStub(2);
        IteratorStub iterator = new IteratorStub(value);

        assertFalse(operation.contains(iterator, otherValue));
    }

    @Test
    public void testFindMatch_HashSetContainsValue_ReturnsTrue() {
        ExpressionStub value = new ExpressionStub(1);
        IteratorStub iterator = new IteratorStub(value);
        HashSetStub hashSet = new HashSetStub();
        hashSet.add(value);

        assertTrue(operation.findMatch(iterator, hashSet));
    }

    @Test
    public void testFindMatch_HashSetDoesNotContainValue_ReturnsFalse() {
        ExpressionStub value = new ExpressionStub(1);
        ExpressionStub otherValue = new ExpressionStub(2);
        IteratorStub iterator = new IteratorStub(value);
        HashSetStub hashSet = new HashSetStub();
        hashSet.add(otherValue);

        assertFalse(operation.findMatch(iterator, hashSet));
    }

    @Test
    public void testEqual_PointersEqual_ReturnsTrue() {
        Pointer pointer = new PointerStub(1);

        assertTrue(operation.equal(pointer, pointer));
    }

    @Test
    public void testEqual_PointerValuesEqual_ReturnsTrue() {
        Pointer pointer1 = new PointerStub(1);
        Pointer pointer2 = new PointerStub(1);

        assertTrue(operation.equal(pointer1, pointer2));
    }

    @Test
    public void testEqual_PointerValuesNotEqual_ReturnsFalse() {
        Pointer pointer1 = new PointerStub(1);
        Pointer pointer2 = new PointerStub(2);

        assertFalse(operation.equal(pointer1, pointer2));
    }

    @Test
    public void testEqual_LeftPointerEqualsRightValue_ReturnsTrue() {
        Pointer pointer = new PointerStub(1);
        ExpressionStub value = new ExpressionStub(1);

        assertTrue(operation.equal(pointer, value));
    }

    @Test
    public void testEqual_LeftValueEqualsRightPointer_ReturnsTrue() {
        ExpressionStub value = new ExpressionStub(1);
        Pointer pointer = new PointerStub(1);

        assertTrue(operation.equal(value, pointer));
    }

    @Test
    public void testEqual_BooleanValuesEqual_ReturnsTrue() {
        assertTrue(operation.equal(true, true));
    }

    @Test
    public void testEqual_BooleanValuesNotEqual_ReturnsFalse() {
        assertFalse(operation.equal(true, false));
    }

    @Test
    public void testEqual_DoubleValuesEqual_ReturnsTrue() {
        double value = 1.0;

        assertTrue(operation.equal(value, value));
    }

    @Test
    public void testEqual_DoubleValuesNotEqual_ReturnsFalse() {
        assertFalse(operation.equal(1.0, 2.0));
    }

    @Test
    public void testEqual_StringValuesEqual_ReturnsTrue() {
        assertEquals(true, operation.equal("value", "value"));
    }

    @Test
    public void testEqual_StringValuesNotEqual_ReturnsFalse() {
        assertEquals(false, operation.equal("value1", "value2"));
    }

    @Test
    public void testEqual_LeftValueIsNull_ReturnsFalse() {
        assertEquals(false, operation.equal(null, "value"));
    }

    @Test
    public void testEqual_RightValueIsNull_ReturnsFalse() {
        assertEquals(false, operation.equal("value", null));
    }

    @Test
    public void testEqual_BothValuesAreNull_ReturnsTrue() {
        assertEquals(true, operation.equal(null, null));
    }

    private class ExpressionStub extends Expression {
        private Object value;

        public ExpressionStub(Object value) {
            this.value = value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }
    }

    private class CoreOperationCompareStub extends CoreOperationCompare {
        public CoreOperationCompareStub(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }

        @Override
        protected boolean equal(Object l, Object r) {
            return l.equals(r);
        }
    }

    private class IteratorStub implements Iterator<Object> {
        private Object value;

        public IteratorStub(Object value) {
            this.value = value;
        }

        @Override
        public boolean hasNext() {
            return value != null;
        }

        @Override
        public Object next() {
            Object nextValue = value;
            value = null;
            return nextValue;
        }

        @Override
        public void remove() {
            // Not implemented
        }
    }

    private class HashSetStub extends HashSet<Object> {
        @Override
        public Iterator<Object> iterator() {
            return new IteratorStub(value);
        }

        private Object value;

        public void add(Object value) {
            this.value = value;
        }
    }

    private class PointerStub implements Pointer {
        private Object value;

        public PointerStub(Object value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public void setValue(Object value) {
            // Not implemented
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public Pointer createPath(EvalContext context) {
            return null;
        }

        @Override
        public Pointer createPathAndSetValue(EvalContext context, Object value) {
            return null;
        }

        @Override
        public Pointer copy() {
            return null;
        }

        @Override
        public String asPath() {
            return null;
        }

        @Override
        public int compareChildNodePointers(Pointer pointer1, Pointer pointer2) {
            return 0;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public boolean isActual() {
            return false;
        }
    }
}