package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element root = newElement("root", null);
        Element a = newElement("a", null);
        a.setAttribute("class", "A");
        a.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2000/xmlns/xlink");
        root.appendChild(a);

        NodePointer parent = new DOMPointer(root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));

        assertEquals(1, iterator.getPosition());
        assertSame(a, iterator.getNodePointer().getNode());
        assertSame(a.getAttributeNode("class"), iterator.getNodePointer().getNodeValue());

        assertEquals(1, iterator.getPosition());
        assertSame(a.getAttributeNode("class"), iterator.getNodePointer().getNodeValue());

        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.getPosition());
        assertSame(a.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns:x"), iterator.getNodePointer().getNodeValue());

        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.getPosition());
        assertNull(iterator.getNodePointer().getNodeValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithQName() {
        Element root = newElement("root", null);
        Element a = newElement("a", null);
        a.setAttribute("class", "A");
        a.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2000/xmlns/xlink");
        root.appendChild(a);

        NodePointer parent = new DOMPointer(root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("xmlns:x", "xmlns"));

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getPosition());
        assertSame(a.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns:x"), iterator.getNodePointer().getNodeValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyElement() {
        Element root = newElement("root", null);
        NodePointer parent = new DOMPointer(root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*"));

        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorWithNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDOMAttributeIteratorWithNullName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(newElement("root", null)), null);
    }

    private Element newElement(String tagName, String namespaceURI) {
        Element element = document.createElement(tagName);
        if (namespaceURI != null) {
            document.setDocumentNamespaceURI(namespaceURI);
        }
        document.appendChild(element);
        return element;
    }

    private Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
}