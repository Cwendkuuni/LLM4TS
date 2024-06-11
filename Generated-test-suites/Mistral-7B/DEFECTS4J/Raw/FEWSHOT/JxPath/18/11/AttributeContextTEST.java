package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    @Test
    public void testAttributeContextInitialization() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertEquals(parentContext, attributeContext.getParentContext());
        assertNull(attributeContext.getNodeIterator());
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetStarted());
    }

    @Test
    public void testAttributeContextNextNodeWithValidAttributeIterator() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        QName name = Mockito.mock(QName.class);
        NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);
        NodePointer attributeNodePointer = Mockito.mock(NodePointer.class);
        NodePointer parentNodePointer = Mockito.mock(NodePointer.class);

        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(parentNodePointer);
        Mockito.when(parentNodePointer.attributeIterator(name)).thenReturn(attributeIterator);
        Mockito.when(attributeIterator.getNodePointer()).thenReturn(attributeNodePointer);
        Mockito.when(attributeNodePointer.getNode()).thenReturn(new Object());

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        Mockito.verify(parentContext, Mockito.times(1)).getCurrentNodePointer();
        Mockito.verify(parentNodePointer, Mockito.times(1)).attributeIterator(name);
        Mockito.verify(attributeIterator, Mockito.times(1)).getNodePointer();
        Mockito.verify(attributeNodePointer, Mockito.times(1)).getNode();

        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testAttributeContextNextNodeWithEmptyAttributeIterator() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);
        NodePointer attributeNodePointer = Mockito.mock(NodePointer.class);

        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointer(new Object()));
        Mockito.when(parentContext.getCurrentPosition()).thenReturn(0);
        Mockito.when(parentContext.getNodeIterator()).thenReturn(attributeIterator);
        Mockito.when(nodeTest instanceof NodeNameTest).thenReturn(true);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertTrue(attributeContext.nextNode());
        assertFalse(attributeContext.nextNode());
        Mockito.verify(parentContext, Mockito.times(1)).getNodeIterator();
    }

    @Test
    public void testAttributeContextNextNodeWithNonAttributeNodeIterator() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);

        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointer(new Object()));
        Mockito.when(parentContext.getCurrentPosition()).thenReturn(0);
        Mockito.when(parentContext.getNodeIterator()).thenReturn(attributeIterator);
        Mockito.when(nodeTest instanceof NodeNameTest).thenReturn(true);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        Mockito.when(attributeIterator.getNodeType()).thenReturn(NodeIterator.TYPE_ELEMENT);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testAttributeContextSetPositionWithValidPosition() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);
        NodePointer attributeNodePointer = Mockito.mock(NodePointer.class);

        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointer(new Object()));
        Mockito.when(parentContext.getNodeIterator()).thenReturn(attributeIterator);
        Mockito.when(nodeTest instanceof NodeNameTest).thenReturn(true);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        Mockito.verify(parentContext, Mockito.times(1)).getNodeIterator();

        attributeContext.setPosition(1);

        Mockito.verify(attributeIterator, Mockito.times(1)).setPosition(1);
    }

    @Test
    public void testAttributeContextSetPositionWithInvalidPosition() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);

        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointer(new Object()));
        Mockito.when(parentContext.getNodeIterator()).thenReturn(attributeIterator);
        Mockito.when(nodeTest instanceof NodeNameTest).thenReturn(true);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        Mockito.when(attributeIterator.getPosition()).thenReturn(1);

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        Mockito.verify(parentContext, Mockito.times(1)).getNodeIterator();

        assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testAttributeContextReset() {
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeNameTest.class);
        NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);

        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointer(new Object()));
        Mockito.when(parentContext.getNodeIterator()).thenReturn(attributeIterator);
        Mockito.when(parentContext.getPosition()).thenReturn(1);
        Mockito.when(nodeTest instanceof NodeNameTest).thenReturn(true);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));

        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        Mockito.verify(parentContext, Mockito.times(1)).reset();

        assertEquals(0, attributeContext.getCurrentPosition());
        assertNull(attributeContext.getNodeIterator());
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetStarted());
    }
}