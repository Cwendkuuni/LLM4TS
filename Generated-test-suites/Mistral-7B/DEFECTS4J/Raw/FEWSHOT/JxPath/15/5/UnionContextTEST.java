package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri. axes.UnionContext;

import java.util.ArrayList;
import java.util.List;

public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();

        NodeSetContext parentContext = new NodeSetContext(null, new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        List<NodePointer> nodeSetA = new ArrayList<>();
        List<NodePointer> nodeSetB = new ArrayList<>();

        // Populate nodes for context A and B
        for (int i = 0; i < 5; i++) {
            nodeSetA.add(new NodePointer(i, "A_" + i));
            nodeSetB.add(new NodePointer(i + 5, "B_" + i));
        }

        // Set up evaluation contexts
        when(contextA.nextSet()).thenReturn(true).thenReturn(nodeSetA.iterator());
        when(contextB.nextSet()).thenReturn(true).thenReturn(nodeSetB.iterator());

        // Test union context
        unionContext.setPosition(0);
        NodePointer nodeA = unionContext.getCurrentNodePointer();
        assertEquals(nodeSetA.get(0), nodeA);

        unionContext.nextNode();
        NodePointer nodeB = unionContext.getCurrentNodePointer();
        assertEquals(nodeSetB.get(0), nodeB);

        unionContext.nextNode();
        NodePointer nodeA1 = unionContext.getCurrentNodePointer();
        assertEquals(nodeSetA.get(1), nodeA1);

        unionContext.nextNode();
        NodePointer nodeB1 = unionContext.getCurrentNodePointer();
        assertEquals(nodeSetB.get(1), nodeB1);

        // Test getDocumentOrder
        assertEquals(1, unionContext.getDocumentOrder());

        // Test setPosition when no more nodes in contexts
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextWithEmptyContexts() {
        EvalContext contextA = new MockEvalContext();
        EvalContext contextB = new MockEvalContext();

        NodeSetContext parentContext = new NodeSetContext(null, new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{contextA, contextB});

        when(contextA.nextSet()).thenReturn(false);
        when(contextB.nextSet()).thenReturn(false);

        unionContext.setPosition(0);
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextWithNullContexts() {
        NodeSetContext parentContext = new NodeSetContext(null, new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, null);

        assertNull(unionContext.getContexts);
        assertNull(unionContext.getDocumentOrder());
    }
}

class MockEvalContext implements EvalContext {
    private Iterator<NodeSet> nodeSetsIterator;
    private Iterator<NodePointer> currentNodeIterator;

    @Override
    public boolean nextSet() {
        return nodeSetsIterator != null && nodeSetsIterator.hasNext();
    }

    @Override
    public Iterator<NodeSet> getNodeSetsIterator() {
        return nodeSetsIterator;
    }

    @Override
    public boolean nextNode() {
        return currentNodeIterator != null && currentNodeIterator.hasNext();
    }

    @Override
    public NodePointer getCurrentNodePointer() {
        return currentNodeIterator.next();
    }

    public void setNodeSetsIterator(Iterator<NodeSet> nodeSetsIterator) {
        this.nodeSetsIterator = nodeSetsIterator;
    }

    public void setCurrentNodeIterator(Iterator<NodePointer> currentNodeIterator) {
        this.currentNodeIterator = currentNodeIterator;
    }
}