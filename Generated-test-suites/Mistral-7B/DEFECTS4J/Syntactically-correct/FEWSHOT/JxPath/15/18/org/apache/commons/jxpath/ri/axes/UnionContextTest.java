package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.BasicNodeSet;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();
        EvalContext parentContext = new MockEvalContext();

        BasicNodeSet nodeSetA = new BasicNodeSet();
        BasicNodeSet nodeSetB = new BasicNodeSet();

        contextA.setNodeSet(nodeSetA);
        contextB.setNodeSet(nodeSetB);

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        assertEquals(2, unionContext.contexts.length);
        assertEquals(BasicNodeSet.class, unionContext.getNodeSet().getClass());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();
        EvalContext parentContext = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_positionZero() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();
        EvalContext parentContext = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
        NodePointer nodePointerA = new NodePointer(1, 1);
        NodePointer nodePointerB = new NodePointer(2, 1);

        contextA.setCurrentNodePointer(nodePointerA);
        contextB.setCurrentNodePointer(nodePointerB);

        unionContext.setPosition(0);

        assertTrue(unionContext.getNodeSet().contains(nodePointerA));
        assertTrue(unionContext.getNodeSet().contains(nodePointerB));
        assertEquals(nodePointerA, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_positionOne() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();
        EvalContext parentContext = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
        NodePointer nodePointerA = new NodePointer(1, 1);
        NodePointer nodePointerB = new NodePointer(2, 1);

        contextA.setCurrentNodePointer(nodePointerA);
        contextB.setCurrentNodePointer(nodePointerB);

        unionContext.setPosition(1);

        assertTrue(unionContext.getNodeSet().contains(nodePointerB));
        assertFalse(unionContext.getNodeSet().contains(nodePointerA));
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_positionGreaterThanTotalNodes() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();
        EvalContext parentContext = new MockEvalContext();

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});
        NodePointer nodePointerA = new NodePointer(1, 1);
        NodePointer nodePointerB = new NodePointer(2, 1);

        contextA.setCurrentNodePointer(nodePointerA);
        contextB.setCurrentNodePointer(nodePointerB);

        unionContext.setPosition(3);

        assertFalse(unionContext.getNodeSet().contains(nodePointerA));
        assertFalse(unionContext.getNodeSet().contains(nodePointerB));
        assertNull(unionContext.getCurrentNodePointer());
    }

    private static class MockEvalContext extends EvalContext {
        @Override
        public boolean nextSet() {
            return true;
        }

        @Override
        public boolean nextNode() {
            return true;
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return new NodePointer(1, 1);
        }
    }
}