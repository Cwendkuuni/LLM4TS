package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA, contextB});
        assertEquals(2, unionContext.contexts.length);
        assertFalse(unionContext.prepared);
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA, contextB});
        assertTrue(unionContext.getDocumentOrder() > 0);

        contextA = new StubEvalContext();
        UnionContext unionContext2 = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA});
        assertEquals(unionContext2.getDocumentOrder(), unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA, contextB});

        BasicNodeSet nodeSet = new BasicNodeSet();
        ArrayList pointers = new ArrayList();

        contextA.setNodeSet(nodeSet);
        contextB.setNodeSet(nodeSet);

        NodePointer pointerA1 = new NodePointer(1, 1);
        NodePointer pointerA2 = new NodePointer(2, 1);
        NodePointer pointerB1 = new NodePointer(3, 1);
        NodePointer pointerB2 = new NodePointer(4, 1);

        pointers.add(pointerA1);
        pointers.add(pointerA2);
        pointers.add(pointerB1);
        pointers.add(pointerB2);

        contextA.setPosition(0);
        contextB.setPosition(2);

        unionContext.setPosition(3);
        assertTrue(unionContext.getNodeSet().contains(pointerB1));
        assertTrue(unionContext.getNodeSet().contains(pointerA2));
        assertFalse(unionContext.getNodeSet().contains(pointerA1));
        assertFalse(unionContext.getNodeSet().contains(pointerB2));
    }

    @Test
    public void testSetPositionPrepared() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA, contextB});

        BasicNodeSet nodeSet = new BasicNodeSet();
        ArrayList pointers = new ArrayList();

        contextA.setNodeSet(nodeSet);
        contextB.setNodeSet(nodeSet);

        NodePointer pointerA1 = new NodePointer(1, 1);
        NodePointer pointerA2 = new NodePointer(2, 1);
        NodePointer pointerB1 = new NodePointer(3, 1);
        NodePointer pointerB2 = new NodePointer(4, 1);

        pointers.add(pointerA1);
        pointers.add(pointerA2);
        pointers.add(pointerB1);
        pointers.add(pointerB2);

        contextA.nextSet();
        contextB.nextSet();

        unionContext.setPosition(3);
        assertTrue(unionContext.getNodeSet().containsAll(pointers));
    }

    @Test
    public void testSetPositionNullContext() {
        EvalContext contextA = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA});
        assertNull(unionContext.getNodeSet());
        unionContext.setPosition(1);
        assertTrue(unionContext.isPositionValid());
    }

    @Test
    public void testSetPositionInvalidPosition() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new StubEvalContext(), new EvalContext[]{contextA, contextB});
        unionContext.setPosition(10);
        assertFalse(unionContext.isPositionValid());
    }

    static class StubEvalContext extends EvalContext {
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
            return null;
        }
    }

}