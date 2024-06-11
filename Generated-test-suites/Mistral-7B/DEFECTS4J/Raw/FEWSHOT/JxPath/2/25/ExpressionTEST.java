package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

public class ExpressionTest {

    @Test
    public void testAbstractExpressionIsContextDependent() {
        Expression expression = new AbstractExpression() {
        };
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testExpressionWithContextDependentComputeContextDependentIsContextDependent() {
        Expression expression = new TestContextDependentExpression();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testExpressionWithContextIndependentComputeContextDependentIsContextIndependent() {
        Expression expression = new TestContextIndependentExpression();
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValueWithContext() {
        Expression expression = new TestContextIndependentExpression();
        Object value = expression.computeValue(new EvalContext(new NodePointer("root")));
        assertNotNull(value);
    }

    @Test
    public void testIterateWithContext() {
        Expression expression = new TestContextIndependentExpression();
        Iterator iterator = expression.iterate(new EvalContext(new NodePointer("root")));
        assertNotNull(iterator);
        iterator.hasNext(); //consume first element
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointersWithContext() {
        Expression expression = new TestContextIndependentExpression();
        Iterator iterator = expression.iteratePointers(new EvalContext(new NodePointer("root")));
        assertNotNull(iterator);
        iterator.hasNext(); //consume first element
        assertFalse(iterator.hasNext());
    }

    private static class AbstractExpression {
    }

    private static class TestContextDependentExpression extends AbstractExpression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }
    }

    private static class TestContextIndependentExpression extends AbstractExpression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return Utils.EMPTY_STRING_ARRAY;
        }

        @Override
        public Object compute(EvalContext context) {
            return Utils.EMPTY_STRING_ARRAY;
        }
    }
}