package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    private CoreOperationRelationalExpression expression;
    private EvalContext context;

    @Test
    public void testComputeValue() {
        expression = new TestCoreOperationRelationalExpression(null);
        context = new TestEvalContext();

        // Test when compute returns true
        assertTrue((Boolean) expression.computeValue(context));

        // Test when compute returns false
        expression = new TestCoreOperationRelationalExpression(null);
        assertFalse((Boolean) expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        expression = new TestCoreOperationRelationalExpression(null);

        // Test the getPrecedence method
        assertEquals(2, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        expression = new TestCoreOperationRelationalExpression(null);

        // Test the isSymmetric method
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        expression = new TestCoreOperationRelationalExpression(null);
        context = new TestEvalContext();

        // Test when both operands are null
        assertFalse(expression.compute(null, null));

        // Test when left operand is null
        assertFalse(expression.compute(null, new Object()));

        // Test when right operand is null
        assertFalse(expression.compute(new Object(), null));

        // Test when both operands are different types
        assertFalse(expression.compute(new Object(), new Integer(5)));

        // Test when both operands are the same object
        assertTrue(expression.compute(new Object(), new Object()));

        // Test when left operand is InitialContext object and right operand is Iterator object
        assertTrue(expression.compute(new TestInitialContext(), new TestIterator()));

        // Test when left operand is Iterator object and right operand is InitialContext object
        assertTrue(expression.compute(new TestIterator(), new TestInitialContext()));

        // Test when left operand is Iterator object and right operand is Object
        assertTrue(expression.compute(new TestIterator(), new Object()));

        // Test when left operand is Object and right operand is Iterator object
        assertTrue(expression.compute(new Object(), new TestIterator()));

        // Test when both operands are Iterator objects
        assertTrue(expression.compute(new TestIterator(), new TestIterator()));

        // Test when both operands are Double objects and values are equal
        assertTrue(expression.compute(new Double(5), new Double(5)));

        // Test when both operands are Double objects and left operand value is lower than right operand value
        assertTrue(expression.compute(new Double(4), new Double(5)));

        // Test when both operands are Double objects and left operand value is higher than right operand value
        assertFalse(expression.compute(new Double(5), new Double(4)));

        // Test when both operands are Double objects and left operand value is NaN
        assertFalse(expression.compute(new Double(Double.NaN), new Double(5)));

        // Test when both operands are Double objects and right operand value is NaN
        assertFalse(expression.compute(new Double(5), new Double(Double.NaN)));
    }

    @Test
    public void testReduce() {
        expression = new TestCoreOperationRelationalExpression(null);

        // Test when operand is SelfContext object
        Object result = expression.reduce(new TestSelfContext());
        assertTrue(result instanceof EvalContext);

        // Test when operand is Collection object
        result = expression.reduce(new TestCollection());
        assertTrue(result instanceof Iterator);

        // Test when operand is Object
        result = expression.reduce(new Object());
        assertEquals(new Object(), result);
    }

    @Test
    public void testContainsMatch() {
        expression = new TestCoreOperationRelationalExpression(null);
        TestIterator it = new TestIterator();
        Object value = new Object();

        // Test when the Iterator is empty
        assertFalse(expression.containsMatch(it, value));

        // Test when the Iterator does not contain the value
        it = new TestIterator(Arrays.asList(new Object()));
        assertFalse(expression.containsMatch(it, value));

        // Test when the Iterator contains the value
        it = new TestIterator(Arrays.asList(new Object(), value));
        assertTrue(expression.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        expression = new TestCoreOperationRelationalExpression(null);
        TestIterator lit = new TestIterator();
        TestIterator rit = new TestIterator();
        Object value = new Object();

        // Test when both Iterators are empty
        assertFalse(expression.findMatch(lit, rit));

        // Test when one Iterator is empty
        lit = new TestIterator(Arrays.asList(new Object()));
        assertFalse(expression.findMatch(lit, rit));

        // Test when both Iterators does not contain the value
        lit = new TestIterator(Arrays.asList(new Object()));
        rit = new TestIterator(Arrays.asList(new Object()));
        assertFalse(expression.findMatch(lit, rit));

        // Test when one Iterator contains the value
        lit = new TestIterator(Arrays.asList(new Object(), value));
        rit = new TestIterator(Arrays.asList(new Object()));
        assertTrue(expression.findMatch(lit, rit));

        // Test when both Iterators contain the value
        lit = new TestIterator(Arrays.asList(value));
        rit = new TestIterator(Arrays.asList(value));
        assertTrue(expression.findMatch(lit, rit));
    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare > 0;
        }
    }

    private class TestEvalContext extends EvalContext {
        public TestEvalContext() {
            super(null, new TestInitialContext());
        }

        @Override
        public Object getSingleNodePointer() {
            return new Object();
        }
    }

    private class TestInitialContext extends InitialContext {
        @Override
        public Object getSingleNodePointer() {
            return new Object();
        }
    }

    private class TestSelfContext extends SelfContext {
        @Override
        public Object getSingleNodePointer() {
            return new Object();
        }
    }

    private class TestIterator implements Iterator {
        private List<Object> list;
        private int index;

        public TestIterator() {
            this.list = new ArrayList<>();
            this.index = 0;
        }

        public TestIterator(List<Object> list) {
            this.list = list;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public Object next() {
            return list.get(index++);
        }
    }

    private class TestCollection implements Collection {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public Object[] toArray(Object[] a) {
            return new Object[0];
        }

        @Override
        public boolean add(Object e) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection c) {
            return false;
        }

        @Override
        public boolean addAll(Collection c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }
}