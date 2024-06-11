package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = new QName("test", "attribute");
        iterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when((Element) node).thenReturn(element);
        when(element.getAttributeNode(name.getPrefix())).thenReturn(null);
        when(element.getAttributeNode(name.getLocalName())).thenReturn(null);
        when(element.getAttributeNode(name.getName())).thenReturn(null);
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        
        NodePointer nodePointer = iterator.getNodePointer();
        
        assertNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        int position = 3;
        boolean result = iterator.setPosition(position);
        
        assertTrue(result);
    }
}