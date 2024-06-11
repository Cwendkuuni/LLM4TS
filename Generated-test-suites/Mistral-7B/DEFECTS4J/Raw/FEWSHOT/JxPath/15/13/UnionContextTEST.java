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
    public void testUnionContextWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertEquals(0, unionContext.size());
        assertFalse(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testUnionContextWithSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        context.addNode(nodePointer);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        assertEquals(1, unionContext.size());
        assertTrue(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());

        unionContext.nextSet();

        assertEquals(1, unionContext.size());
        assertTrue(unionContext.hasNextNode());

        unionContext.nextNode();

        assertSame(nodePointer, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testUnionContextWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        NodePointer nodePointer1 = new NodePointer(new Object(), 0);
        NodePointer nodePointer2 = new NodePointer(new Object(), 1);
        context1.addNode(nodePointer1);
        context2.addNode(nodePointer2);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(2, unionContext.size());
        assertTrue(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());

        unionContext.nextSet();

        assertEquals(2, unionContext.size());
        assertTrue(unionContext.hasNextNode());

        unionContext.nextNode();

        assertSame(nodePointer1, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.hasNextNode());

        unionContext.nextSet();

        assertEquals(2, unionContext.size());
        assertTrue(unionContext.hasNextNode());

        unionContext.nextNode();

        assertSame(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPositionWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        context.addNode(nodePointer);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        assertFalse(unionContext.setPosition(0));

        unionContext.nextSet();

        assertTrue(unionContext.setPosition(0));

        assertSame(nodePointer, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        NodePointer nodePointer1 = new NodePointer(new Object(), 0);
        NodePointer nodePointer2 = new NodePointer(new Object(), 1);
        context1.addNode(nodePointer1);
        context2.addNode(nodePointer2);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertFalse(unionContext.setPosition(0));

        unionContext.nextSet();

        assertTrue(unionContext.setPosition(0));

        assertSame(nodePointer1, unionContext.getCurrentNodePointer());

        unionContext.nextSet();

        assertTrue(unionContext.setPosition(0));

        assertSame(nodePointer2, unionContext.getCurrentNodePointer());
    }
}