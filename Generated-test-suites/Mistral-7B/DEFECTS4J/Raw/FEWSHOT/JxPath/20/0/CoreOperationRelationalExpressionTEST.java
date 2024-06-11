package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * JUnit test cases for CoreOperationRelationalExpression class.
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testCoreOperationRelationalExpression() {
        assertNotNull(CoreOperationRelationalExpression.class);
    }

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationAdd(new CoreNumber(3.0), new CoreNumber(4.0));
        EvalContext context = new EvalContext(new SelfContext(1), new InitialContext());
        Object result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.FALSE);

        expr = new CoreOperationSubtract(new CoreNumber(3.0), new CoreNumber(4.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.TRUE);

        expr = new CoreOperationMultiply(new CoreNumber(3.0), new CoreNumber(4.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.TRUE);

        expr = new CoreOperationDivide(new CoreNumber(3.0), new CoreNumber(4.0));
        result = expr.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals((Boolean) result, Boolean.FALSE);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        int precedence = expr.getPrecedence();
        assertEquals(precedence, CoreOperationRelationalExpression.RELATIONAL_EXPR_PRECEDENCE);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        boolean symmetric = expr.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expr = new CoreGreaterThan();
        assertTrue(expr instanceof CoreOperationRelationalExpression);
        boolean result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(1);
        assertTrue(result);

        result = expr.evaluateCompare(-1);
        assertFalse(result);

        expr = new CoreOperationGreaterThanOrEqual();
        result = expr.evaluateCompare(0);
        assertFalse(result);

        result = expr.evaluateCompare(1);
        assertTrue(result);

        result = expr.evaluateCompare(-1);
        assertFalse(result);

        result = expr.evaluateCompare(0);
        assertTrue(result);

        expr = new CoreOperationLessThan();
        result = expr.evaluateCompare(0);
        assertTrue(result);

        result = expr.evaluateCompare(1);
        assertFalse(result);

        result = expr.evaluateCompare(-1);
        assertTrue(result);

        expr = new CoreOperationLessThanOrEqual();
        result = expr.evaluateCompare(0);
        assertTrue(result);

        result = expr.evaluateCompare(1);
        assertFalse(result);

        result = expr.evaluateCompare(-1);
        assertTrue(result);

        result = expr.evaluateCompare(0);
        assertTrue(result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new Expression[0]);
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(4.0)), Boolean.FALSE);

        expr = new CoreGreaterThan();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(4.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(4.0)), Boolean.FALSE);

        expr = new CoreGreaterThanOrEqual();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(4.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(4.0)), Boolean.FALSE);

        expr = new CoreOperationGreaterThan();
        expr.args = new Expression[]{new CoreNumber(4.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(4.0), new CoreNumber(3.0)), Boolean.TRUE);

        expr = new CoreOperationGreaterThanOrEqual();
        expr.args = new Expression[]{new CoreNumber(4.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(4.0), new CoreNumber(3.0)), Boolean.TRUE);

        expr = new CoreOperationGreaterThan();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(3.0)), Boolean.FALSE);

        expr = new CoreOperationGreaterThanOrEqual();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(3.0)), Boolean.TRUE);

        expr = new CoreOperationLessThan();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(4.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(4.0)), Boolean.FALSE);

        expr = new CoreOperationLessThanOrEqual();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(4.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(4.0)), Boolean.FALSE);

        expr = new CoreOperationLessThan();
        expr.args = new Expression[]{new CoreNumber(4.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(4.0), new CoreNumber(3.0)), Boolean.TRUE);

        expr = new CoreOperationLessThanOrEqual();
        expr.args = new Expression[]{new CoreNumber(4.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(4.0), new CoreNumber(3.0)), Boolean.TRUE);

        expr = new CoreOperationLessThan();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(3.0)), Boolean.FALSE);

        expr = new CoreOperationLessThanOrEqual();
        expr.args = new Expression[]{new CoreNumber(3.0), new CoreNumber(3.0)};
        assertEquals(expr.compute(new CoreNumber(3.0), new CoreNumber(3.0)), Boolean.TRUE);
    }

    @Test
    public void testReduce() {
        Object o = new CoreNumber(3.0);
        Object reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0])).reduce(o);
        assertEquals(o, reduced);

        o = new SelfContext(1);
        reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0])).reduce(o);
        assertNull(reduced);

        o = new InitialContext();
        reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0])).reduce(o);
        assertNull(reduced);

        o = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0])).reduce(o);
        assertEquals(o, reduced);

        o = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        reduced = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0])).reduce(o);
        assertNull(reduced);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expr = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0]));
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        Object value = new CoreNumber(3.0);
        boolean result = expr.containsMatch(it, value);
        assertTrue(result);

        it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(4.0);
            }
        };
        result = expr.containsMatch(it, value);
        assertFalse(result);

        it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        value = new CoreNumber(4.0);
        result = expr.containsMatch(it, value);
        assertFalse(result);

        it = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        value = new CoreNumber(3.0);
        result = expr.containsMatch(it, value);
        assertTrue(result);

        it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        result = expr.containsMatch(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expr = CoreOperationRelationalExpression.class.cast(new CoreOperationRelationalExpression(new Expression[0]));
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        boolean result = expr.findMatch(lit, rit);
        assertTrue(result);

        lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(4.0);
            }
        };
        result = expr.findMatch(lit, rit);
        assertFalse(result);

        lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        result = expr.findMatch(lit, rit);
        assertTrue(result);

        lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new CoreNumber(3.0);
            }
        };
        result = expr.findMatch(lit, rit);
        assertFalse(result);
    }
}