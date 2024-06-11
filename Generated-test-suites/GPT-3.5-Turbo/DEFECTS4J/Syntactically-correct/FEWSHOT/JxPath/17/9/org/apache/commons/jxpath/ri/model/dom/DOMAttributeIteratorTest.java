package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
        name = new QName("testPrefix", "testLocalName");
        iterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.getParent());
        assertEquals(name, iterator.getName());
        assertNull(iterator.getPosition());
        assertNull(iterator.getAttributes());
    }
    
    @Test
    public void testTestAttr() {
        Attr attr = mock(Attr.class);
        when(attr.getNamespaceURI()).thenReturn("testNamespace");
        when(attr.getLocalName()).thenReturn("testLocalName");
        when(attr.getPrefix()).thenReturn("testPrefix");
        when(parent.getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        
        assertEquals(true, iterator.testAttr(attr));
    }
    
    @Test
    public void testEqualStrings() {
        assertEquals(true, DOMAttributeIterator.equalStrings(null, null));
        assertEquals(false, DOMAttributeIterator.equalStrings("test", null));
        assertEquals(false, DOMAttributeIterator.equalStrings(null, "test"));
        assertEquals(true, DOMAttributeIterator.equalStrings("test", "test"));
        assertEquals(false, DOMAttributeIterator.equalStrings("test1", "test2"));
    }
    
    @Test
    public void testGetAttribute_withNamespace() {
        Element element = mock(Element.class);
        when(element.getAttributeNodeNS("testNamespace", "testLocalName")).thenReturn(mock(Attr.class));
        when(parent.getNamespaceResolver().getNamespaceURI("testPrefix")).thenReturn("testNamespace");
        
        assertNotNull(iterator.getAttribute(element, name));
    }
    
    @Test
    public void testGetAttribute_withoutNamespace() {
        Element element = mock(Element.class);
        when(element.getAttributeNode("testLocalName")).thenReturn(mock(Attr.class));
        
        assertNotNull(iterator.getAttribute(element, name));
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(parent, nodePointer.getParent());
        assertEquals(attributes.get(0), nodePointer.getNode());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionValid() {
        assertEquals(true, iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionInvalid() {
        assertEquals(false, iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
    }
}