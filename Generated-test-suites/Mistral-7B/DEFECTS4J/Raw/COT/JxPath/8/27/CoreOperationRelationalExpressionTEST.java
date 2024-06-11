package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression.CoreOperationRelationalExpressionFactory;

/**
 * Test cases for CoreOperationRelationalExpression class
 */
public class CoreOperationRelationalExpressionTest {

    @SuppressWarnings("unchecked")
    private Object[] getTestData() {
        Collection<Object> left = new ArrayList<>();
        left.add(new Integer(1));
        left.add(new Integer(2));
        left.add(new Integer(3));
        Collection<Object> right = new ArrayList<>();
        right.add(new Integer(1));
        right.add(new Integer(2));
        right.add(new Integer(3));
        right.add(new Integer(4));

        Object[] testData = new Object[8][2];
        testData[0] = new CoreOperationRelationalExpressionFactory()
                .newGreaterThanExpression(left.iterator(), right.iterator());
        testData[1] = Boolean.TRUE;

        testData[2] = new CoreOperationRelationalExpressionFactory()
                .newGreaterThanOrEqualExpression(left.iterator(), right.iterator());
        testData[3] = Boolean.TRUE;
        testData[3] = Boolean.TRUE; // for the case when left = right

        testData[4] = new CoreOperationRelationalExpressionFactory()
                .newLessThanExpression(left.iterator(), right.iterator());
        testData[5] = Boolean.FALSE;

        testData[6] = new CoreOperationRelationalExpressionFactory()
                .newLessThanOrEqualExpression(left.iterator(), right.iterator());
        testData[7] = Boolean.FALSE;
        testData[7] = Boolean.TRUE; // for the case when left = right

        return testData;
    }

    @org.junit.Test
    public void testGreaterThan() {
        Object[] testData = getTestData();
        for (Object testCase : testData) {
            CoreOperationRelationalExpression expression = (CoreOperationRelationalExpression) testCase[0];
            boolean expected = (boolean) testCase[1];
            EvalContext context = new EvalContext();
            Object result = expression.computeValue(context);
            assertEquals(expected, result);
        }
    }

    @org.junit.Test
    public void testGreaterThanOrEqual() {
        Object[] testData = getTestData();
        for (Object testCase : testData) {
            CoreOperationRelationalExpression expression = (CoreOperationRelationalExpression) testCase[0];
            boolean expected = (boolean) testCase[1];
            EvalContext context = new EvalContext();
            Object result = expression.computeValue(context);
            assertEquals(expected, result);
            // Test with left == right
            context = new EvalContext();
            left.clear();
            left.addAll(right);
            result = expression.computeValue(context);
            assertEquals(expected, result);
        }
    }

    @org.junit.Test
    public void testLessThan() {
        Object[] testData = getTestData();
        for (Object testCase : testData) {
            CoreOperationRelationalExpression expression = (CoreOperationRelationalExpression) testCase[0];
            boolean expected = (boolean) testCase[1];
            EvalContext context = new EvalContext();
            Object result = expression.computeValue(context);
            assertEquals(expected, result);
        }
    }

    @org.junit.Test
    public void testLessThanOrEqual() {
        Object[] testData = getTestData();
        for (Object testCase : testData) {
            CoreOperationRelationalExpression expression = (CoreOperationRelationalExpression) testCase[0];
            boolean expected = (boolean) testCase[1];
            EvalContext context = new EvalContext();
            Object result = expression.computeValue(context);
            assertEquals(expected, result);
            // Test with left == right
            EvalContext context2 = new EvalContext();
            left.clear();
            left.addAll(right);
            result = expression.computeValue(context2);
            assertTrue(expected || (expected && left.size() > 1));
        }
    }

    @org.junit.Test(expected = java.lang.ClassCastException.class)
    public void testWithInitialContext() {
        EvalContext context = new EvalContext();
        InitialContext left = new InitialContext();
        InitialContext right = new InitialContext();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationFactory().newInitialContextExpression(left),
                new CoreOperationFactory().newInitialContextExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        expression.computeValue(context);
    }

    @org.junit.Test(expected = java.lang.ClassCastException.class)
    public void testWithSelfContext() {
        EvalContext context = new EvalContext();
        SelfContext left = new SelfContext();
        Object right = new Integer(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationFactory().newSelfContextExpression(),
                new CoreOperationFactory().newLiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        expression.computeValue(context);
    }

    @org.junit.Test(expected = java.lang.ClassCastException.class)
    public void testWithIteratorAndNonIterator() {
        EvalContext context = new EvalContext();
        Iterator left = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new UnsupportedOperationException();
            }
        }.iterator();
        Object right = new Integer(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                left, new CoreOperationFactory().newLiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        expression.computeValue(context);
    }

    @org.junit.Test(expected = java.lang.ClassCastException.class)
    public void testWithNonIteratorAndIterator() {
        EvalContext context = new EvalContext();
        Object left = new Integer(2);
        Iterator right = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new UnsupportedOperationException();
            }
        }.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationFactory().newLiteralExpression(left), right) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        expression.computeValue(context);
    }

    @org.junit.Test(expected = java.lang.ClassCastException.class)
    public void testWithTwoIteratorsDifferentSize() {
        EvalContext context = new EvalContext();
        Iterator left = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        Iterator right = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new UnsupportedOperationException();
            }
        }.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                left, right) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        expression.computeValue(context);
    }

    @org.junit.Test(expected = java.lang.ClassCastException.class)
    public void testWithTwoIteratorsSameElement() {
        EvalContext context = new EvalContext();
        Iterator left = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        Iterator right = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                left, right) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        expression.computeValue(context);
    }

    @org.junit.Test
    public void testWithTwoIteratorsNoMatch() {
        EvalContext context = new EvalContext();
        Iterator left = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        Iterator right = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        }.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                left, right) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        assertFalse(expression.computeValue(context).booleanValue());
    }

    @org.junit.Test
    public void testWithTwoIteratorsMatch() {
        EvalContext context = new EvalContext();
        Iterator left = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        Iterator right = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                left, right) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @org.junit.Test
    public void testWithTwoIteratorsSameElementDifferentOrder() {
        EvalContext context = new EvalContext();
        Iterator left = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        }.iterator();
        Iterator right = new ArrayList<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                final Object element = super.next();
                super.next();
                return element;
            }
        }.iterator();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                left, right) {
            @Override
            protected boolean evaluateCompare(int compare) {
                throw new UnsupportedOperationException();
            }
        };
        assertTrue(expression.computeValue(context).booleanValue());
    }

    @org.junit.Test
    public void testWithTwoDoubles() {
        double left = Double.parseDouble("2.0");
        double right = Double.parseDouble("1.5");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationFactory().newLiteralExpression(left),
                new CoreOperationFactory().newLiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                switch (compare) {
                    case 0:
                        return false;
                    case -1:
                        return left < right;
                    case 1:
                        return left > right;
                    default:
                        throw new AssertionError("Invalid compare value: " + compare);
                }
            }
        };
        assertFalse(expression.computeValue(null).booleanValue());
        assertTrue(expression.computeValue(new EvalContext()).booleanValue());
    }

    @org.junit.Test
    public void testWithTwoStrings() {
        String left = "2";
        String right = "1.5";
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(
                new CoreOperationFactory().newLiteralExpression(left),
                new CoreOperationFactory().newLiteralExpression(right)) {
            @Override
            protected boolean evaluateCompare(int compare) {
                switch (compare) {
                    case 0:
                        return false;
                    case -1:
                        return InfoSetUtil.compareStrings(left, right) < 0;
                    case 1:
                        return InfoSetUtil.compareStrings(left, right) > 0;
                    default:
                        throw new AssertionError("Invalid compare value: " + compare);
                }
            }
        };
        assertFalse(expression.computeValue(null).booleanValue());
        assertFalse(expression.computeValue(new EvalContext()).booleanValue());
    }

}