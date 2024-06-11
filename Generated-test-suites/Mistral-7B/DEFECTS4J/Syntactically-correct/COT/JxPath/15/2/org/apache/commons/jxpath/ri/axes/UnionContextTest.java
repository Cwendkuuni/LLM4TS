package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.RITestBase;
import org.apache.commons.jxpath.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for UnionContext
 */
public class UnionContextTest extends RITestBase {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertTrue(unionContext.getDocumentOrder() == 1);

        EvalContext parentContextSingle = new EvalContext(new BasicNodeSet());
        UnionContext unionContextSingle = new UnionContext(parentContextSingle, new EvalContext[0]);
        assertTrue(unionContextSingle.getDocumentOrder() == 0);
    }

    @Test
    public void testSetPosition_emptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_singleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
        NodePointer nodePointer = new NodePointer(new Object(), 0);
        context.setCurrentNodePointer(nodePointer);
        context.nextNode();
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer));
    }

    @Test
    public void testSetPosition_multipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        NodePointer nodePointer1 = new NodePointer(new Object(), 0);
        context1.setCurrentNodePointer(nodePointer1);
        context1.nextNode();

        NodePointer nodePointer2 = new NodePointer(new Object(), 1);
        context2.setCurrentNodePointer(nodePointer2);
        context2.nextNode();

        List<NodePointer> nodes = new ArrayList<>();
        nodes.add(nodePointer1);
        nodes.add(nodePointer2);

        UnionContext clone = new UnionContext(new EvalContext(new BasicNodeSet()), contextsForTest(context1, context2));
        assertTrue(clone.setPosition(0));
        assertTrue(clone.getCurrentNodePointer().equals(nodePointer1));

        nodes.remove(0);
        nodes.add(nodePointer2);
        UnionContext unionContext = new UnionContext(parentContext, contextsForTest(context1, context2));
        assertTrue(unionContext.setPosition(1));
        assertTrue(unionContext.getCurrentNodePointer().equals(nodePointer2));
    }

    /**
     * Helper method to create EvalContext array for test
     *
     * @param contexts contexts to be added to array
     * @return array of contexts
     */
    private EvalContext[] contextsForTest(EvalContext... contexts) {
        return Arrays.copyOf(contexts, contexts.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNextNode_unpreparedContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
        context.nextNode();
        unionContext.nextNode();
    }

    @Test
    public void testNextNode_preparedContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        context1.nextSet();
        context1.nextNode();
        context2.nextSet();
        context2.nextNode();
        unionContext.nextNode();
        assertTrue(unionContext.getCurrentNodePointer().equals(context1.getCurrentNodePointer()));
        unionContext.nextNode();
        assertTrue(unionContext.getCurrentNodePointer().equals(context2.getCurrentNodePointer()));
    }

    @Test
    public void testNextSet_emptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[0]);
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testNextSet_singleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});
        assertTrue(unionContext.nextSet());
    }

    @Test
    public void testNextSet_multipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertTrue(unionContext.nextSet());
        context1.nextSet();
        context2.nextSet();
        unionContext.nextSet();
        assertTrue(unionContext.nextSet());
    }
}