package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathTestCase;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.beans.BeanPointerFactory;

import java.util.Iterator;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {
    private Expression expression;
    private EvalContext context;

    @Before
    public void setUp() {
        expression = new ConcreteExpression();
        context = new EvalContext(JXPathContext.newContext(new Object()), Locale.getDefault());
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeContextDependent() {
        assertFalse(expression.computeContextDependent());
    }

    @Test
    public void testComputeValue() {
        Object value = expression.computeValue(context);
        assertEquals("test", value);
    }

    @Test
    public void testCompute() {
        Object value = expression.compute(context);
        assertEquals("test", value);
    }

    @Test
    public void testIterate() {
        Iterator iterator = expression.iterate(context);
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Iterator iterator = expression.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    public class ConcreteExpression extends Expression {
        public boolean computeContextDependent() {
            return false;
        }

        public Object computeValue(EvalContext context) {
            return "test";
        }

        public Object compute(EvalContext context) {
            return "test";
        }
    }
}