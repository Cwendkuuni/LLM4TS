package org.apache.commons.jxpath.ri.axes.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeSetContext;
import org.apache.commons.jxpath.ri.model.NodeSetContext.NodeSetType;
import org.apache.commons.jxpath.ri.test.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionContextTest {

    private EvalContext contextA, contextB, unionContext;
    private NodeSetContext nodeSetContextA, nodeSetContextB, nodeSetContextUnion;
    private List<NodePointer> nodeSetA, nodeSetB, nodeSetUnion;

    @Before
    public void setUp() {
        contextA = new EvalContext(new NodeSetContext(new BasicNodeSet(), NodeSetType.NODE_SET, null));
        contextB = new EvalContext(new NodeSetContext(new BasicNodeSet(), NodeSetType.NODE_SET, null));

        nodeSetContextA = (NodeSetContext) contextA.getNodeSetContext();
        nodeSetContextB = (NodeSetContext) contextB.getNodeSetContext();
        nodeSetA = new ArrayList<>();
        nodeSetB = new ArrayList<>();

        nodeSetContextA.setNodeSet(new BasicNodeSet(Arrays.asList(new NodePointer[]{TestUtils.createNodePointer("A1"), TestUtils.createNodePointer("A2"), TestUtils.createNodePointer("A3")})));
        nodeSetContextB.setNodeSet(new BasicNodeSet(Arrays.asList(new NodePointer[]{TestUtils.createNodePointer("B1"), TestUtils.createNodePointer("B2"), TestUtils.createNodePointer("B3"), TestUtils.createNodePointer("B4")})));

        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        nodeSetContextUnion = (NodeSetContext) unionContext.getNodeSetContext();
        nodeSetUnion = new ArrayList<>();
    }

    @Test
    public void testGetterMethods() {
        assertEquals(2, unionContext.getContexts.length);
        assertFalse(unionContext.isPrepared());
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        unionContext.setPosition(0);
        assertTrue(nodeSetUnion.isEmpty());
        unionContext.setPosition(1);
        unionContext.setPosition(1);
        unionContext.setPosition(3);
        assertEquals(4, nodeSetUnion.size());
        assertEquals(nodeSetB.get(2), nodeSetUnion.get(0));
        assertEquals(nodeSetB.get(3), nodeSetUnion.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionInvalidIndex() {
        unionContext.setPosition(5);
    }

    @Test
    public void testGetCurrentNodePointer() {
        unionContext.setPosition(0);
        assertEquals(nodeSetA.get(0), unionContext.getCurrentNodePointer());
        unionContext.setPosition(1);
        assertEquals(nodeSetB.get(2), unionContext.getCurrentNodePointer());
        unionContext.setPosition(3);
        assertEquals(nodeSetB.get(3), unionContext.getCurrentNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextSetAfterPrepared() {
        unionContext.nextSet();
    }

    @Test
    public void testNextNodeAfterLastNode() {
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        unionContext.nextNode();
        assertFalse(unionContext.nextNodeHasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeBeforeFirstNode() {
        unionContext.nextNode();
    }
}