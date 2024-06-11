package org.apache.commons.jxpath.ri.axes.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.axes.UnionContext;

/**
 * Tests for UnionContext class
 */
public class UnionContextTest {

    private EvalContext contextA;
    private EvalContext contextB;
    private UnionContext unionContext;

    @Before
    public void setup() {
        contextA = new EvalContext(new BasicNodeSet(), new NodePointerFactory());
        contextB = new EvalContext(new BasicNodeSet(), new NodePointerFactory());
    }

    @Test
    public void testUnionContextConstructor() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
        assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_success() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
        List<NodePointer> nodesA = new ArrayList<>();
        List<NodePointer> nodesB = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nodesA.add(NodePointerFactory.createNodePointer(i, "nodeA_" + i));
            nodesB.add(NodePointerFactory.createNodePointer(i + 5, "nodeB_" + i));
        }
        contextA.setNodeSet(new BasicNodeSet(nodesA.toArray(new NodePointer[0])));
        contextB.setNodeSet(new BasicNodeSet(nodesB.toArray(new NodePointer[0])));

        unionContext.setPosition(0);
        NodePointer node = unionContext.getCurrentNode();
        assertNotNull(node);
        assertTrue(Arrays.asList(contextA.getCurrentNodes(), contextB.getCurrentNodes()).contains(node));

        unionContext.nextSet();
        node = unionContext.getCurrentNode();
        assertNull(node);
        assertFalse(contextA.hasNextSet());
        assertFalse(contextB.hasNextSet());

        unionContext.nextSet();
        node = unionContext.getCurrentNode();
        assertNotNull(node);
        assertTrue(Arrays.asList(contextA.getCurrentNodes(), contextB.getCurrentNodes()).contains(node));

        unionContext.nextNode();
        node = unionContext.getCurrentNode();
        assertNotNull(node);
        assertTrue(Arrays.asList(contextA.getCurrentNodes(), contextB.getCurrentNodes()).contains(node));

        unionContext.nextNode();
        node = unionContext.getCurrentNode();
        assertNull(node);
        assertFalse(contextA.hasNextNode());
        assertFalse(contextB.hasNextNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextConstructor_emptyContexts() {
        new UnionContext(null, new EvalContext[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextConstructor_nullContexts() {
        new UnionContext(null, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_outOfBounds() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
        unionContext.setPosition(contextsLength() * 2);
    }
}