package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSet;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext parentContext = new EvaluationContext(null, null);
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        EvalContext context2 = new EvaluationContext(parentContext, new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(2, unionContext.contexts.length);
        assertFalse(unionContext.isRootContext());
        assertTrue(unionContext.isNodeSetContext());
        assertSame(parentContext, unionContext.getParentContext());
        assertNull(unionContext.getNodeSet());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvaluationContext(null, null);
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        EvalContext context2 = new EvaluationContext(parentContext, new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        EvalContext parentContext = new EvaluationContext(null, null);
        EvalContext context1 = new EvaluationContext(parentContext, new BasicNodeSet());
        EvalContext context2 = new EvaluationContext(parentContext, new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        NodeSet nodeSet = new BasicNodeSet();
        NodePointer nodePointer1 = new NodePointer(null, 1, null, null);
        NodePointer nodePointer2 = new NodePointer(null, 2, null, null);
        NodePointer nodePointer3 = new NodePointer(null, 3, null, null);

        context1.setNodeSet(nodeSet);
        context1.addNodeToCurrentSet(nodePointer1);
        context1.addNodeToCurrentSet(nodePointer2);

        context2.setNodeSet(nodeSet);
        context2.addNodeToCurrentSet(nodePointer3);

        unionContext.setPosition(0);
        NodePointer actualNodePointer = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = nodePointer1;
        assertEquals(expectedNodePointer, actualNodePointer);

        unionContext.setPosition(1);
        actualNodePointer = unionContext.getCurrentNodePointer();
        expectedNodePointer = nodePointer2;
        assertEquals(expectedNodePointer, actualNodePointer);

        unionContext.setPosition(2);
        actualNodePointer = unionContext.getCurrentNodePointer();
        expectedNodePointer = nodePointer3;
        assertEquals(expectedNodePointer, actualNodePointer);

        unionContext.setPosition(3); // out of bounds
        assertNull(unionContext.getCurrentNodePointer());
    }
}