package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertNotNull(expression);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(2.0)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext(new Object[0])));

        expression = new CoreOperationRelationalExpression(new Expression[]{
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(1.0)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        assertEquals(Boolean.TRUE, expression.computeValue(new EvalContext(new Object[0])));

        expression = new CoreOperationRelationalExpression(new Expression[]{
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(2.0)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        assertEquals(Boolean.FALSE, expression.computeValue(new EvalContext(new Object[0])));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertTrue(expression.evaluateCompare(1));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(0));
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(2.0)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        Object left = new CoreOperationConstant(1.0);
        Object right = new CoreOperationConstant(2.0);
        assertTrue(expression.compute(left, right));

        left = new CoreOperationConstant(2.0);
        right = new CoreOperationConstant(1.0);
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(1.0);
        right = new CoreOperationConstant(1.0);
        assertTrue(expression.compute(left, right));

        left = new CoreOperationConstant(1.0);
        right = new CoreOperationConstant(1.0);
        assertTrue(expression.compute(right, left));

        left = new CoreOperationConstant(1.0);
        right = new CoreOperationConstant(1.0);
        right = InfoSetUtil.createNumber(right, Double.NaN);
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(1.0);
        right = new CoreOperationConstant(1.0);
        right = InfoSetUtil.createNumber(right, Double.POSITIVE_INFINITY);
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(1.0);
        right = new CoreOperationConstant(1.0);
        right = InfoSetUtil.createNumber(right, Double.NEGATIVE_INFINITY);
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(1.0);
        right = new CoreOperationConstant(null);
        assertNull(expression.compute(left, right));

        left = new CoreOperationConstant(null);
        right = new CoreOperationConstant(1.0);
        assertNull(expression.compute(left, right));

        left = new CoreOperationConstant(new InitialContext());
        right = new CoreOperationConstant(new InitialContext());
        assertEquals(left, right);

        left = new CoreOperationConstant(new SelfContext());
        right = new CoreOperationConstant(new SelfContext());
        assertEquals(left, right);

        Collection<Object> leftCollection = new ArrayList<Object>();
        leftCollection.add(new CoreOperationConstant(1.0));
        leftCollection.add(new CoreOperationConstant(2.0));
        Iterator<Object> leftIterator = leftCollection.iterator();

        Object right = new CoreOperationConstant
                (InfoSetUtil.createNumber(new CoreOperationConstant(1.0).computeValue(new EvalContext(new Object[0])), 3.0));

        assertTrue(expression.compute(leftIterator, right));

        left = new CoreOperationConstant(new InitialContext());
        right = new CoreOperationConstant(new InitialContext());
        ((InitialContext) left).reset();
        ((InitialContext) right).reset();
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(1.0);
            }
        });
        right = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(2.0);
            }
        });
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(1.0);
            }
        });
        right = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(1.0);
            }
        });
        assertTrue(expression.compute(left, right));

        left = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(1.0);
            }
        });
        right = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(2.0);
            }
        });
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreOperationConstant(1.0);
            }
        });
        right = new CoreOperationConstant(new HashSet<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(2.0));
        }}.iterator());
        assertFalse(expression.compute(left, right));

        left = new CoreOperationConstant(new HashSet<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
        }}.iterator());
        right = new CoreOperationConstant(new CoreOperationConstant(1.0));
        assertTrue(expression.compute(left, right));

        left = new CoreOperationConstant(new HashSet<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
        }}.iterator());
        right = new CoreOperationConstant(new CoreOperationConstant(2.0));
        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[0]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        assertEquals(expression.reduce(new CoreOperationConstant(1.0)), new CoreOperationConstant(1.0));
        assertEquals(expression.reduce(new CoreOperationConstant(new InitialContext())), new InitialContext());
        assertEquals(expression.reduce(new CoreOperationConstant(new SelfContext())), new SelfContext());
        assertEquals(expression.reduce(new CoreOperationConstant(new ArrayList<Object>())), new CoreOperationConstant(new ArrayList<Object>().iterator()));
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
                new CoreOperationConstant(1.0)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        Iterator<Object> it = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
        }}.iterator();
        Object value = new CoreOperationConstant(1.0);
        assertTrue(expression.containsMatch(it, value));

        it = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(2.0));
        }}.iterator();
        assertFalse(expression.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{
                new CoreOperationConstant(1.0),
                new CoreOperationConstant(2.0)
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return evaluateCompare(compare) == Boolean.TRUE;
            }
        };
        Iterator<Object> lit = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(2.0));
        }}.iterator();
        Iterator<Object> rit = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
        }}.iterator();
        assertTrue(expression.findMatch(lit, rit));

        lit = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(2.0));
        }}.iterator();
        rit = new ArrayList<Object>() {{
            add(new CoreOperationConstant(2.0));
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
        }}.iterator();
        assertFalse(expression.findMatch(lit, rit));

        lit = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(2.0));
        }}.iterator();
        rit = new ArrayList<Object>() {{
            add(new CoreOperationConstant(1.0));
            add(new CoreOperationConstant(2.0));
            add(new CoreOperationConstant(1.0));
        }}.iterator();
        assertTrue(expression.findMatch(lit, rit));
    }

}