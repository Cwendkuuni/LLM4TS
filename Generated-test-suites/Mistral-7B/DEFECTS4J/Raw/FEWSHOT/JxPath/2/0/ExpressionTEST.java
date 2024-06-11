package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertNull;
import org.junit.Assert.assertSame;
import org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {

    private Expression emptyExpression;
    private Expression singleNodeExpression;
    private Expression contextDependentExpression;
    private Expression contextIndependentExpression;

    @Before
    public void setUp() {
        emptyExpression = new ConstantExpression(new Object[] {});
        singleNodeExpression = new ConstantExpression(new Object[] { new NodePointer(new QName(null, "root"), null, null) });
        contextDependentExpression = new ContextDependentExpression();
        contextIndependentExpression = new ConstantExpression(new Object[] { ONE });
    }

    @Test
    public void testIsContextDependent() {
        assertEquals(false, emptyExpression.isContextDependent());
        assertEquals(false, singleNodeExpression.isContextDependent());
        assertFalse(contextDependentExpression.isContextDependent());
        assertTrue(contextIndependentExpression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Object resultEmpty = emptyExpression.computeValue(new EvalContext(new Object[]{}));
        assertNull(resultEmpty);

        Object resultSingleNode = singleNodeExpression.computeValue(new EvalContext(new Object[]{ new NodePointer(new QName(null, "root"), null, null) }));
        assertSame(new NodePointer(new QName(null, "root"), null, null), resultSingleNode);

        assertNull(contextDependentExpression.computeValue(new EvalContext(new Object[]{ ONE, ONE })));
        assertNull(contextDependentExpression.computeValue(new EvalContext(new Object[]{ ZERO, ZERO })));

        assertEquals(ONE, contextIndependentExpression.computeValue(new EvalContext(new Object[]{ ONE, ONE })));
        assertEquals(ONE, contextIndependentExpression.computeValue(new EvalContext(new Object[]{ ZERO, ZERO })));
    }

    @Test
    public void testIterate() {
        Iterator itEmpty = emptyExpression.iterate(new EvalContext(new Object[]{}));
        assertFalse(itEmpty.hasNext());

        Iterator itSingleNode = singleNodeExpression.iterate(new EvalContext(new Object[]{ new NodePointer(new QName(null, "root"), null, null) }));
        assertTrue(itSingleNode.hasNext());
        assertSame(new NodePointer(new QName(null, "root"), null, null), itSingleNode.next());
        assertFalse(itSingleNode.hasNext());

        Iterator itContextDependent = contextDependentExpression.iterate(new EvalContext(new Object[]{ ONE, ONE }));
        assertFalse(itContextDependent.hasNext());

        Iterator itContextIndependent = contextIndependentExpression.iterate(new EvalContext(new Object[]{ ONE, ONE }));
        assertFalse(itContextIndependent.hasNext());

        Iterator itContextIndependentPointers = contextIndependentExpression.iteratePointers(new EvalContext(new Object[]{ ONE, ONE }));
        assertTrue(itContextIndependentPointers.hasNext());
        assertSame(new NodePointer(new QName(null, "value"), ONE, Locale.US), itContextIndependentPointers.next());
        assertFalse(itContextIndependentPointers.hasNext());
    }
}

class ContextDependentExpression extends Expression {
    @Override
    public boolean computeContextDependent() {
        return true;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return context.getContext().size();
    }

    @Override
    public Object compute(EvalContext context) {
        return context.getContext().size();
    }
}

class ConstantExpression extends Expression {
    private Object[] values;

    public ConstantExpression(Object[] values) {
        this.values = values;
    }

    @Override
    public boolean computeContextDependent() {
        return false;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return values[0];
    }

    @Override
    public Object compute(EvalContext context) {
        return values[0];
    }
}