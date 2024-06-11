package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link org.apache.commons.jxpath.ri.compiler.Expression}.
 */
public class ExpressionTest {

    private Expression expression;
    private EvalContext context;

    @Before
    public void setUp() {
        expression = new StaticExpression();
        context = new EvalContext(new Object(), new QName("root"), new NodePointer("root", null, null, null), null);
    }

    @Test
    public void testIsContextDependent() {
        assertFalse(expression.isContextDependent());
    }

    @Test
    public void testComputeValueStatic() {
        Object result = expression.computeValue(context);
        assertSame(VALUE, result);
    }

    @Test
    public void testComputeValueDynamic() {
        Expression dynamicExpression = new DynamicExpression();
        Object result = dynamicExpression.computeValue(context);
        assertSame(VALUE, result);

        context = new EvalContext(new Object(), new QName("root2"), new NodePointer("root2", null, null, null), null);
        result = dynamicExpression.computeValue(context);
        assertNotSame(VALUE, result);
    }

    @Test
    public void testCompute() {
        Object result = expression.compute(context);
        assertSame(VALUE, result);
    }

    @Test
    public void testIterate() {
        Iterator iter = expression.iterate(context);
        assertTrue(iter.hasNext());
        assertSame(VALUE, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testIteratePointers() {
        Iterator iter = expression.iteratePointers(context);
        assertTrue(iter.hasNext());
        assertSame(POINTER, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testPointerIterator() {
        Iterator iter = new Expression.PointerIterator(ValueUtils.iterate(VALUE), new QName("value"), context.getRootContext().getCurrentNodePointer().getLocale());
        assertTrue(iter.hasNext());
        assertSame(POINTER, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testValueIterator() {
        Iterator iter = new Expression.ValueIterator(ValueUtils.iterate(VALUE));
        assertTrue(iter.hasNext());
        assertSame(VALUE, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testComputeContextDependent_Static() {
        assertFalse(expression.computeContextDependent());
    }

    @Test
    public void testComputeContextDependent_Dynamic() {
        Expression dynamicExpression = new DynamicExpression();
        assertFalse(dynamicExpression.computeContextDependent());

        context = new EvalContext(new Object(), new QName("root2"), new NodePointer("root2", null, null, null), null);
        assertFalse(dynamicExpression.computeContextDependent());

        context = new EvalContext(new Object(), new QName("root3"), new NodePointer("root3", null, null, null), null);
        assertTrue(dynamicExpression.computeContextDependent());
    }

    private static Object VALUE = new Object();
    private static Pointer POINTER = new NodePointer("node", null, null, null);

    private static class StaticExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return false;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return VALUE;
        }

        @Override
        public Object compute(EvalContext context) {
            return VALUE;
        }
    }

    private static class DynamicExpression extends Expression {
        @Override
        public boolean computeContextDependent() {
            return context.getCurrentNodePointer().getLocalName().equals("root3");
        }

        @Override
        public Object computeValue(EvalContext context) {
            return context.getCurrentNodePointer().getValue();
        }

        @Override
        public Object compute(EvalContext context) {
            return context.getCurrentNodePointer().getValue();
        }
    }
}