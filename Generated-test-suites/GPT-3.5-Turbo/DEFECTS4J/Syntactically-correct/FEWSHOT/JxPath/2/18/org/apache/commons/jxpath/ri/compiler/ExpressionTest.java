package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {
    private Expression expression;

    @Before
    public void setUp(){
          expression = new TestExpression();
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
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals("testResult", result);
    }

    @Test
    public void testCompute() {
        EvalContext context = new EvalContext();
        Object result = expression.compute(context);
        assertEquals("testResult", result);
    }

    @Test
    public void testIterate() {
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iterate(context);
        assertTrue(iterator.hasNext());
        assertEquals("testValue", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers() {
        EvalContext context = new EvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertTrue(iterator.hasNext());
        assertEquals("testPointer", iterator.next());
        assertFalse(iterator.hasNext());
    }

    public class TestExpression extends Expression {

        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return "testResult";
        }

        @Override
        public Object compute(EvalContext context) {
            return "testResult";
        }

        @Override
        public Iterator iterate(EvalContext context) {
            return Collections.singleton("testValue").iterator();
        }

        @Override
        public Iterator iteratePointers(EvalContext context) {
            return Collections.singleton("testPointer").iterator();
        }
    }
}