package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("*"));
        assertTrue(iterator.hasNext());

        assertEquals(iterator.getNodePointer().getNode().getNodeName(), "root");
        assertEquals(iterator.getNodePointer().getNodeType(), Node.ELEMENT_NODE);

        assertFalse(iterator.hasNext());

        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xml:foo", "bar");
        iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xml:foo"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xml:foo");
        assertEquals(attrPointer.getNode().getValue(), "bar");

        assertFalse(iterator.hasNext());

        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xml:foo2", "baz");
        iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xml:foo"));
        assertTrue(iterator.hasNext());

        attrPointer = (DOMAttribePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xml:foo");
        assertEquals(attrPointer.getNode().getValue(), "bar");

        assertTrue(iterator.hasNext());

        attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xml:foo2");
        assertEquals(attrPointer.getNode().getValue(), "baz");

        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xml:nonMatchingName"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefix() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:foo", "*"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:foo");
        assertEquals(attrPointer.getNode().getValue(), "http://example.com/foo");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullParent() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("*"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "root");
        assertEquals(attrPointer.getNode().getNodeType(), Node.ELEMENT_NODE);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyParent() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("*"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "root");
        assertEquals(attrPointer.getNode().getNodeType(), Node.ELEMENT_NODE);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNameFiltering() {
        Element element = newElement("root", null);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "bar");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bar", "baz");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:foo"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:foo");
        assertEquals(attrPointer.getNode().getValue(), "bar");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefixAndNameFiltering() {
        Element element = newElement("root", null);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "bar");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bar", "baz");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:foo"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:foo");
        assertEquals(attrPointer.getNode().getValue(), "bar");

        iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:bar"));
        assertTrue(iterator.hasNext());

        attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:bar");
        assertEquals(attrPointer.getNode().getValue(), "baz");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNamespaceURI() {
        Element element = newElement("root", "http://example.com");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "bar");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bar", "baz");
        element.setAttributeNS("http://example.com", "my:attr", "value");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("my:attr"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "my:attr");
        assertEquals(attrPointer.getNode().getValue(), "value");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMixedCasePrefix() {
        Element element = newElement("root", null);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "XMLNS:foo", "bar");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("XMLNS:foo"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "XMLNS:foo");
        assertEquals(attrPointer.getNode().getValue(), "bar");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMixedCaseLocalName() {
        Element element = newElement("root", null);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "bar");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:Foo", "baz");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:Foo"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:Foo");
        assertEquals(attrPointer.getNode().getValue(), "baz");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithMultipleAttributes() {
        Element element = newElement("root", null);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "bar");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:bar", "baz");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:baz", "qux");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:foo"));
        assertTrue(iterator.hasNext());

        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:foo");
        assertEquals(attrPointer.getNode().getValue(), "bar");

        assertTrue(iterator.hasNext());

        attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:bar");
        assertEquals(attrPointer.getNode().getValue(), "baz");

        assertTrue(iterator.hasNext());

        attrPointer = (DOMAttributePointer) iterator.next();
        assertEquals(attrPointer.getNode().getNodeName(), "xmlns:baz");
        assertEquals(attrPointer.getNode().getValue(), "qux");

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullParentAndName() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName(null, null));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithNullName() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithParentThatIsNotAnElement() {
        Element element = newElement("root", null);
        Node textNode = newTextNode("foo");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName("xmlns:foo"));
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMElementPointer(null, textNode), new QName("xmlns:foo"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyName() {
        Element element = newElement("root", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(null, element), new QName(""));
        assertFalse(iterator.hasNext());
    }

    private static Element newElement(String tagName, String namespaceURI) {
        Element element = document.createElementNS(namespaceURI, tagName);
        document.appendChild(element);
        return element;
    }

    private static Node newTextNode(String text) {
        Node textNode = document.createTextNode(text);
        document.appendChild(textNode);
        return textNode;
    }

    private static Document document = newDocument();
}