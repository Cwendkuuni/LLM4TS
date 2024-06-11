package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    private Expression expr;
    private EvalContext context;

    @Before
    public void setUp() {
        expr = new DummyExpression();
        context = new EvalContext(null, null);
    }

    @Test
    public void testIsContextDependent_default() {
        assertTrue(expr.isContextDependent());
    }

    @Test
    public void testIsContextDependent_overridden() {
        DummyContextDependentExpression expr = new DummyContextDependentExpression();
        assertFalse(expr.isContextDependent());
    }

    @Test
    public void testComputeValue_default() {
        Object result = expr.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValue_overridden() {
        DummyContextDependentExpression expr = new DummyContextDependentExpression();
        Object result = expr.computeValue(context);
        assertEquals("dummy", result);
    }

    @Test
    public void testCompute_default() {
        Object result = expr.compute(context);
        assertNull(result);
    }

    @Test
    public void testCompute_overridden() {
        DummyContextDependentExpression expr = new DummyContextDependentExpression();
        Object result = expr.compute(context);
        assertEquals("dummy", result);
    }

    @Test
    public void testIterate_default() {
        Iterator<Object> iterator = expr.iterate(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterate_overridden() {
        DummyContextDependentExpression expr = new DummyContextDependentExpression();
        Iterator<Object> iterator = expr.iterate(context);
        assertTrue(iterator.hasNext());
        assertEquals("dummy", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_default() {
        Iterator<Object> iterator = expr.iteratePointers(context);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratePointers_overridden() {
        DummyContextDependentExpression expr = new DummyContextDependentExpression();
        Iterator<Object> iterator = expr.iteratePointers(context);
        assertTrue(iterator.hasNext());
        Pointer pointer = (Pointer) iterator.next();
        assertEquals("dummy", pointer.getValue());
        assertFalse(iterator.hasNext());
    }
}

class DummyExpression extends Expression {

    @Override
    public boolean computeContextDependent() {
        return true;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return null;
    }

    @Override
    public Object compute(EvalContext context) {
        return null;
    }

    @Override
    public Iterator<Object> iterate(EvalContext context) {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<Object> iteratePointers(EvalContext context) {
        return Collections.emptyList().iterator();
    }
}

class DummyContextDependentExpression extends DummyExpression {

    @Override
    public boolean computeContextDependent() {
        return false;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return "dummy";
    }
}