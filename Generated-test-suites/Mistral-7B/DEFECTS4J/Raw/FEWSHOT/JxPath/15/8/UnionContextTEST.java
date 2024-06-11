package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class UnionContextTest {

    @Test
    public void testUnionContextCreation() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });
        assertEquals(2, unionContext.contexts.length);
        assertEquals(UnionContext.class, unionContext.getClass());
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });
        assertTrue(unionContext.getDocumentOrder() > 0);

        UnionContext unionContextWithoutParent = new UnionContext(null, new EvalContext[] { context1, context2 });
        assertEquals(0, unionContextWithoutParent.getDocumentOrder());
    }

    @Test
    public void testSetPosition_EmptyContext() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });

        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_NoMoreNodesToReturn() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });

        context1.setNextSet(true);
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context1.setNextNode(null);

        context2.setNextSet(true);
        context2.setCurrentNodePointer(new NodePointer(2, 1));
        context2.setNextNode(null);

        unionContext.setPosition(0);
        assertFalse(unionContext.setPosition(1));
    }

    @Test
    public void testSetPosition_SingleContext() {
        EvalContext context1 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1 });

        context1.setNextSet(true);
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context1.setNextNode(null);

        NodePointer nodePointer = new NodePointer(1, 1);
        unionContext.setPosition(0);
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer));
    }

    @Test
    public void testSetPosition_MultipleContexts() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });

        context1.setNextSet(true);
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context1.setNextNode(null);

        context2.setNextSet(true);
        context2.setCurrentNodePointer(new NodePointer(2, 1));
        context2.setNextNode(null);

        unionContext.setPosition(0);
        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        assertEquals(context1, unionContext.getCurrentContext());
        assertTrue(nodePointer1.equals(context1.getCurrentNodePointer()));

        unionContext.next();
        NodePointer nodePointer2 = unionContext.getCurrentNodePointer();
        assertEquals(context2, unionContext.getCurrentContext());
        assertTrue(nodePointer2.equals(context2.getCurrentNodePointer()));
    }

    @Test
    public void testSetPosition_SameNodePointer() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });

        context1.setNextSet(true);
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context1.setNextNode(null);

        context2.setNextSet(true);
        context2.setCurrentNodePointer(new NodePointer(1, 1));
        context2.setNextNode(null);

        unionContext.setPosition(0);
        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        assertEquals(context1, unionContext.getCurrentContext());
        assertTrue(nodePointer1.equals(context1.getCurrentNodePointer()));

        unionContext.next();
        NodePointer nodePointer2 = unionContext.getCurrentNodePointer();
        assertEquals(context2, unionContext.getCurrentContext());
        assertTrue(nodePointer2.equals(context2.getCurrentNodePointer()));

        unionContext.next(); // should return the next node from the next context
        NodePointer nodePointer3 = unionContext.getCurrentNodePointer();
        assertFalse(nodePointer1.equals(nodePointer3));
        assertSame(context2, unionContext.getCurrentContext());
    }

    @Test
    public void testSetPosition_OutOfBounds() {
        EvalContext context1 = new TestEvalContext();
        EvalContext context2 = new TestEvalContext();
        UnionContext unionContext = new UnionContext(new TestEvalContext(), new EvalContext[] { context1, context2 });

        context1.setNextSet(true);
        context1.setCurrentNodePointer(new NodePointer(1, 1));
        context1.setNextNode(null);

        context2.setNextSet(true);
        context2.setCurrentNodePointer(new NodePointer(2, 1));
        context2.setNextNode(null);

        unionContext.setPosition(unionContext.size());
        assertNull(unionContext.getCurrentNodePointer());
    }
}

class TestEvalContext extends EvalContext {
    @Override
    public void setNextNode(Node node) {
        // do nothing
    }

    @Override
    public Node nextNode() {
        return null;
    }

    @Override
    public void nextSet() {
        // do nothing
    }

    @Override
    public boolean nextSet() {
        return false;
    }
}