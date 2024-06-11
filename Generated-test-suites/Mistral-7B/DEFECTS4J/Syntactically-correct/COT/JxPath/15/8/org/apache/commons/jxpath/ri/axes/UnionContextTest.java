package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UnionContextTest.java
 */
public class UnionContextTest {

    private EvalContext context1;
    private EvalContext context2;
    private NodeSetContext nodeSetContext;
    private UnionContext unionContext;

    @Before
    public void setUp() {
        context1 = new EvalContext(new BasicNodeSet(), new Object[]{}, new Object[0]);
        context2 = new EvalContext(new BasicNodeSet(), new Object[]{}, new Object[0]);
        nodeSetContext = new NodeSetContext(new BasicNodeSet(), null);
    }

    @Test
    public void testUnionContextConstructor() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        assertNotNull(unionContext);
        assertEquals(2, unionContext.contexts.length);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        assertEquals(1, unionContext.getDocumentOrder());

        UnionContext unionContext1 = new UnionContext(nodeSetContext, new EvalContext[] {context2, context1});
        assertEquals(1, unionContext1.getDocumentOrder());
    }

    @Test
    public void testSetPosition_success() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});

        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context1.add(new NodePointer(3));

        context2.add(new NodePointer(3));
        context2.add(new NodePointer(4));
        context2.add(new NodePointer(5));

        unionContext.setPosition(3);

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(3, nodePointer.getNodeNumber());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_outOfBounds() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});

        unionContext.setPosition(3);

        unionContext.setPosition(10);
    }

    @Test
    public void testGetCurrentNodePointer_empty() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        unionContext.setPosition(0);

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetCurrentNodePointer_singleContext_firstNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1});
        context1.add(new NodePointer(1));

        unionContext.setPosition(0);

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(1, nodePointer.getNodeNumber());
    }

    @Test
    public void testGetCurrentNodePointer_singleContext_lastNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));

        unionContext.setPosition(1);

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(2, nodePointer.getNodeNumber());
    }

    @Test
    public void testGetCurrentNodePointer_multipleContexts_firstNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        unionContext.setPosition(0);

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(1, nodePointer.getNodeNumber());
    }

    @Test
    public void testGetCurrentNodePointer_multipleContexts_lastNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));
        context2.add(new NodePointer(4));

        unionContext.setPosition(3);

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(4, nodePointer.getNodeNumber());
    }

    @Test
    public void testGetCurrentNodePointer_multipleContexts_sameNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(1));
        context2.add(new NodePointer(1));

        unionContext.setPosition(0);

        NodePointer nodePointer1 = unionContext.getCurrentNodePointer();
        assertEquals(1, nodePointer1.getNodeNumber());

        unionContext.next();

        NodePointer nodePointer2 = unionContext.getCurrentNodePointer();
        assertEquals(1, nodePointer2.getNodeNumber());
    }

    @Test
    public void testNext_success() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        unionContext.setPosition(0);

        unionContext.next();

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(1, nodePointer.getNodeNumber());
    }

    @Test
    public void testNext_lastNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        unionContext.setPosition(2);

        unionContext.next();

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertEquals(3, nodePointer.getNodeNumber());
    }

    @Test
    public void testNext_emptyContext() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        context2.nextSet();

        unionContext.setPosition(2);

        unionContext.next();

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testHasNext_success() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        unionContext.setPosition(0);

        assertTrue(unionContext.hasNext());

        unionContext.next();

        assertFalse(unionContext.hasNext());
    }

    @Test
    public void testHasNext_emptyContext() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1, context2});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));
        context2.add(new NodePointer(3));

        context2.nextSet();

        unionContext.setPosition(2);

        assertFalse(unionContext.hasNext());
    }

    @Test
    public void testHasNext_singleContext_lastNode() {
        UnionContext unionContext = new UnionContext(nodeSetContext, new EvalContext[] {context1});
        context1.add(new NodePointer(1));
        context1.add(new NodePointer(2));

        unionContext.setPosition(1);

        assertTrue(unionContext.hasNext());

        unionContext.next();

        assertFalse(unionContext.hasNext());
    }
}