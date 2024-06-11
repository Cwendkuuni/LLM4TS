package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.EvaluationContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSet;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.junit.Test;

/**
 * Test cases for UnionContext
 */
public class UnionContextTest {

    @Test
    public void testUnionContext() {
        EvalContext ctx1 = new EvaluationContext(new BasicNodeSet(), null);
        EvalContext ctx2 = new EvaluationContext(new BasicNodeSet(), null);
        NodeSet nodeSet1 = ctx1.getNodeSet();
        NodeSet nodeSet2 = ctx2.getNodeSet();

        NodePointer nodePointer1 = nodeSet1.add(new NodePointer(1));
        NodePointer nodePointer2 = nodeSet1.add(new NodePointer(2));
        NodePointer nodePointer3 = nodeSet2.add(new NodePointer(2));
        NodePointer nodePointer4 = nodeSet2.add(new NodePointer(3));

        EvalContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        assertEquals(4, unionContext.size());
        assertTrue(unionContext.containsNodePointer(nodePointer1));
        assertTrue(unionContext.containsNodePointer(nodePointer2));
        assertTrue(unionContext.containsNodePointer(nodePointer3));
        assertTrue(unionContext.containsNodePointer(nodePointer4));

        unionContext.nextSet();
        assertFalse(unionContext.hasNextNode());

        unionContext.nextSet();
        assertEquals(4, unionContext.size());
        assertTrue(unionContext.containsNodePointer(nodePointer1));
        assertTrue(unionContext.containsNodePointer(nodePointer2));
        assertTrue(unionContext.containsNodePointer(nodePointer3));
        assertTrue(unionContext.containsNodePointer(nodePointer4));

        unionContext.nextNode();
        assertFalse(unionContext.hasNextNode());

        unionContext.nextNode();
        assertFalse(unionContext.hasNextNode());

        unionContext.nextNode();
        assertFalse(unionContext.hasNextNode());

        unionContext.nextNode();
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext ctx1 = new EvaluationContext(new BasicNodeSet(), null);
        EvalContext ctx2 = new EvaluationContext(new BasicNodeSet(), null);

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext ctx1 = new EvaluationContext(new BasicNodeSet(), null);
        EvalContext ctx2 = new EvaluationContext(new BasicNodeSet(), null);

        NodeSet nodeSet1 = ctx1.getNodeSet();
        NodeSet nodeSet2 = ctx2.getNodeSet();

        NodePointer nodePointer1 = nodeSet1.add(new NodePointer(1));
        NodePointer nodePointer2 = nodeSet1.add(new NodePointer(2));
        NodePointer nodePointer3 = nodeSet2.add(new NodePointer(2));
        NodePointer nodePointer4 = nodeSet2.add(new NodePointer(3));

        EvalContext unionContext = new UnionContext(null, new EvalContext[]{ctx1, ctx2});

        unionContext.nextSet();
        unionContext.nextNode();

        unionContext.setPosition(1);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer1));

        unionContext.nextNode();
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer2));

        unionContext.setPosition(4);
        assertNull(unionContext.getCurrentNodePointer());
    }
}