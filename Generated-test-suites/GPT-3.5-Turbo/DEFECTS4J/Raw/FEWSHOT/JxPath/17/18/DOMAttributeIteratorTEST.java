package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;
    private Element element;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        name = new QName("testNamespace", "testPrefix", "testName");
        iterator = new DOMAttributeIterator(parent, name);
        element = Mockito.mock(Element.class);
        
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(element.getNodeType()).thenReturn(Node.ELEMENT_NODE);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertEquals(0, iterator.position);
    }

    @Test
    public void testGetAttribute() {
        QName attributeName = new QName("attributeNamespace", "attributePrefix", "attributeName");
        Attr attribute = Mockito.mock(Attr.class);
        
        Mockito.when(parent.getNamespaceResolver()).thenReturn(Mockito.mock(NamespaceResolver.class));
        Mockito.when(element.getAttributeNodeNS(Mockito.isNull(String.class), Mockito.eq(name.getName()))).thenReturn(attribute);
        
        iterator.getAttribute(element, name);
        
        Mockito.verify(element).getAttributeNodeNS(Mockito.isNull(String.class), Mockito.eq(name.getName()));
    }
    
    @Test
    public void testGetAttributeFallback() {
        QName attributeName = new QName("attributeNamespace", "attributePrefix", "attributeName");
        Attr attribute = Mockito.mock(Attr.class);
        NamedNodeMap namedNodeMap = Mockito.mock(NamedNodeMap.class);
        
        Mockito.when(parent.getNamespaceResolver()).thenReturn(Mockito.mock(NamespaceResolver.class));
        Mockito.when(element.getAttributes()).thenReturn(namedNodeMap);
        Mockito.when(namedNodeMap.getLength()).thenReturn(1);
        Mockito.when(namedNodeMap.item(0)).thenReturn(attribute);
        Mockito.when(attribute.getPrefix()).thenReturn(name.getPrefix());
        Mockito.when(attribute.getLocalName()).thenReturn(name.getName());
        Mockito.when(iterator.testAttr(attribute)).thenReturn(true);
        
        iterator.getAttribute(element, name);
        
        Mockito.verify(element).getAttributes();
        Mockito.verify(namedNodeMap).item(0);
    }

    @Test
    public void testTestAttr() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(parent.getNamespaceURI(Mockito.notNull(String.class))).thenReturn("namespaceURI");
        Mockito.when(attr.getPrefix()).thenReturn("testPrefix");
        
        iterator.testAttr(attr);
        
        Mockito.verify(parent).getNamespaceURI(Mockito.notNull(String.class));
        Mockito.verify(attr).getPrefix();
    }
    
    @Test
    public void testEqualStrings() {
        assertEquals(true, iterator.equalStrings("test", "test"));
    }

    @Test
    public void testGetNodePointer() {
        Attr attr = Mockito.mock(Attr.class);
        DOMAttributePointer attributePointer = Mockito.mock(DOMAttributePointer.class);
        iterator.attributes.add(attr);

        Mockito.when(parent.getNamespaceResolver()).thenReturn(Mockito.mock(NamespaceResolver.class));
        Mockito.when(attr.getPrefix()).thenReturn("testPrefix");
        
        iterator.getNodePointer();
        
        Mockito.verify(parent).getNamespaceURI(Mockito.notNull(String.class));
        Mockito.verify(attr).getPrefix();
        Mockito.verify(attributePointer).getAttributeNode();
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertEquals(true, iterator.setPosition(1));
        assertEquals(1, iterator.position);
    }
}