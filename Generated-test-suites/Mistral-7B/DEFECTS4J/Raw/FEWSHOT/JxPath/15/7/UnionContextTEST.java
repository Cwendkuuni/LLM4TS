package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.BasicNodeSet;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();
        EvalContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });

        assertEquals(3, unionContext.getDocumentOrder());

        NodePointer nodeA1 = new NodePointer(1);
        NodePointer nodeA2 = new NodePointer(2);
        NodePointer nodeB1 = new NodePointer(3);
        NodePointer nodeB2 = new NodePointer(4);

        contextA.setNodeSet(new BasicNodeSet(new NodePointer[] { nodeA1, nodeA2 }));
        contextB.setNodeSet(new BasicNodeSet(new NodePointer[] { nodeB1, nodeB2 }));

        unionContext.nextSet();
        unionContext.nextNode();
        assertEquals(nodeA1, unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertEquals(nodeA2, unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertEquals(nodeB1, unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertEquals(nodeB2, unionContext.getCurrentNodePointer());

        assertFalse(unionContext.nextNodeExists());
    }

    @Test
    public void testUnionContext_emptyContexts() {
        EvalContext contextA = new MockEvalContext();
        EvalContext unionContext = new UnionContext(null, new EvalContext[0]);

        assertEquals(0, unionContext.getDocumentOrder());

        assertFalse(unionContext.nextSetExists());
    }

    @Test
    public void testUnionContext_singleContext() {
        EvalContext context = new MockEvalContext();
        EvalContext unionContext = new UnionContext(null, new EvalContext[] { context });

        assertEquals(context.getDocumentOrder(), unionContext.getDocumentOrder());

        context.nextSet();
        context.nextNode();
        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals(context.getCurrentNodePointer(), unionContext.getCurrentNodePointer());

        assertFalse(unionContext.nextNodeExists());
    }
}

class MockEvalContext extends EvalContext {
    private int currentSetIndex = 0;
    private int currentNodeIndex = 0;

    @Override
    public boolean nextSet() {
        return ++currentSetIndex < 2;
    }

    @Override
    public boolean nextNode() {
        return ++currentNodeIndex < getSize();
    }

    @Override
    public int getSize() {
        return 2;
    }
}