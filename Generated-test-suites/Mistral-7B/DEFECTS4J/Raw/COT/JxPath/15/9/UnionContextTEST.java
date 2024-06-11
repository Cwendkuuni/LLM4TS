package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UnionContextTest - tests for UnionContext class
 */
public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        int documentOrder = unionContext.getDocumentOrder();
        assertTrue(documentOrder > 0);
    }

    @Test
    public void testSetPositionWithOneContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        NodePointer nodePointer = new NodePointer(new Object(), 0);
        context.setCurrentNode(nodePointer);
        context.nextSet();
        context.nextNode();

        int position = 0;
        boolean result = unionContext.setPosition(position);
        assertTrue(result);
        assertEquals(nodePointer, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        NodePointer nodePointer1 = new NodePointer(new Object(), 0);
        context1.setCurrentNode(nodePointer1);
        context1.nextSet();
        context1.nextNode();

        NodePointer nodePointer2 = new NodePointer(new Object(), 1);
        context2.setCurrentNode(nodePointer2);
        context2.nextSet();
        context2.nextNode();

        int position = 0;
        boolean result = unionContext.setPosition(position);
        assertTrue(result);
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithPositionOutOfRange() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        int position = context.size();
        boolean result = unionContext.setPosition(position);
        assertFalse(result);
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionBeforePreparation() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        int position = 0;
        unionContext.setPosition(position);
    }
}