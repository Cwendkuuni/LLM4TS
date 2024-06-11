package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.Attribute;
import org.apache.commons.jxpath.ri.model.Document;
import org.apache.commons.jxpath.ri.model.Element;
import org.apache.commons.jxpath.ri.model.Node;
import org.apache.commons.jxpath.ri.model.NodeIteratorimpl;
import org.apache.commons.jxpath.ri.model.NodePointerimpl;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private Attribute attribute;
    private Element element;
    private Document document;

    @Before
    public void setup() {
        parentContext = mock(EvalContext.class);
        nodeTest = mock(NodeNameTest.class);
        attribute = mock(Attribute.class);
        element = mock(Element.class);
        document = mock(Document.class);
        when(element.getAttributeNode("test")).thenReturn(attribute);
        when(parentContext.getCurrentNodePointer()).thenReturn(new NodePointerimpl(element, 0));
        when(attribute.getParentNode()).thenReturn(element);
        when(document.getDocumentElement()).thenReturn(element);
    }

    @Test
    public void testAttributeContext() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(1);
        NodePointer expectedNodePointer = new NodePointerimpl(element, 1);
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(actualNodePointer);
        assertEquals(expectedNodePointer.getNode(), actualNodePointer.getNode());
    }

    @Test
    public void testNextNode() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = mock(NodeIteratorimpl.class);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(iterator);
        NodePointer expectedNodePointer = new NodePointerimpl(attribute, 0);
        attributeContext.nextNode();
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(actualNodePointer);
        assertEquals(expectedNodePointer.getNode(), actualNodePointer.getNode());
    }

    @Test
    public void testNextNode_AttributeIteratorIsEmpty() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(null);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_AttributeIteratorHasNoNext() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        NodeIterator iterator = mock(NodeIteratorimpl.class);
        when(parentContext.getCurrentNodePointer().attributeIterator(nodeTest)).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(false);
        assertNull(attributeContext.getCurrentNodePointer());
    }
}