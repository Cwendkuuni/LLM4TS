package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        EvalContext parentContext = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(3, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPositionWithEmptyContexts() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        EvalContext parentContext = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{});

        assertFalse(unionContext.setPosition(0));
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testSetPositionWithOneContext() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext parentContext = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1});

        assertSame(context1.getNodeSet(), unionContext.getNodeSet());
        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithTwoContexts() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        EvalContext parentContext = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();

        NodePointer nodePointer1 = nodeSet.add("Node1");
        context1.nextSet();
        context1.nextNode();
        context1.setCurrentNodePointer(nodePointer1);

        NodePointer nodePointer2 = nodeSet.add("Node2");
        context2.nextSet();
        context2.nextNode();
        context2.setCurrentNodePointer(nodePointer2);

        assertTrue(unionContext.setPosition(0));
        assertSame(nodePointer1, unionContext.getCurrentNodePointer());

        assertTrue(unionContext.nextNode());
        assertSame(nodePointer2, unionContext.getCurrentNodePointer());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testSetPositionWithTwoContexts_duplicateNodes() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        EvalContext parentContext = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        NodePointer nodePointer1 = unionContext.getNodeSet().add("Node1");
        context1.nextSet();
        context1.nextNode();
        context1.setCurrentNodePointer(nodePointer1);

        NodePointer nodePointer2 = unionContext.getNodeSet().add("Node2");
        context2.nextSet();
        context2.nextNode();
        context2.setCurrentNodePointer(nodePointer2);

        assertTrue(unionContext.setPosition(0));
        assertSame(nodePointer1, unionContext.getCurrentNodePointer());

        assertTrue(unionContext.nextNode());
        assertSame(nodePointer2, unionContext.getCurrentNodePointer());

        assertTrue(unionContext.nextNode()); // should return the same node again
        assertSame(nodePointer2, unionContext.getCurrentNodePointer());

        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testSetPositionWithTwoContexts_endOfContexts() {
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        EvalContext parentContext = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        context1.nextSet();
        context1.nextNode();

        context2.nextSet();
        context2.nextNode();

        assertTrue(unionContext.setPosition(3)); // beyond the end of contexts
        assertNull(unionContext.getCurrentNodePointer());
    }
}