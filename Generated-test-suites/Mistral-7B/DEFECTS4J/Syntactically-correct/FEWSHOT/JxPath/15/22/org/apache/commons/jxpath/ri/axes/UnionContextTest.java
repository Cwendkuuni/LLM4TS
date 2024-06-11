package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodeSetContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(2, unionContext.contexts.length);
        assertEquals(parentContext, unionContext.getParentContext());
        assertSame(new BasicNodeSet(), unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPositionWithEmptyContexts() {
        EvalContext parentContext = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertFalse(unionContext.prepared);
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithOneContext() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        assertFalse(unionContext.prepared);
        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithMultipleContexts() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertFalse(unionContext.prepared);
        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithPositionOutOfRange() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertFalse(unionContext.prepared);
        assertFalse(unionContext.setPosition(-1));
    }

    @Test
    public void testSetPositionWithPreparedContext() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertFalse(unionContext.prepared);
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.prepared);
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithSameNodePointerInDifferentContexts() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        NodePointer nodePointer = new NodePointer(new Object(), 0);

        context1.setCurrentNodePointer(nodePointer);
        context2.setCurrentNodePointer(nodePointer);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertFalse(unionContext.prepared);
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.prepared);
        assertTrue(unionContext.getNodeSet().contains(nodePointer));
    }

    @Test
    public void testSetPositionWithDifferentNodePointersInDifferentContexts() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        NodePointer nodePointer1 = new NodePointer(new Object1(), 0);
        NodePointer nodePointer2 = new NodePointer(new Object2(), 0);

        context1.setCurrentNodePointer(nodePointer1);
        context2.setCurrentNodePointer(nodePointer2);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertFalse(unionContext.prepared);
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.prepared);
        assertFalse(unionContext.getNodeSet().contains(nodePointer2));
        assertTrue(unionContext.getNodeSet().contains(nodePointer1));
    }

    @Test
    public void testNextSetWithEmptyContexts() {
        EvalContext parentContext = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testNextSetWithOneContext() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        assertTrue(unionContext.nextSet());
    }

    @Test
    public void testNextSetWithMultipleContexts() {
        EvalContext parentContext = new MockEvalContext();
        EvalContext context1 = new MockEvalContext();
        EvalContext context2 = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertTrue(unionContext.nextSet());
    }

    private static class MockEvalContext extends EvalContext {
        private int nextSetCallCount = 0;
        private int nextNodeCallCount = 0;
        private NodePointer currentNodePointer;

        @Override
        public boolean nextSet() {
            nextSetCallCount++;
            return true;
        }

        @Override
        public boolean nextNode() {
            nextNodeCallCount++;
            return true;
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return currentNodePointer;
        }

        @Override
        public void setCurrentNodePointer(NodePointer nodePointer) {
            currentNodePointer = nodePointer;
        }

        public int getNextSetCallCount() {
            return nextSetCallCount;
        }

        public int getNextNodeCallCount() {
            return nextNodeCallCount;
        }

        public void reset() {
            nextSetCallCount = 0;
            nextNodeCallCount = 0;
        }
    }

    private static class Object1 {
    }

    private static class Object2 {
    }
}