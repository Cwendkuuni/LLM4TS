package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for AttributeContext
 */
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(attributeContext);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
        Assert.assertNotNull(attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(attributeContext.iterator.getNodePointer()).thenReturn(currentNodePointer);
        Assert.assertEquals(currentNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
    }

    @Test
    public void testSetPosition_whenValidPosition_returnsTrue() {
        List<NodePointer> nodes = new ArrayList<>();
        NodePointer node1 = Mockito.mock(NodePointer.class);
        NodePointer node2 = Mockito.mock(NodePointer.class);
        nodes.add(node1);
        nodes.add(node2);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodes.get(0));

        Mockito.when(node1.getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));

        attributeContext.setPosition(1);
        Assert.assertTrue(attributeContext.isSetStarted());
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertEquals(nodes.get(1), attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_whenInvalidPosition_returnsFalse() {
        List<NodePointer> nodes = new ArrayList<>();
        NodePointer node1 = Mockito.mock(NodePointer.class);
        NodePointer node2 = Mockito.mock(NodePointer.class);
        nodes.add(node1);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(node1);

        attributeContext.setPosition(2);
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_whenNotStarted_throwsException() {
        List<NodePointer> nodes = new ArrayList<>();
        NodePointer node1 = Mockito.mock(NodePointer.class);
        NodePointer node2 = Mockito.mock(NodePointer.class);
        nodes.add(node1);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(node1);
        Mockito.when(node1.getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));

        attributeContext.setPosition(1);
        Assert.assertFalse(attributeContext.nextNode());
        attributeContext.setPosition(1); // Should throw exception
    }

    @Test
    public void testNextNode() {
        List<NodePointer> nodes = new ArrayList<>();
        NodePointer node1 = Mockito.mock(NodePointer.class);
        NodePointer node2 = Mockito.mock(NodePointer.class);
        nodes.add(node1);
        NodePointer node3 = Mockito.mock(NodePointer.class);
        nodes.add(node2);
        nodes.add(node3);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(node1);
        Mockito.when(node1.getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));
        Mockito.when(node2.getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));

        attributeContext.setPosition(0);
        Assert.assertFalse(attributeContext.nextNode());

        attributeContext.setPosition(1);
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertEquals(node2, attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(2);
        Assert.assertTrue(attributeContext.nextNode());
        Assert.assertEquals(node3, attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(3);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNode_whenNotStarted_throwsException() {
        List<NodePointer> nodes = new ArrayList<>();
        NodePointer node1 = Mockito.mock(NodePointer.class);
        NodePointer node2 = Mockito.mock(NodePointer.class);
        nodes.add(node1);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(node1);
        Mockito.when(node1.getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));

        attributeContext.nextNode(); // Should throw exception
    }
}