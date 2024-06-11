package org.apache.commons.jxpath.ri.compiler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new LiteralExpression(new SelfContext(new Integer(1)));
        Expression right = new LiteralExpression(new SelfContext(new Integer(2)));
        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new LiteralExpression(new SelfContext(new Integer(3)));
        right = new LiteralExpression(new SelfContext(new Integer(2)));
        expression = new GtExpression(left, right);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new LiteralExpression(new SelfContext(new Integer(1)));
        right = new LiteralExpression(new SelfContext(new Integer(1)));
        expression = new EqExpression(left, right);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new GtExpression(null, null);
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new GtExpression(null, null);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new GtExpression(null, null);
        assertTrue(expression.evaluateCompare(1) == -1);
        assertTrue(expression.evaluateCompare(0) == -1);
        assertTrue(expression.evaluateCompare(-1) == 1);
        assertTrue(expression.evaluateCompare(0) == 0);
        assertTrue(expression.evaluateCompare(1) == 0);
        assertTrue(expression.evaluateCompare(1) == 0);
    }

    @Test
    public void testComputeWithInitialContext() {
        Expression left = new LiteralExpression(new SelfContext(new Integer(1)));
        Expression right = new LiteralExpression(new InitialContext());
        CoreOperationRelationalExpression expression = new GtExpression(left, right);
        assertFalse(expression.compute(null, null));
    }

    @Test
    public void testComputeWithIteratorAndCollection() {
        Collection<Integer> left = new HashSet<>();
        left.add(new Integer(1));
        Iterator<Object> leftIterator = left.iterator();

        Iterator<Object> rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new UnsupportedOperationException();
            }
        };

        CoreOperationRelationalExpression expression = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression.compute(null, null));

        left.add(new Integer(2));

        CoreOperationRelationalExpression expression2 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression2.compute(null, null));

        right = new LiteralExpression(new SelfContext(new Integer(2)));
        rightIterator = right.iterator();

        CoreOperationRelationalExpression expression3 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression3.compute(null, null));

        left.clear();
        left.add(new Integer(1));
        left.add(new Integer(2));

        CoreOperationRelationalExpression expression4 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(right));
        assertTrue(expression4.compute(null, null));
    }

    @Test
    public void testComputeWithIterators() {
        Iterator<Object> leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                throw new UnsupportedOperationException();
            }
        };

        Iterator<Object> rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression2 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression2.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression3 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression3.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression4 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression4.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression5 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression5.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression6 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression6.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression7 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression7.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression8 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression8.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression9 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression9.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression10 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression10.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression11 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression11.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        CoreOperationRelationalExpression expression12 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertTrue(expression12.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression13 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression13.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression14 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression14.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression15 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression15.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression16 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression16.compute(null, null));

        leftIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };

        rightIterator = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };

        CoreOperationRelationalExpression expression17 = new GtExpression(new LiteralExpression(leftIterator), new LiteralExpression(rightIterator));
        assertFalse(expression17.compute(null, null));
    }

}