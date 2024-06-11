package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    @Test
    public void testContextDependency() {
        AbstractExpression exp1 = new MyContextDependentExpression();
        AbstractExpression exp2 = new MyContextIndependentExpression();

        EvalContext context = new EvalContext(null, null);

        assertFalse(exp1.isContextDependent());
        assertFalse(exp2.isContextDependent());

        context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "root", null, 0));

        assertTrue(exp1.isContextDependent());
        assertFalse(exp2.isContextDependent());

        context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "root2", null, 0));

        assertTrue(exp1.isContextDependent());
        assertFalse(exp2.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        AbstractExpression exp = new MyContextIndependentExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "root", null, 0));

        assertEquals("expectedValue", exp.computeValue(context));
    }

    @Test
    public void testCompute() {
        AbstractExpression exp = new MyContextIndependentExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "root", null, 0));

        assertEquals("expectedValue", exp.compute(context));
    }

    @Test
    public void testIterate() {
        AbstractExpression exp = new MyContextIndependentExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "root", null, 0));

        Iterator result = exp.iterate(context);

        assertTrue(result.hasNext());
        assertEquals("expectedValue1", result.next());

        assertTrue(result.hasNext());
        assertEquals("expectedValue2", result.next());

        assertFalse(result.hasNext());
    }

    @Test
    public void testIteratePointers() {
        AbstractExpression exp = new MyContextIndependentExpression();

        EvalContext context = new EvalContext(null, null);
        context.setCurrentNodePointer(new NodePointer(null, "root", null, 0));

        Iterator result = exp.iteratePointers(context);

        assertTrue(result.hasNext());
        assertTrue(result.next() instanceof Pointer);

        assertFalse(result.hasNext());
    }

    private abstract static class AbstractExpression extends Expression {
        public abstract Object computeValue(EvalContext context);
        public abstract Object compute(EvalContext context);
    }

    private static class MyContextDependentExpression extends AbstractExpression {
        @Override
        public boolean computeContextDependent() {
            return true;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getCurrentNodePointer().getNodeName();
        }
    }

    private static class MyContextIndependentExpression extends AbstractExpression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return "expectedValue";
        }

        @Override
        public Object compute(EvalContext context) {
            return computeValue(context);
        }
    }
}