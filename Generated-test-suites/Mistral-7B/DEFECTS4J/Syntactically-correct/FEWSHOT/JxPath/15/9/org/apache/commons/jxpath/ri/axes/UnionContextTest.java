package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext contextA = new TestEvalContext();
        EvalContext contextB = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { contextA, contextB });
        assertNotNull(unionContext);
        assertEquals(2, unionContext.contexts.length);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext contextA = new TestEvalContext();
        EvalContext contextB = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { contextA, contextB });
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_oneContext() {
        EvalContext context = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context });
        assertFalse(unionContext.prepared);

        unionContext.setPosition(0);
        assertTrue(unionContext.prepared);
    }

    @Test
    public void testSetPosition_multipleContexts() {
        EvalContext contextA = new TestEvalContext();
        EvalContext contextB = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { contextA, contextB });

        unionContext.setPosition(0);
        assertTrue(unionContext.prepared);
    }

    @Test
    public void testSetPosition_outOfBounds() {
        EvalContext contextA = new TestEvalContext();
        EvalContext contextB = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { contextA, contextB });

        unionContext.setPosition(Integer.MAX_VALUE);
        assertFalse(unionContext.prepared);
        assertFalse(unionContext.getNodeSet().contains(unionContext.getCurrentNodePointer()));
    }

    @Test
    public void testSetPosition_samePositionInBothContexts() {
        EvalContext contextA = new TestEvalContext();
        EvalContext contextB = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { contextA, contextB });

        contextA.addNode("A1");
        contextB.addNode("B1");
        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals("A1", unionContext.getCurrentNode().getValue());
        assertEquals("B1", unionContext.getContext().getCurrentNode().getValue());
    }

    @Test
    public void testSetPosition_differentPositionsInBothContexts() {
        EvalContext contextA = new TestEvalContext();
        EvalContext contextB = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { contextA, contextB });

        contextA.addNode("A1");
        contextA.nextSet();
        contextA.nextNode();
        contextB.addNode("B1");

        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals("A2", unionContext.getCurrentNode().getValue());
        assertNull(contextB.getCurrentNode());
    }

    private static class TestEvalContext extends EvalContext {
        private int currentSetIndex = 0;
        private int currentNodeIndex = 0;
        private BasicNodeSet nodeSet = new BasicNodeSet();

        @Override
        public boolean nextSet() {
            if (currentSetIndex < 3) {
                nodeSet.clear();
                for (int i = 0; i < 3; i++) {
                    nodeSet.add(new NodePointer("test-" + currentSetIndex + "-" + currentNodeIndex++));
                }
                currentSetIndex++;
                return true;
            }
            return false;
        }

        @Override
        public boolean nextNode() {
            return nextSet() && currentNodeIndex < 3;
        }

        @Override
        public NodePointer getCurrentNodePointer() {
            return nodeSet.getPointer(currentNodeIndex);
        }

        @Override
        public String getCurrentNodeValue() {
            NodePointer nodePointer = getCurrentNodePointer();
            return nodePointer == null ? null : nodePointer.getValue();
        }
    }
}