package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertEquals(11, iterator.getPosition());
        assertNull(iterator.getNodePointer().getNode().getPreviousSibling());
        assertNull(iterator.getNodePointer().getNode().getNextSibling());

        iterator.setPosition(0);
        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("id", attrPointer.getAttribute().getNodeName());
        assertEquals("id0", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(1);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("class", attrPointer.getAttribute().getNodeName());
        assertEquals("class1", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(12);
        assertNull(iterator.getNodePointer());

        iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("myNamespace:myAttribute"));
        iterator.setPosition(0);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());
        iterator.setPosition(1);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("myNamespace:myAttribute1", attrPointer.getAttribute().getNodeName());
        assertEquals("value1", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(2);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("myNamespace:myAttribute2", attrPointer.getAttribute().getNodeName());
        assertEquals("value2", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(13);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIteratorWithFilter() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("myAttribute"));
        assertNull(iterator.getNodePointer());

        iterator.setPosition(0);
        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());

        iterator.setPosition(1);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("myAttribute1", attrPointer.getAttribute().getNodeName());
        assertEquals("value1", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(2);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("myAttribute2", attrPointer.getAttribute().getNodeName());
        assertEquals("value2", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(12);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());

        iterator.setPosition(13);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());
    }

    @Test
    public void testDOMAttributeIteratorWithPrefix() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("myPrefix:myAttribute"));
        assertNull(iterator.getNodePointer());

        iterator.setPosition(0);
        DOMAttributePointer attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());

        iterator.setPosition(1);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("myPrefix:myAttribute1", attrPointer.getAttribute().getNodeName());
        assertEquals("value1", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(2);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attrPointer.getAttribute());
        assertEquals("myPrefix:myAttribute2", attrPointer.getAttribute().getNodeName());
        assertEquals("value2", attrPointer.getAttribute().getNodeValue());

        iterator.setPosition(12);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());

        iterator.setPosition(13);
        attrPointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNull(attrPointer.getAttribute());
    }

    @Test
    public void testDOMAttributeIteratorWithEmptyIterator() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), new QName("*"));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIteratorWithNullParent() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
        assertNull(iterator);
    }

    @Test
    public void testDOMAttributeIteratorWithNullName() {
        Element element = createTestElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(element), null);
        assertNull(iterator);
    }

    private Element createTestElement() {
        Element element = new ElementImpl("test");
        element.setAttribute("id", "id0");
        element.setAttribute("class", "class1");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:myNamespace", "http://www.example.com/mynamespace");
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:myPrefix", "http://www.example.com/myprefix");
        element.setAttribute("myAttribute1", "value1");
        element.setAttribute("myAttribute2", "value2");
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                element.appendChild(child);
            }
        }
        return element;
    }
}