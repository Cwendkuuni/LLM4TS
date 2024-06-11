package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.NodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {
    @Test
    public void testUnionContextCreation() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        assertEquals(2, unionContext.contexts.length);
        assertFalse(unionContext.prepared);
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        NodeSet nodeSet = unionContext.getNodeSet();
        NodePointer nodePointerA = new NodePointer(0, 0, 0);
        NodePointer nodePointerB = new NodePointer(0, 0, 1);

        // Set position within the first context
        contextA.setNextSet(true);
        contextA.setCurrentNodePointer(nodePointerA);
        unionContext.setPosition(0);
        assertEquals(nodePointerA, nodeSet.getPointer(0));

        // Set position within the second context
        contextB.setNextSet(true);
        contextB.setCurrentNodePointer(nodePointerB);
        unionContext.setPosition(1);
        assertEquals(nodePointerB, nodeSet.getPointer(1));
    }

    @Test
    public void testSetPosition_whenBothContextsEmpty_returnFalse() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        contextA.setNextSet(false);
        contextB.setNextSet(false);

        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_whenBothContextsEmpty_returnFalse_secondTime() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        contextA.setNextSet(false);
        contextB.setNextSet(false);

        assertFalse(unionContext.setPosition(0));
        assertFalse(unionContext.setPosition(1));
    }

    @Test
    public void testSetPosition_whenBothContextsHaveData_returnTrue() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        NodeSet nodeSetA = new BasicNodeSet();
        NodeSet nodeSetB = new BasicNodeSet();
        contextA.setNextSet(true);
        contextA.setCurrentNodePointer(new NodePointer(0, 0, 0));
        nodeSetA.add(contextA.getCurrentNodePointer());

        contextB.setNextSet(true);
        contextB.setCurrentNodePointer(new NodePointer(0, 0, 0));
        nodeSetB.add(contextB.getCurrentNodePointer());

        unionContext.setPosition(0);
        assertEquals(contextA.getCurrentNodePointer(), unionContext.getNodeSet().getPointer(0));
        unionContext.setPosition(1);
        assertEquals(contextB.getCurrentNodePointer(), unionContext.getNodeSet().getPointer(1));
        assertTrue(unionContext.setPosition(2));
        assertNull(unionContext.getNodeSet().getPointer(2));
    }

    @Test
    public void testSetPosition_whenFirstContextHasDataAndSecondEmpty_returnTrue() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        NodeSet nodeSetA = new BasicNodeSet();
        NodeSet nodeSetB = new BasicNodeSet();
        contextA.setNextSet(true);
        contextA.setCurrentNodePointer(new NodePointer(0, 0, 0));
        nodeSetA.add(contextA.getCurrentNodePointer());

        contextB.setNextSet(false);

        unionContext.setPosition(0);
        assertEquals(contextA.getCurrentNodePointer(), unionContext.getNodeSet().getPointer(0));
        unionContext.setPosition(1);
        assertNull(unionContext.getNodeSet().getPointer(1));
        assertTrue(unionContext.setPosition(2));
        assertNull(unionContext.getNodeSet().getPointer(2));
    }

    @Test
    public void testSetPosition_whenFirstEmptyAndSecondHasData_returnTrue() {
        EvalContext contextA = new StubEvalContext();
        EvalContext contextB = new StubEvalContext();
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[] { contextA, contextB });

        contextA.setNextSet(false);

        contextB.setNextSet(true);
        contextB.setCurrentNodePointer(new NodePointer(0, 0, 0));
        NodeSet nodeSetB = new BasicNodeSet();
        nodeSetB.add(contextB.getCurrentNodePointer());

        unionContext.setPosition(0);
        assertNull(unionContext.getNodeSet().getPointer(0));
        unionContext.setPosition(1);
        assertEquals(contextB.getCurrentNodePointer(), unionContext.getNodeSet().getPointer(0));
        assertTrue(unionContext.setPosition(2));
        assertNull(unionContext.getNodeSet().getPointer(1));
    }

    private static class StubEvalContext extends EvalContext {
        @Override
        public boolean nextNode() {
            return true;
        }

        @Override
        public void reset() {
        }

        @Override
        public void setNextSet(boolean nextSet) {
        }
    }
}