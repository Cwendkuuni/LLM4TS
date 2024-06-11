package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.util.TestUtils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testAbstractExpression_isContextDependent_default() {
        Expression expression = new AbstractExpression() {
            // do nothing
        };
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testAbstractExpression_isContextDependent_overridden() {
        Expression expression = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testAbstractExpression_computeValue() {
        Expression expression = new AbstractExpression() {
            @Override
            public Object computeValue(EvalContext context) {
                return null;
            }
        };
        assertNull(expression.computeValue(TestUtils.createEvalContext()));
    }

    @Test
    public void testAbstractExpression_compute_default() {
        Expression expression = new AbstractExpression() {
            // do nothing
        };
        assertNull(expression.compute(TestUtils.createEvalContext()));
    }

    @Test
    public void testAbstractExpression_compute_overridden() {
        Expression expression = new AbstractExpression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        };
        assertNull(expression.compute(TestUtils.createEvalContext()));
    }

    @Test
    public void testAbstractExpression_iterate() {
        Expression expression = new AbstractExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                return ValueUtils.iterate(new Object());
            }
        };
        Iterator iterator = expression.iterate(TestUtils.createEvalContext());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAbstractExpression_iteratePointers_default() {
        Expression expression = new AbstractExpression() {
            // do nothing
        };
        assertFalse(expression.iteratePointers(TestUtils.createEvalContext()).hasNext());
    }

    @Test
    public void testAbstractExpression_iteratePointers_overridden() {
        Expression expression = new AbstractExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return ValueUtils.iterate(new Object()).iterator();
            }
        };
        Iterator iterator = expression.iteratePointers(TestUtils.createEvalContext());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }
}