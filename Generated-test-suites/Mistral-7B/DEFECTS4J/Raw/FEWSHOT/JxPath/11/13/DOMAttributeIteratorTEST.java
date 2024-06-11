package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    @Test
    public void testCreateDOMAttributeIterator() {
        Element element = new org.w3c.dom.ElementImpl();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("test"));
        assertNotNull(iterator);
    }

    @Test
    public void testCreateDOMAttributeIteratorWithWildcardName() {
        Element element = new org.w3c.dom.ElementImpl();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));
        assertNotNull(iterator);
    }

    @Test
    public void testCreateDOMAttributeIteratorWithQualifiedName() {
        Element element = new org.w3c.dom.ElementImpl();
        String namespace = "http://test.com/";
        element.setNamespaceURI(namespace);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName(namespace, "test"));
        assertNotNull(iterator);
    }

    @Test
    public void testCreateDOMAttributeIteratorWithNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("test"));
        assertNull(iterator);
    }

    @Test
    public void testCreateDOMAttributeIteratorWithNullName() {
        NodePointer parent = new NodePointer(new org.w3c.dom.ElementImpl());
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertNull(iterator);
    }

    @Test
    public void testNextAttribute() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("test"));

        NodePointer firstAttribute = iterator.getNodePointer();
        assertNotNull(firstAttribute);
        assertEquals("test1", firstAttribute.getName().getLocalPart());
        assertEquals("value1", firstAttribute.getNodeValue());

        iterator.next();
        NodePointer secondAttribute = iterator.getNodePointer();
        assertNotNull(secondAttribute);
        assertEquals("test2", secondAttribute.getName().getLocalPart());
        assertEquals("value2", secondAttribute.getNodeValue());

        iterator.next();
        NodePointer thirdAttribute = iterator.getNodePointer();
        assertNull(thirdAttribute);
    }

    @Test
    public void testNextAttributeWithWildcardName() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));

        NodePointer firstAttribute = iterator.getNodePointer();
        assertNotNull(firstAttribute);
        assertEquals("test1", firstAttribute.getName().getLocalPart());
        assertEquals("value1", firstAttribute.getNodeValue());

        iterator.next();
        NodePointer secondAttribute = iterator.getNodePointer();
        assertNotNull(secondAttribute);
        assertEquals("test2", secondAttribute.getName().getLocalPart());
        assertEquals("value2", secondAttribute.getNodeValue());

        iterator.next();
        NodePointer thirdAttribute = iterator.getNodePointer();
        assertNull(thirdAttribute);
    }

    @Test
    public void testNextAttributeWithQualifiedName() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setNamespaceURI("http://test.com/");
        element.setAttributeNS("http://test.com/", "test1", "value1");
        element.setAttributeNS("http://test.com/", "test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test.com/", "test"));

        NodePointer firstAttribute = iterator.getNodePointer();
        assertNotNull(firstAttribute);
        assertEquals("test1", firstAttribute.getName().getLocalPart());
        assertEquals("value1", firstAttribute.getNodeValue());

        iterator.next();
        NodePointer secondAttribute = iterator.getNodePointer();
        assertNotNull(secondAttribute);
        assertEquals("test2", secondAttribute.getName().getLocalPart());
        assertEquals("value2", secondAttribute.getNodeValue());

        iterator.next();
        NodePointer thirdAttribute = iterator.getNodePointer();
        assertNull(thirdAttribute);
    }

    @Test
    public void testPreviousAttribute() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("test"));

        iterator.next();
        iterator.next();

        NodePointer secondAttribute = iterator.getNodePointer();
        assertNotNull(secondAttribute);
        assertEquals("test2", secondAttribute.getName().getLocalPart());
        assertEquals("value2", secondAttribute.getNodeValue());

        iterator.previous();
        NodePointer firstAttribute = iterator.getNodePointer();
        assertNotNull(firstAttribute);
        assertEquals("test1", firstAttribute.getName().getLocalPart());
        assertEquals("value1", firstAttribute.getNodeValue());

        iterator.previous();
        NodePointer thirdAttribute = iterator.getNodePointer();
        assertNull(thirdAttribute);
    }

    @Test
    public void testPreviousAttributeWithWildcardName() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));

        iterator.next();
        iterator.next();

        NodePointer secondAttribute = iterator.getNodePointer();
        assertNotNull(secondAttribute);
        assertEquals("test2", secondAttribute.getName().getLocalPart());
        assertEquals("value2", secondAttribute.getNodeValue());

        iterator.previous();
        NodePointer firstAttribute = iterator.getNodePointer();
        assertNotNull(firstAttribute);
        assertEquals("test1", firstAttribute.getName().getLocalPart());
        assertEquals("value1", firstAttribute.getNodeValue());

        iterator.previous();
        NodePointer thirdAttribute = iterator.getNodePointer();
        assertNull(thirdAttribute);
    }

    @Test
    public void testPreviousAttributeWithQualifiedName() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setNamespaceURI("http://test.com/");
        element.setAttributeNS("http://test.com/", "test1", "value1");
        element.setAttributeNS("http://test.com/", "test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test.com/", "test"));

        iterator.next();
        iterator.next();

        NodePointer secondAttribute = iterator.getNodePointer();
        assertNotNull(secondAttribute);
        assertEquals("test2", secondAttribute.getName().getLocalPart());
        assertEquals("value2", secondAttribute.getNodeValue());

        iterator.previous();
        NodePointer firstAttribute = iterator.getNodePointer();
        assertNotNull(firstAttribute);
        assertEquals("test1", firstAttribute.getName().getLocalPart());
        assertEquals("value1", firstAttribute.getNodeValue());

        iterator.previous();
        NodePointer thirdAttribute = iterator.getNodePointer();
        assertNull(thirdAttribute);
    }

    @Test
    public void testHasNextAttribute() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("test"));

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator.next();
        iterator.next();

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextAttributeWithWildcardName() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setAttribute("test1", "value1");
        element.setAttribute("test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("*"));

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator.next();
        iterator.next();

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextAttributeWithQualifiedName() {
        Element element = new org.w3c.dom.ElementImpl();
        element.setNamespaceURI("http://test.com/");
        element.setAttributeNS("http://test.com/", "test1", "value1");
        element.setAttributeNS("http://test.com/", "test2", "value2");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element), new QName("http://test.com/", "test"));

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());

        iterator.next();
        iterator.next();

        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }
}