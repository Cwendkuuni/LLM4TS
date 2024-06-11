package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    private static EvalContext createContext(NodePointer nodePointer) {
        EvalContext context = new EvalContext(new BasicNodeSet(), null);
        context.setCurrentNodePointer(nodePointer);
        return context;
    }

    @Test
    public void testUnionContextWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertFalse(unionContext.isReady());
        assertNull(unionContext.getCurrentNodePointer());
        assertEquals(0, unionContext.size());
    }

    @Test
    public void testUnionContextWithOneContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer = new NodePointer(1);
        EvalContext context = createContext(nodePointer);

        EvalContext[] contexts = {context};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertTrue(unionContext.isReady());
        assertSame(nodePointer, unionContext.getCurrentNodePointer());
        assertEquals(1, unionContext.size());
    }

    @Test
    public void testUnionContextWithTwoContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);

        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertTrue(unionContext.isReady());
        assertNull(unionContext.getCurrentNodePointer());
        assertEquals(2, unionContext.size());
    }

    @Test
    public void testUnionContextWithTwoContextsAndPositionZero() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);

        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextWithTwoContextsAndPositionOne() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);

        EvalContext[] contexts = {context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextSet();

        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextWithThreeContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);
        NodePointer nodePointer3 = new NodePointer(3);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);
        EvalContext context3 = createContext(nodePointer3);

        EvalContext[] contexts = {context1, context2, context3};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertTrue(unionContext.isReady());
        assertNull(unionContext.getCurrentNodePointer());
        assertEquals(3, unionContext.size());
    }

    @Test
    public void testUnionContextWithThreeContextsAndPositionZero() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);
        NodePointer nodePointer3 = new NodePointer(3);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);
        EvalContext context3 = createContext(nodePointer3);

        EvalContext[] contexts = {context1, context2, context3};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextWithThreeContextsAndPositionOne() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);
        NodePointer nodePointer3 = new NodePointer(3);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);
        EvalContext context3 = createContext(nodePointer3);

        EvalContext[] contexts = {context1, context2, context3};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextSet();

        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextWithThreeContextsAndPositionTwo() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet(), null);
        NodePointer nodePointer1 = new NodePointer(1);
        NodePointer nodePointer2 = new NodePointer(2);
        NodePointer nodePointer3 = new NodePointer(3);

        EvalContext context1 = createContext(nodePointer1);
        EvalContext context2 = createContext(nodePointer2);
        EvalContext context3 = createContext(nodePointer3);

        EvalContext[] contexts = {context1, context2, context3};
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals(nodePointer3, unionContext.getCurrentNodePointer());
    }
}