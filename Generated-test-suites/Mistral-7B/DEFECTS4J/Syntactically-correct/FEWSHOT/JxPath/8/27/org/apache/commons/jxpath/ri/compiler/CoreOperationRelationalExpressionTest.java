package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.iter.IterIdentifier;
import org.apache.commons.jxpath.ri.iter.Iterators;
import org.apache.commons.jxpath.ri.iter.SequenceIterator;
import org.apache.commons.jxpath.ri.util.Iterators2;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext(new InfoSet())));

        expression = new GreaterThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext(new InfoSet())));

        expression = new LessThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertEquals(Boolean.FALSE, expression.computeValue(new EvalContext(new InfoSet())));

        expression = new LessThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext(new InfoSet())));

        Expression left = new ArrayIndexExpression(new VariableExpression("x"), new ConstExpression(2));
        Expression right = new ConstExpression(5);
        expression = new GreaterThanExpression(left, right);
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator getSingleNodePointer() {
                return Iterators.emptyIterator();
            }

            @Override
            public Iterator getIterator(IterIdentifier id) {
                return id.iterator();
            }

            @Override
            public Object getValue(IterIdentifier id) {
                return new Object[]{1, 2, 3, 4, 5};
            }
        });
        SequenceIterator iterator = (SequenceIterator) context.getIterator(Iterators.makeIdentified("x"));
        assertTrue(expression.computeValue(context).equals(Boolean.TRUE));
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.isSymmetric());

        expression = new LessThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.isSymmetric());

        expression = new GreaterThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.isSymmetric());

        expression = new LessThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));

        expression = new GreaterThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));

        expression = new LessThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));

        expression = new LessThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertTrue(expression.compute(3.0, 5.0));
        assertTrue(expression.compute(3.1, 5.0));
        assertFalse(expression.compute(3.0, 4.999999));

        expression = new GreaterThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertTrue(expression.compute(3.0, 5.0));
        assertTrue(expression.compute(3.1, 5.0));
        assertTrue(expression.compute(3.0, 5.0));
        assertTrue(expression.compute(3.0, 5.1));

        expression = new LessThanExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.compute(3.0, 5.0));
        assertFalse(expression.compute(3.1, 5.0));
        assertTrue(expression.compute(2.999999, 5.0));

        expression = new LessThanOrEqualExpression(new ConstExpression(3), new ConstExpression(5));
        assertFalse(expression.compute(3.0, 5.0));
        assertFalse(expression.compute(3.1, 5.0));
        assertTrue(expression.compute(2.999999, 5.0));
        assertTrue(expression.compute(3.0, 5.0));
    }

    @Test
    public void testReduce() {
        Object left = new ConstExpression(3);
        Object right = new ConstExpression(5);
        assertEquals(left, expression.reduce(left));
        assertEquals(right, expression.reduce(right));

        Expression leftIter = new ArrayIndexExpression(new VariableExpression("x"), new ConstExpression(2));
        Expression rightIter = new ConstExpression(5);
        assertEquals(leftIter, expression.reduce(leftIter));
        assertEquals(rightIter, expression.reduce(rightIter));
    }

    @Test
    public void testContainsMatch() {
        Object[] elements = {1, 2, 3, 4, 5};
        Iterator iterator = Iterators2.asIterator(elements);
        Object value = new ConstExpression(5);
        assertTrue(expression.containsMatch(iterator, value));
        assertFalse(expression.containsMatch(iterator, new ConstExpression(2)));
        assertTrue(expression.containsMatch(Iterators.emptyIterator(), value));
    }

    @Test
    public void testFindMatch() {
        Object[] leftElements = {1, 2, 3, 4, 5};
        Object[] rightElements = {2, 4, 5, 6, 7};
        Iterator leftIterator = Iterators2.asIterator(leftElements);
        Iterator rightIterator = Iterators2.asIterator(rightElements);
        HashSet left = new HashSet();
        while (leftIterator.hasNext()) {
            left.add(leftIterator.next());
        }
        assertTrue(expression.findMatch(leftIterator, rightIterator));
        assertFalse(expression.findMatch(leftIterator, Iterators.emptyIterator()));
        assertFalse(expression.findMatch(Iterators.emptyIterator(), leftIterator));
        assertFalse(expression.findMatch(Iterators.emptyIterator(), rightIterator));
    }
}