package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {

    private Expression expression;

    @Before
    public void setUp() {
        expression = new TestExpression();
    }

    @Test
    public void testIsContextDependent() {
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        assertFalse(expression.computeContextDependent());
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new TestEvalContext();
        Object result = expression.computeValue(context);
        assertEquals("test", result);
    }

    @Test
    public void testCompute() {
        EvalContext context = new TestEvalContext();
        Object result = expression.compute(context);
        assertEquals("test", result);
    }

    // Custom TestExpression class extending Expression

    private class TestExpression extends Expression {

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return "test";
        }

        @Override
        public Object compute(EvalContext context) {
            return "test";
        }
    }

    // Custom TestEvalContext class extending EvalContext

    private class TestEvalContext extends EvalContext {

        @Override
        public NodePointer getContextNodePointer() {
            return null;
        }

        @Override
        public NodePointer getRootContextNodePointer() {
            return null;
        }

        @Override
        public EvalContext cloneContext() {
            return null;
        }
    }
}