
package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    
    @Mock
    private NodePointer parent;
    
    private DOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        iterator = new DOMAttributeIterator(parent, new QName("*"));
    }

    @Test
    public void testDOMAttributeIterator() {
        assertNotNull(iterator);
    }
    
    @Test
    public void testGetAttribute() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElement("test");
        Attr attr = document.createAttribute("attr");
        element.setAttributeNode(attr);
        
        Attr result = iterator.getAttribute(element, new QName("attr"));
        assertNotNull(result);
        assertEquals("attr", result.getName());
    }
    
    @Test
    public void testGetAttributeNamespace() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElementNS("http://example.com", "test");
        Attr attr = document.createAttributeNS("http://example.com", "attr");
        element.setAttributeNodeNS(attr);
        
        Attr result = iterator.getAttribute(element, new QName("attr", "http://example.com"));
        assertNotNull(result);
        assertEquals("attr", result.getName());
    }
    
    @Test
    public void testGetAttributeNamespaceNotFound() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElementNS("http://example.com", "test");
        Attr attr = document.createAttribute("attr");
        element.setAttributeNode(attr);
        
        Attr result = iterator.getAttribute(element, new QName("attr", "http://other.com"));
        assertNull(result);
    }
    
    @Test
    public void testGetAttributeNodeFound() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElementNS("http://example.com", "test");
        Attr attr = document.createAttributeNS("http://example.com", "attr");
        element.setAttributeNodeNS(attr);
        
        Attr result = iterator.getAttributeNode(element, new QName("attr", "http://example.com"));
        assertNotNull(result);
        assertEquals("attr", result.getName());
    }
    
    @Test
    public void testGetAttributeNodeNotFound() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElementNS("http://example.com", "test");
        Attr attr = document.createAttribute("attr");
        element.setAttributeNode(attr);
        
        Attr result = iterator.getAttributeNode(element, new QName("attr", "http://other.com"));
        assertNull(result);
    }
    
    @Test
    public void testTestAttr() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElementNS("http://example.com", "test");
        Attr attr = document.createAttributeNS("http://example.com", "attr");
        element.setAttributeNodeNS(attr);
        
        boolean result = iterator.testAttr(attr, new QName("*"));
        assertEquals(true, result);
    }
    
    @Test
    public void testEmptyAttributes() {
        iterator = new DOMAttributeIterator(parent, new QName("attr"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testAttributesNotEmpty() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElement("test");
        Attr attr1 = document.createAttribute("attr1");
        Attr attr2 = document.createAttribute("attr2");
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);
        
        iterator = new DOMAttributeIterator(parent, new QName("attr"));
        assertNotNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
        assertNotNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testGetNodePointer() {
        Document document = DocumentBuilder.newDocument();
        Element element = document.createElement("test");
        Attr attr = document.createAttribute("attr");
        element.setAttributeNode(attr);
        
        iterator = new DOMAttributeIterator(parent, new QName("attr"));
        assertNotNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionWithinRange() {
        boolean result = iterator.setPosition(1);
        assertEquals(true, result);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPositionOutOfRange() {
        boolean result = iterator.setPosition(10);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualStringsSameString() {
        boolean result = iterator.equalStrings("test", "test");
        assertEquals(true, result);
    }
    
    @Test
    public void testEqualStringsDifferentStrings() {
        boolean result = iterator.equalStrings("test", "other");
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualStringsOneStringNull() {
        boolean result = iterator.equalStrings("test", null);
        assertEquals(false, result);
    }
    
    @Test
    public void testEqualStringsBothStringsNull() {
        boolean result = iterator.equalStrings(null, null);
        assertEquals(true, result);
    }
    
    private final static class DocumentBuilder {
        public static Document newDocument() {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                return builder.newDocument();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
