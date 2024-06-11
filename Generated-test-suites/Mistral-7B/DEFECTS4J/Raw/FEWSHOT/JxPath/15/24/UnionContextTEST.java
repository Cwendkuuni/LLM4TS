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
    public void testUnionContext() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet());
        contextA.addNode(new NodePointer(1));
        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet());
        contextB.addNode(new NodePointer(2));

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        assertEquals(3, unionContext.getLength());
        assertEquals(2, unionContext.getDocumentOrder());
        assertFalse(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());

        unionContext.nextSet();
        assertSame(contextA.getNodeSet(), unionContext.getNodeSet());
        assertTrue(unionContext.hasNextNode());
        assertSame(contextA.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
        unionContext.nextNode();
        assertSame(contextA.getCurrentNodePointer().getNode(), unionContext.getCurrentNodePointer().getNode());
        assertSame(contextB.getNodeSet(), unionContext.getNodeSet());
        assertFalse(unionContext.hasNextNode());
        unionContext.nextSet();
        assertSame(contextB.getNodeSet(), unionContext.getNodeSet());
        assertTrue(unionContext.hasNextNode());
        assertSame(contextB.getCurrentNodePointer().getNode(), unionContext.getCurrentNodePointer().getNode());
        unionContext.nextNode();
        assertSame(contextB.getCurrentNodePointer().getNode(), unionContext.getCurrentNodePointer().getNode());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet());
        EvalContext contextB = new NodeSetContext(null, new BasicNodeSet());

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{});

        assertEquals(0, unionContext.getLength());
        assertEquals(0, unionContext.getDocumentOrder());
        assertFalse(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testUnionContextNullContexts() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet());
        EvalContext contextB = null;

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});

        assertEquals(1, unionContext.getLength());
        assertEquals(contextA.getDocumentOrder(), unionContext.getDocumentOrder());
        assertFalse(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());
    }

    @Test
    public void testUnionContextSingleContext() {
        EvalContext context = new NodeSetContext(null, new BasicNodeSet());
        context.addNode(new NodePointer(1));

        UnionContext unionContext = new UnionContext(null, new EvalContext[]{context});

        assertEquals(1, unionContext.getLength());
        assertEquals(context.getDocumentOrder(), unionContext.getDocumentOrder());
        assertFalse(unionContext.hasNextSet());
        assertFalse(unionContext.hasNextNode());
        assertSame(context.getNodeSet(), unionContext.getNodeSet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextNullContext() {
        EvalContext contextA = new NodeSetContext(null, new BasicNodeSet());
        EvalContext contextB = null;

        new UnionContext(null, new EvalContext[]{contextA, contextB});
    }
}