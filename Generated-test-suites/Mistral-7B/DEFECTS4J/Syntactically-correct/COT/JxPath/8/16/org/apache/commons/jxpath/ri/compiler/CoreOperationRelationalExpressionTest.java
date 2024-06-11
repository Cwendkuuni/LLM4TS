package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.iter.Iterator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Tests for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    /**
     * Test method for {@link CoreOperationRelationalExpression#CoreOperationRelationalExpression(Expression[])} .
     */
    @org.junit.Test
    public void testCoreOperationRelationalExpressionConstructor() {
        // Create instances with valid arguments
        Expression[] args1 = new Expression[2];
        Expression arg1 = new CoreOperation(new Literal(1));
        Expression arg2 = new CoreOperation(new Literal(2));
        args1[0] = arg1;
        args1[1] = arg2;
        CoreOperationRelationalExpression coreOpRelationalExpression1 = new CoreOperationRelationalExpression(args1);

        Expression[] args2 = new Expression[2];
        Expression arg3 = new CoreOperation(new Literal(3));
        Expression arg4 = new CoreOperation(new Literal(3));
        args2[0] = arg3;
        args2[1] = arg4;
        CoreOperationRelationalExpression coreOpRelationalExpression2 = new CoreOperationRelationalExpression(args2);

        // Test that correct number of arguments are passed to the constructor
        assertEquals(2, coreOpRelationalExpression1.getArgs().length);
        assertEquals(2, coreOpRelationalExpression2.getArgs().length);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#computeValue(EvalContext)} .
     */
    @org.junit.Test
    public void testComputeValue() {
        // Given
        Expression left = new CoreOperation(new Literal(1));
        Expression right = new CoreOperation(new Literal(2));
        CoreOperationRelationalExpression coreOpRelationalExpression = new CoreOperationRelationalExpression(new CoreOperation[] { left, right });
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        // When
        Object result = coreOpRelationalExpression.computeValue(context);

        // Then
        assertNotNull(result);
        assertEquals(Boolean.FALSE, result);

        // Given
        left = new CoreOperation(new Literal(2));
        right = new CoreOperation(new Literal(2));
        coreOpRelationalExpression = new CoreOperationRelationalExpression(new CoreOperation[] { left, right });

        // When
        result = coreOpRelationalExpression.computeValue(context);

        // Then
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#getPrecedence()} .
     */
    @org.junit.Test
    public void testGetPrecedence() {
        // When
        int precedence = new CoreOperationRelationalExpression(new CoreOperation[2][])
                .getPrecedence();

        // Then
        assertEquals(3, precedence);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#isSymmetric()} .
     */
    @org.junit.Test
    public void testIsSymmetric() {
        // When
        boolean symmetric = new CoreOperationRelationalExpression(new CoreOperation[2][])
                .isSymmetric();

        // Then
        assertFalse(symmetric);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#evaluateCompare(int)} .
     */
    @org.junit.Test
    public void testEvaluateCompare() {
        // Given
        CoreOperationRelationalExpression coreOpRelationalExpression = new CoreOperationRelationalExpression(new CoreOperation[2][]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        // When
        boolean result = coreOpRelationalExpression.evaluateCompare(0);

        // Then
        assertTrue(result);

        // When
        result = coreOpRelationalExpression.evaluateCompare(-1);

        // Then
        assertFalse(result);

        // When
        result = coreOpRelationalExpression.evaluateCompare(1);

        // Then
        assertFalse(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#compute(Object, Object)} .
     */
    @org.junit.Test
    public void testCompute() {
        // Given
        Object left = new Literal(1);
        Object right = new Literal(2);
        CoreOperationRelationalExpression coreOpRelationalExpression = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperation(left), new CoreOperation(right) });

        // Given - initialization of evaluation context
        EvalContext context = new EvalContext(new InitialContext(), new SelfContext());

        // When - compute with non-iterable left and non-iterable right
        boolean result = coreOpRelationalExpression.compute(left, right);

        // Then
        assertFalse(result);

        // Given - initialization of evaluation context with iterable left and non-iterable right
        context = new EvalContext(new InitialContext(), new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3)))) {
            @Override
            public Iterator getIterator() {
                return ((Collection) getSingleNodePointer()).iterator();
            }
        };
        left = getSingleNodePointer();

        // When - compute
        result = coreOpRelationalExpression.compute(left, right);

        // Then
        assertFalse(result);

        // Given - initialization of evaluation context with non-iterable left and iterable right
        context = new EvalContext(new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))) {
            @Override
            public Iterator getIterator() {
                return ((Collection) getSingleNodePointer()).iterator();
            }
        }, new SelfContext());
        right = ((Collection) getSingleNodePointer()).iterator();

        // When - compute
        result = coreOpRelationalExpression.compute(left, right);

        // Then
        assertFalse(result);

        // Given - initialization of evaluation context with iterable left and iterable right
        context = new EvalContext(new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))), new IteratorTestData(Arrays.asList(new Literal(3), new Literal(2), new Literal(1))));
        left = ((Collection) getSingleNodePointer()).iterator();
        right = ((Collection) getSingleNodePointer()).iterator();

        // When - compute
        result = coreOpRelationalExpression.compute(left, right);

        // Then
        assertFalse(result);

        // Given - initialization of evaluation context with iterable left and iterable right
        // where the first iterable contains the second iterable
        context = new EvalContext(new IteratorTestData(Arrays.asList(new Literal(1), new IteratorTestData(Arrays.asList(new Literal(3), new Literal(2), new Literal(1))), new Literal(4))), new IteratorTestData(Arrays.asList(new Literal(3), new Literal(2), new Literal(1))));
        left = ((Collection) getSingleNodePointer()).iterator();
        right = ((Collection) getSingleNodePointer()).iterator();

        // When - compute
        result = coreOpRelationalExpression.compute(left, right);

        // Then
        assertTrue(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#reduce(Object)} .
     */
    @org.junit.Test
    public void testReduce() {
        // Given
        Object o1 = new Literal(1);
        Object o2 = new SelfContext();
        Object o3 = new CollectionTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3)));

        // When
        Object result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .reduce(o1);

        // Then
        assertEquals(o1, result);

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .reduce(o2);

        // Then
        assertEquals(o2, result);

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .reduce(o3);

        // Then
        assertEquals(o3.getClass().getMethod("iterator").invoke(o3), result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#containsMatch(Iterator, Object)} .
     */
    @org.junit.Test
    public void testContainsMatch() {
        // Given
        Iterator it1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        Object value1 = new Literal(2);

        // When
        boolean result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .containsMatch(it1, value1);

        // Then
        assertFalse(result);

        // Given
        it1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        Object value2 = new Literal(1);

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .containsMatch(it1, value2);

        // Then
        assertTrue(result);

        // Given
        it1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        Object value3 = new Literal(3);

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .containsMatch(it1, value3);

        // Then
        assertTrue(result);

        // Given
        it1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        Object value4 = new Literal(4);

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .containsMatch(it1, value4);

        // Then
        assertFalse(result);
    }

    /**
     * Test method for {@link CoreOperationRelationalExpression#findMatch(Iterator, Iterator)} .
     */
    @org.junit.Test
    public void testFindMatch() {
        // Given
        Iterator lit1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        Iterator rit1 = new IteratorTestData(Arrays.asList(new Literal(3), new Literal(2), new Literal(1))).iterator();

        // When
        boolean result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .findMatch(lit1, rit1);

        // Then
        assertFalse(result);

        // Given
        lit1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        rit1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .findMatch(lit1, rit1);

        // Then
        assertTrue(result);

        // Given
        lit1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        rit1 = new IteratorTestData(Arrays.asList(new Literal(3), new Literal(2), new Literal(1))).iterator();

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .findMatch(lit1, rit1);

        // Then
        assertTrue(result);

        // Given
        lit1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(2), new Literal(3))).iterator();
        rit1 = new IteratorTestData(Arrays.asList(new Literal(1), new Literal(4), new Literal(3))).iterator();

        // When
        result = new CoreOperationRelationalExpression(new CoreOperation[1][])
                .findMatch(lit1, rit1);

        // Then
        assertFalse(result);
    }

    /**
     * Test data for {@link Iterator} type.
     */
    private static class IteratorTestData implements Iterator {
        private Iterator delegate;
        private Collection collection;

        public IteratorTestData(Collection collection) {
            this.collection = collection;
            this.delegate = collection.iterator();
        }

        @Override
        public boolean hasNext() {
            return delegate.hasNext();
        }

        @Override
        public Object next() {
            return delegate.next();
        }

        @Override
        public void remove() {
            delegate.remove();
        }

        public Collection getCollection() {
            return collection;
        }

        public Object getSingleNodePointer() {
            return collection.iterator().next();
        }
    }

    /**
     * Test data for {@link Collection} type.
     */
    private static class CollectionTestData implements Collection {
        private Object[] elements;

        public CollectionTestData(Object... elements) {
            this.elements = elements;
        }

        @Override
        public Iterator iterator() {
            return new IteratorTestData(Arrays.asList(elements));
        }

        @Override
        public boolean add(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(Object o) {
            for (Object element : elements) {
                if (o.equals(element)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean addAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return elements.length;
        }

        @Override
        public boolean isEmpty() {
            return elements.length == 0;
        }
    }

}