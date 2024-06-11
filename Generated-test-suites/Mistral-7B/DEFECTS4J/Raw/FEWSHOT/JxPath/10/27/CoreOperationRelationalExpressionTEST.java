package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Number left = 5;
        Number right = 10;
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(left), new CoreConstantExpression(right));

        assertTrue(expression.computeValue(new EvalContext()) instanceof Boolean);
        assertTrue(((Boolean) expression.computeValue(new EvalContext())).booleanValue());

        left = 5;
        right = 5;
        expression = new CoreOperationGreaterThan(new CoreConstantExpression(left), new CoreConstantExpression(right));

        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean);
        assertFalse(((Boolean) expression.computeValue(new EvalContext())).booleanValue());
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(1));

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(1));

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(1), new CoreConstantExpression(1));

        assertFalse(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
        assertTrue(expression.evaluateCompare(1));
    }

    @Test
    public void testComputeWithIterators() {
        Integer[] ints1 = {1, 2, 3};
        Integer[] ints2 = {3, 4, 5};
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreArrayExpression(new CoreConstantExpression(ints1), new CoreFunctionCallExpression(new CoreFunctionNameExpression("iterator"), new CoreArrayExpression(new CoreConstantExpression(ints1))), null), new CoreArrayExpression(new CoreConstantExpression(ints2), new CoreFunctionCallExpression(new CoreFunctionNameExpression("iterator"), new CoreArrayExpression(new CoreConstantExpression(ints2))), null));

        Iterator iterator1 = new ArrayIterator(ints1);
        Iterator iterator2 = new ArrayIterator(ints2);

        EvalContext context = new EvalContext();
        context.setCurrentNode(new SelfContext());
        context.setCurrentNodeIterator(iterator1);
        context.setVariable("iter2", iterator2);

        assertTrue(expression.computeValue(context) instanceof Boolean);
        assertTrue(((Boolean) expression.computeValue(context)).booleanValue());

        context.setCurrentNodeIterator(iterator2);
        context.setVariable("iter1", iterator1);

        assertFalse(expression.computeValue(context) instanceof Boolean);
        assertFalse(((Boolean) expression.computeValue(context)).booleanValue());
    }

    @Test
    public void testComputeWithCollections() {
        HashSet<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        HashSet<Integer> set2 = new HashSet<>();
        set2.add(3);
        set2.add(4);

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(set1), new CoreConstantExpression(set2));

        EvalContext context = new EvalContext();
        context.setCurrentNode(new SelfContext());
        context.setVariable("coll1", set1);
        context.setVariable("coll2", set2);

        assertFalse(expression.computeValue(context) instanceof Boolean);
        assertFalse(((Boolean) expression.computeValue(context)).booleanValue());
    }

    @Test
    public void testComputeWithInitialContexts() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(new InitialContext()), new CoreConstantExpression(new InitialContext()));

        EvalContext context = new EvalContext();
        context.setCurrentNode(new SelfContext());
        context.setCurrentNodeIterator(new ArrayIterator(new Object[0]));

        assertNull(expression.computeValue(context));
    }

    @Test
    public void testComputeWithNaN() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new CoreConstantExpression(Double.NaN), new CoreConstantExpression(Double.NaN));

        assertFalse(expression.computeValue(new EvalContext()) instanceof Boolean);
        assertFalse(((Boolean) expression.computeValue(new EvalContext())).booleanValue());
    }

    private static class ArrayIterator implements Iterator {
        private Object[] array;
        private int index;

        public ArrayIterator(Object[] array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public Object next() {
            return array[index++];
        }
    }
}