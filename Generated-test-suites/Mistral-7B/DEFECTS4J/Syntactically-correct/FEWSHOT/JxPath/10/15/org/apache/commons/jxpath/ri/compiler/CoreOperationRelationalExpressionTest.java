package org.apache.commons.jxpath.ri.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(new ConstantExpression(5), new ConstantExpression(10));
        EvalContext context = new EvalContext(new MockInfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThan(new ConstantExpression(10), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(6));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(4));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new ConstantExpression(5), new ConstantExpression(10));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThan(new ConstantExpression(10), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationLessThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(new ConstantExpression(5), new ConstantExpression(6));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationLessThanOrEqual(new ConstantExpression(5), new ConstantExpression(4));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        assertEquals(false, expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5), new ConstantExpression(5));
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(1));
        assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testComputeWithIterators() {
        List list1 = new ArrayList();
        list1.add(new Integer(1));
        list1.add(new Integer(2));
        list1.add(new Integer(3));
        list1.add(new Integer(4));
        list1.add(new Integer(5));

        List list2 = new ArrayList();
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        list2.add(new Integer(3));
        list2.add(new Integer(5));
        list2.add(new Integer(6));

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(list1.iterator(), list2.iterator());
        EvalContext context = new EvalContext(new MockInfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThanOrEqual(list1.iterator(), list2.iterator());
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThan(list2.iterator(), list1.iterator());
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThanOrEqual(list2.iterator(), list1.iterator());
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithCollections() {
        Collection collection1 = new HashSet();
        collection1.add(new Integer(1));
        collection1.add(new Integer(2));
        collection1.add(new Integer(3));
        collection1.add(new Integer(4));
        collection1.add(new Integer(5));

        Collection collection2 = new HashSet();
        collection2.add(new Integer(1));
        collection2.add(new Integer(2));
        collection2.add(new Integer(3));
        collection2.add(new Integer(5));
        collection2.add(new Integer(6));

        CoreOperationRelationalExpression expression = new CoreOperationGreaterThan(collection1.iterator(), collection2.iterator());
        EvalContext context = new EvalContext(new MockInfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThanOrEqual(collection1.iterator(), collection2.iterator());
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeWithDoubles() {
        CoreOperationRelationalExpression expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        EvalContext context = new EvalContext(new MockInfoSet());
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(6.0));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(4.5));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThanOrEqual(new ConstantExpression(5.0), new ConstantExpression(5.0));
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        expression = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(6.0));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        expression = new CoreOperationGreaterThan(new ConstantExpression(5.0), new ConstantExpression(5.0));
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    private static class MockInfoSet implements InfoSet {
        public int size() {
            return 0;
        }

        public Object getFirstChild() {
            return null;
        }

        public Iterator getIterator() {
            return new Iterator() {
                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    return null;
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public Object getSingleNodePointer() {
            return null;
        }

        public boolean getBooleanValue(int index) {
            return false;
        }

        public byte getByteValue(int index) {
            return 0;
        }

        public short getShortValue(int index) {
            return 0;
        }

        public int getIntValue(int index) {
            return 0;
        }

        public long getLongValue(int index) {
            return 0;
        }

        public float getFloatValue(int index) {
            return 0;
        }

        public double getDoubleValue(int index) {
            return 0;
        }

        public String getStringValue(int index) {
            return "";
        }

        public int getLength() {
            return 0;
        }
    }
}