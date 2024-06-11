package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertFalse(unionContext.prepared);
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testUnionContextSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        assertEquals(1, unionContext.contexts.length);
        assertEquals(context, unionContext.contexts[0]);
        assertSame(parentContext, unionContext.getParentContext());
        assertFalse(unionContext.prepared);
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testUnionContextMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(2, unionContext.contexts.length);
        assertSame(context1, unionContext.contexts[0]);
        assertSame(context2, unionContext.contexts[1]);
        assertSame(parentContext, unionContext.getParentContext());
        assertFalse(unionContext.prepared);
        assertNull(unionContext.getNodeSet());

        context1.nextSet();
        context1.nextNode();
        NodePointer nodePointer1 = context1.getCurrentNodePointer();
        context1.nextNode();
        NodePointer nodePointer2 = context1.getCurrentNodePointer();

        context2.nextSet();
        context2.nextNode();
        NodePointer nodePointer3 = context2.getCurrentNodePointer();

        unionContext.setPosition(0);
        assertTrue(unionContext.getNodeSet().getPointer(0).equals(nodePointer1));

        unionContext.nextNode();
        assertTrue(unionContext.getNodeSet().getPointer(0).equals(nodePointer2));

        unionContext.nextNode();
        assertTrue(unionContext.getNodeSet().getPointer(0).equals(nodePointer3));

        unionContext.prepared = true;
        assertEquals(3, unionContext.getNodeSet().size());
    }

    @Test
    public void testUnionContextWithDifferentDocumentOrders() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        context1.setDocumentOrder(1);
        context2.setDocumentOrder(0);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(2, unionContext.contexts.length);
        assertEquals(context1, unionContext.contexts[0]);
        assertEquals(context2, unionContext.contexts[1]);
        assertSame(parentContext, unionContext.getParentContext());
        assertFalse(unionContext.prepared);
        assertNull(unionContext.getNodeSet());

        context1.nextSet();
        context1.nextNode();
        NodePointer nodePointer1 = context1.getCurrentNodePointer();

        context2.nextSet();
        context2.nextNode();
        NodePointer nodePointer2 = context2.getCurrentNodePointer();

        unionContext.setPosition(0);
        assertTrue(unionContext.getNodeSet().getPointer(0).equals(nodePointer2));

        unionContext.nextNode();
        assertTrue(unionContext.getNodeSet().getPointer(0).equals(nodePointer1));
    }
}