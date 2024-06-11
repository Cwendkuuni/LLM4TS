package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class contains JUnit 4 test methods for the DOMAttributeIterator class.
 */
public class DOMAttributeIteratorTest {

    /**
     * Test method for {@link DOMAttributeIterator#DOMAttributeIterator(NodePointer, QName)} .
     */
    @org.junit.Test
    public void testDOMAttributeIterator() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("root");

        Element child1 = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("child1");
        child1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2005/xpath-functions");
        child1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://foo.com");
        child1.setAttribute("class", "red");
        child1.setAttribute("style", "color: red; background: yellow;");
        element.appendChild(child1);

        Element child2 = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("child2");
        element.appendChild(child2);

        NodePointer rootPointer = new DOMPointer(element);
        QName classQName = new QName("class");
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, classQName);

        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        Attr attribute = attributePointer.getNode();
        assertEquals("class", attribute.getNodeName());
        assertEquals("red", attribute.getNodeValue());

        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) nodePointer2;
        Attr attribute2 = attributePointer2.getNode();
        assertEquals("style", attribute2.getNodeName());
        assertEquals("color: red; background: yellow;", attribute2.getNodeValue());
    }

    /**
     * Test method for {@link DOMAttributeIterator#DOMAttributeIterator(NodePointer, QName)} with empty Iterator.
     */
    @org.junit.Test
    public void testEmptyDOMAttributeIterator() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("root");

        NodePointer rootPointer = new DOMPointer(element);
        QName nullQName = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, nullQName);

        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    /**
     * Test method for {@link DOMAttributeIterator#DOMAttributeIterator(NodePointer, QName)} with empty Node.
     */
    @org.junit.Test
    public void testEmptyNodeDOMAttributeIterator() {
        NodePointer rootPointer = new DOMPointer(new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest()
                .newDocumentBuilder().newDocument());
        QName nullQName = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, nullQName);

        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    /**
     * Test method for {@link DOMAttributeIterator#DOMAttributeIterator(NodePointer, QName)} with wildcard QName.
     */
    @org.junit.Test
    public void testWildcardDOMAttributeIterator() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("root");

        Element child1 = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("child1");
        child1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2005/xpath-functions");
        child1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://foo.com");
        child1.setAttribute("class", "red");
        child1.setAttribute("style", "color: red; background: yellow;");
        element.appendChild(child1);

        Element child2 = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("child2");
        element.appendChild(child2);

        NodePointer rootPointer = new DOMPointer(element);
        QName wildcardQName = new QName("*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, wildcardQName);

        assertNotNull(iterator);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        Attr attribute = attributePointer.getNode();

        // Test the first attribute
        assertEquals("class", attribute.getNodeName());
        assertEquals("red", attribute.getNodeValue());

        // Move to the second attribute
        iterator.next();
        nodePointer = iterator.getNodePointer();
        attributePointer = (DOMAttributePointer) nodePointer;
        attribute = attributePointer.getNode();

        // Test the second attribute
        assertEquals("style", attribute.getNodeName());
        assertEquals("color: red; background: yellow;", attribute.getNodeValue());

        // Move to the next attribute (none)
        iterator.next();
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    /**
     * Test method for {@link DOMAttributeIterator#DOMAttributeIterator(NodePointer, QName)} with non-matching QName.
     */
    @org.junit.Test
    public void testNonMatchingDOMAttributeIterator() {
        Element element = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("root");

        Element child1 = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("child1");
        child1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2005/xpath-functions");
        child1.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:foo", "http://foo.com");
        child1.setAttribute("class", "red");
        child1.setAttribute("style", "color: red; background: yellow;");
        element.appendChild(child1);

        Element child2 = new org.apache.commons.jxpath.ri.model.dom.DocumentBuilderFactoryTest().newDocumentBuilder()
                .newDocument().createElement("child2");
        element.appendChild(child2);

        NodePointer rootPointer = new DOMPointer(element);
        QName nonMatchingQName = new QName("nonExistent");
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, nonMatchingQName);

        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }
}