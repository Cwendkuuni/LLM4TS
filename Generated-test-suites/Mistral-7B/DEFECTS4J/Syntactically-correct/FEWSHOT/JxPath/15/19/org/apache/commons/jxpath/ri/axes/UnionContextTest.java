package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext context1 = new DummyEvalContext();
        EvalContext context2 = new DummyEvalContext();

        BasicNodeSet nodeSet1 = new BasicNodeSet();
        BasicNodeSet nodeSet2 = new BasicNodeSet();

        nodeSet1.add(new NodePointer(1, "node1"));
        nodeSet1.add(new NodePointer(2, "node2"));

        nodeSet2.add(new NodePointer(3, "node3"));
        nodeSet2.add(new NodePointer(4, "node4"));

        EvalContext[] contexts = new EvalContext[]{context1, context2};
        UnionContext unionContext = new UnionContext(null, contexts);

        assertEquals(4, unionContext.getSize());
        assertNotSame(nodeSet1, unionContext.getNodeSet());
        assertNotSame(nodeSet2, unionContext.getNodeSet());

        int i = 0;
        while (unionContext.nextSet()) {
            while (unionContext.nextNode()) {
                NodePointer nodePointer = unionContext.getCurrentNodePointer();
                assertEquals(nodeSet1.get(i++), nodePointer);
                i++;
                assertEquals(nodeSet2.get(i++), nodePointer);
                i++;
            }
        }
    }

    @Test
    public void testUnionContextGetDocumentOrder() {
        EvalContext context1 = new DummyEvalContext();
        EvalContext context2 = new DummyEvalContext();

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testUnionContextSetPosition() {
        EvalContext context1 = new DummyEvalContext();
        EvalContext context2 = new DummyEvalContext();

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});

        for (int i = 0; i < unionContext.getSize(); i++) {
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
            unionContext.setPosition(i);
            assertTrue(unionContext.nextSet());
            assertTrue(unionContext.nextNode());
        }

        assertFalse(unionContext.nextSet());
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testUnionContextPrepared() {
        EvalContext context1 = new DummyEvalContext();
        EvalContext context2 = new DummyEvalContext();

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context1, context2});

        assertFalse(unionContext.isPrepared());

        unionContext.nextSet();
        unionContext.nextNode();

        assertTrue(unionContext.isPrepared());
    }

    private static class DummyEvalContext extends EvalContext {
        @Override
        public boolean nextSet() {
            return true;
        }

        @Override
        public boolean nextNode() {
            return true;
        }
    }
}