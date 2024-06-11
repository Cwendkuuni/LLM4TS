package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        assertEquals("UnionContext", unionContext.getClass().getName());
        assertEquals(parentContext, unionContext.getParentContext());
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_emptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);

        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_singleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context });

        assertTrue(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_multipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        EvalContext[] contexts = new EvalContext[] { context1, context2 };
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();
        NodePointer node1 = new NodePointer("node1");
        NodePointer node2 = new NodePointer("node2");

        context1.addNodePointer(node1);
        context2.addNodePointer(node2);

        assertTrue(unionContext.setPosition(0));
        assertEquals(node1, nodeSet.getPointer(0));
    }

    @Test
    public void testSetPosition_multipleContextsWithMultipleNodes() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();

        NodePointer node1_1 = new NodePointer("node1_1");
        NodePointer node1_2 = new NodePointer("node1_2");
        NodePointer node2_1 = new NodePointer("node2_1");
        NodePointer node2_2 = new NodePointer("node2_2");

        context1.addNodePointer(node1_1);
        context1.addNodePointer(node1_2);
        context2.addNodePointer(node2_1);
        context2.addNodePointer(node2_2);

        assertTrue(unionContext.setPosition(0));
        assertEquals(node1_1, nodeSet.getPointer(0));

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertEquals(node1_2, nodeSet.getPointer(1));

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertEquals(node2_1, nodeSet.getPointer(2));

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertEquals(node2_2, nodeSet.getPointer(3));

        assertFalse(unionContext.nextSet());
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testSetPosition_multipleContextsWithDuplicateNodes() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        NodePointer node1 = new NodePointer("node1");

        context1.addNodePointer(node1);
        context2.addNodePointer(node1);

        assertTrue(unionContext.setPosition(0));
        assertEquals(node1, unionContext.getNodeSet().getPointer(0));

        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertNull(unionContext.getNodeSet().getPointer(1));
    }

    @Test
    public void testSetPosition_multipleContextsWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });

        context1.setEmpty();
        context2.setEmpty();

        assertFalse(unionContext.setPosition(0));
    }
}