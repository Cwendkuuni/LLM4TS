package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * UnionContext Test Cases
 */
public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertNotNull(unionContext);
        assertEquals(parentContext, unionContext.getParentContext());
        assertNotNull(unionContext.getNodeSet());
        assertTrue(unionContext.isUnionContext());
        assertEquals(2, unionContext.getContexts().length);
    }

    @Test
    public void testGetDocumentOrder() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        assertEquals(1, unionContext.getDocumentOrder());
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

        List<NodePointer> pointers = Arrays.asList(new NodePointer(0), new NodePointer(1), new NodePointer(2));
        context.setNodeSet(new BasicNodeSet(pointers));

        assertTrue(unionContext.setPosition(0));
        assertEquals(pointers.get(0), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_multipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        List<NodePointer> pointers1 = Arrays.asList(new NodePointer(0), new NodePointer(1), new NodePointer(2));
        context1.setNodeSet(new BasicNodeSet(pointers1));

        List<NodePointer> pointers2 = Arrays.asList(new NodePointer(3), new NodePointer(4), new NodePointer(5));
        context2.setNodeSet(new BasicNodeSet(pointers2));

        assertTrue(unionContext.setPosition(0));
        assertEquals(pointers1.get(0), unionContext.getCurrentNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_beyondLastPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context});

        List<NodePointer> pointers = Arrays.asList(new NodePointer(0), new NodePointer(1), new NodePointer(2));
        context.setNodeSet(new BasicNodeSet(pointers));

        unionContext.setPosition(pointers.size());
    }

    @Test
    public void testSetPosition_reachedEndOfContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        List<NodePointer> pointers1 = Arrays.asList(new NodePointer(0), new NodePointer(1), new NodePointer(2));
        context1.setNodeSet(new BasicNodeSet(pointers1));

        List<NodePointer> pointers2 = Arrays.asList(new NodePointer(3), new NodePointer(4), new NodePointer(5));
        context2.setNodeSet(new BasicNodeSet(pointers2));

        unionContext.setPosition(pointers1.size());
        assertTrue(unionContext.nextSet());
        assertNull(unionContext.getCurrentNodePointer());

        unionContext.nextNode();
        assertEquals(pointers2.get(0), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_samePositionInDifferentContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});

        List<NodePointer> pointers1 = Arrays.asList(new NodePointer(0), new NodePointer(1), new NodePointer(2));
        context1.setNodeSet(new BasicNodeSet(pointers1));

        List<NodePointer> pointers2 = Arrays.asList(new NodePointer(1), new NodePointer(2), new NodePointer(3));
        context2.setNodeSet(new BasicNodeSet(pointers2));

        unionContext.setPosition(1);
        assertEquals(pointers1.get(1), unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextSet());
        assertEquals(pointers2.get(0), unionContext.getCurrentNodePointer());
    }
}