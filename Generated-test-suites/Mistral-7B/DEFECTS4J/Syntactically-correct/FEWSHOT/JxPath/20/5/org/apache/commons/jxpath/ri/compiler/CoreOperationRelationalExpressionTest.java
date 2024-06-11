package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression left = new Literal(new SelfContext(EvalContext.NULL, "5"));
        Expression right = new Literal(new SelfContext(EvalContext.NULL, "5"));
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[0]);
        Object result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, "5"));
        right = new Literal(new SelfContext(EvalContext.NULL, "6"));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, "5"));
        right = new Literal(new SelfContext(EvalContext.NULL, "5"));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, "5"));
        right = new Literal(new SelfContext(EvalContext.NULL, "4"));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, "5.1"));
        right = new Literal(new SelfContext(EvalContext.NULL, "5.0"));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, "5.1"));
        right = new Literal(new SelfContext(EvalContext.NULL, "5.1"));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, "5.1"));
        right = new Literal(new SelfContext(EvalContext.NULL, "5.2"));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, Double.NaN));
        right = new Literal(new SelfContext(EvalContext.NULL, Double.NaN));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, Double.NaN));
        right = new Literal(new SelfContext(EvalContext.NULL, Double.NaN));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, ""));
        right = new Literal(new SelfContext(EvalContext.NULL, ""));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        left = new Literal(new SelfContext(EvalContext.NULL, ""));
        right = new Literal(new SelfContext(EvalContext.NULL, "abc"));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.computeValue(new EvalContext());
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCompute() {
        Object left = new SelfContext(EvalContext.NULL, "5");
        Object right = new SelfContext(EvalContext.NULL, "5");
        CoreOperationRelationalExpression expression = new GreaterThanExpression(new CoreOperation[0]);
        boolean result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new SelfContext(EvalContext.NULL, "6");
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new SelfContext(EvalContext.NULL, "5");
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new SelfContext(EvalContext.NULL, "4");
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 5.1);
        right = new SelfContext(EvalContext.NULL, 5.0);
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 5.1);
        right = new SelfContext(EvalContext.NULL, 5.1);
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 5.1);
        right = new SelfContext(EvalContext.NULL, 5.2);
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, Double.NaN);
        right = new SelfContext(EvalContext.NULL, Double.NaN);
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, Double.NaN);
        right = new SelfContext(EvalContext.NULL, Double.NaN);
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, "");
        right = new SelfContext(EvalContext.NULL, "");
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, "");
        right = new SelfContext(EvalContext.NULL, "abc");
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        right = new SelfContext(EvalContext.NULL, 4);
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        right = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        right = new SelfContext(EvalContext.NULL, Collections.singleton(4));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        right = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        right = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(4));
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(1, 2, 3));
        right = new SelfContext(EvalContext.NULL, (Collection) Arrays.asList(4));
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new InitialContext();
        right = new InitialContext();
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new InitialContext();
        right = new SelfContext(EvalContext.NULL, "5");
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new InitialContext();
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new IteratorWrapper(Collections.singleton(new SelfContext(EvalContext.NULL, "5")).iterator());
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new IteratorWrapper(Arrays.asList(new SelfContext(EvalContext.NULL, "2"), new SelfContext(EvalContext.NULL, "5")).iterator());
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new IteratorWrapper(Arrays.asList(new SelfContext(EvalContext.NULL, "2"), new SelfContext(EvalContext.NULL, "5"), new SelfContext(EvalContext.NULL, "5")).iterator());
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new IteratorWrapper(Arrays.asList(new SelfContext(EvalContext.NULL, "2"), new SelfContext(EvalContext.NULL, "5"), new SelfContext(EvalContext.NULL, "6")).iterator());
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, "5");
        right = new IteratorWrapper(Arrays.asList(new SelfContext(EvalContext.NULL, "2"), new SelfContext(EvalContext.NULL, "5"), new SelfContext(EvalContext.NULL, "5"), new SelfContext(EvalContext.NULL, "6")).iterator());
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, 'b');
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, 'a');
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, 'z');
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, 'z');
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, '\\u00E5');
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, '\\u00E5');
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertTrue(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, "\uD800\uDC00");
        expression = new GreaterThanExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);

        left = new SelfContext(EvalContext.NULL, 'a');
        right = new SelfContext(EvalContext.NULL, "\uD800\uDC00");
        expression = new GreaterThanOrEqualExpression(new CoreOperation[0]);
        result = expression.compute(left, right);
        assertFalse(result);
    }

    private static class IteratorWrapper implements Iterator {
        Iterator inner;

        public IteratorWrapper(Iterator inner) {
            this.inner = inner;
        }

        public boolean hasNext() {
            return inner.hasNext();
        }

        public Object next() {
            return inner.next();
        }

        public void remove() {
            inner.remove();
        }
    }
}