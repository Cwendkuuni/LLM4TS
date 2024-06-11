package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JDOMAttributeIteratorTest.java
 */
public class JDOMAttributeIteratorTest {

    @org.junit.Before
    public void setUp() {
    }

    @org.junit.After
    public void tearDown() {
    }

    /**
     * Test constructor JDOMAttributeIterator.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("*"));
        assertEquals(element, iterator.parent.getNode());
        assertEquals(Collections.EMPTY_LIST, iterator.attributes);
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor JDOMAttributeIterator with specific attribute name.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testConstructorWithSpecificAttributeName() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("att1", "value1"));
        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration("ns", new Namespace("ns", "http://test.com"));
        element.addAttribute("att1", "value2", "ns");
        Element parent = new Element("parent");
        parent.addContent(root);
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("ns:att1"));

        assertEquals(parent, iterator.parent.getNode());
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(element.getAttribute("att1", "ns"));
        assertEquals(attributes, iterator.attributes);
        assertTrue(iterator.hasNext());

        NodePointer nodePointer = iterator.next();
        assertEquals(element, nodePointer.getNode().getParent());
        assertEquals("att1", nodePointer.getName().getName());
        assertEquals("value2", nodePointer.getName().getLocalPart());
        assertEquals("ns", nodePointer.getName().getPrefix());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att1", "ns"));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor JDOMAttributeIterator with all attributes.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testConstructorWithAllAttributes() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("att1", "value1"));
        root.addAttribute(new Attribute("att2", "value2"));
        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration("ns", new Namespace("ns", "http://test.com"));
        element.addAttribute("att1", "value3", "ns");
        element.addAttribute("att2", "value4", "ns");
        Element parent = new Element("parent");
        parent.addContent(root);
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*"));

        assertEquals(parent, iterator.parent.getNode());
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(root.getAttribute("att1"));
        attributes.add(root.getAttribute("att2"));
        attributes.addAll(element.getAttributes("ns"));
        assertEquals(attributes, iterator.attributes);
        assertTrue(iterator.hasNext());

        NodePointer nodePointer = iterator.next();
        assertEquals(root, nodePointer.getNode().getParent());
        assertEquals("att1", nodePointer.getName().getName());
        assertEquals(null, nodePointer.getName().getPrefix());
        assertEquals("value1", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att1"));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());

        nodePointer = iterator.next();
        assertEquals(root, nodePointer.getNode().getParent());
        assertEquals("att2", nodePointer.getName().getName());
        assertEquals(null, nodePointer.getName().getPrefix());
        assertEquals("value2", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att2"));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());

        nodePointer = iterator.next();
        assertEquals(element, nodePointer.getNode().getParent());
        assertEquals("att1", nodePointer.getName().getName());
        assertEquals("ns", nodePointer.getName().getPrefix());
        assertEquals("value3", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att1", "ns"));
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());

        nodePointer = iterator.next();
        assertEquals(element, nodePointer.getNode().getParent());
        assertEquals("att2", nodePointer.getName().getName());
        assertEquals("ns", nodePointer.getName().getPrefix());
        assertEquals("value4", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att2", "ns"));
        assertEquals(4, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getNodePointer.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testGetNodePointer() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("att1", "value1"));
        root.addAttribute(new Attribute("att2", "value2"));
        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration("ns", new Namespace("ns", "http://test.com"));
        element.addAttribute("att1", "value3", "ns");
        element.addAttribute("att2", "value4", "ns");
        Element parent = new Element("parent");
        parent.addContent(root);
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*"));
        int position = 0;

        // First attribute
        NodePointer nodePointer = iterator.next();
        assertEquals(root, nodePointer.getNode().getParent());
        assertEquals("att1", nodePointer.getName().getName());
        assertEquals(null, nodePointer.getName().getPrefix());
        assertEquals("value1", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att1"));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());

        // Next attribute
        nodePointer = iterator.next();
        assertEquals(root, nodePointer.getNode().getParent());
        assertEquals("att2", nodePointer.getName().getName());
        assertEquals(null, nodePointer.getName().getPrefix());
        assertEquals("value2", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att2"));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.hasNext());

        // Next attribute
        nodePointer = iterator.next();
        assertEquals(element, nodePointer.getNode().getParent());
        assertEquals("att1", nodePointer.getName().getName());
        assertEquals("ns", nodePointer.getName().getPrefix());
        assertEquals("value3", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att1", "ns"));
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());

        // Next attribute
        nodePointer = iterator.next();
        assertEquals(element, nodePointer.getNode().getParent());
        assertEquals("att2", nodePointer.getName().getName());
        assertEquals("ns", nodePointer.getName().getPrefix());
        assertEquals("value4", nodePointer.getName().getLocalPart());
        assertEquals(nodePointer.getNode(), nodePointer.getNode().getAttribute("att2", "ns"));
        assertEquals(4, iterator.getPosition());
        assertFalse(iterator.hasNext());

        // Get next, expected end of iteration
        nodePointer = iterator.next();
        assertNull(nodePointer);
        assertEquals(5, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test getPosition.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testGetPosition() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("att1", "value1"));
        root.addAttribute(new Attribute("att2", "value2"));
        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration("ns", new Namespace("ns", "http://test.com"));
        element.addAttribute("att1", "value3", "ns");
        element.addAttribute("att2", "value4", "ns");
        Element parent = new Element("parent");
        parent.addContent(root);
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*"));

        // Test getPosition on first attribute
        NodePointer nodePointer = iterator.next();
        assertEquals(1, iterator.getPosition());

        // Test getPosition on last attribute
        nodePointer = iterator.next();
        assertEquals(4, iterator.getPosition());

        // Test getPosition on end of iteration
        nodePointer = iterator.next();
        assertEquals(5, iterator.getPosition());
    }

    /**
     * Test setPosition.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testSetPosition() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("att1", "value1"));
        root.addAttribute(new Attribute("att2", "value2"));
        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration("ns", new Namespace("ns", "http://test.com"));
        element.addAttribute("att1", "value3", "ns");
        element.addAttribute("att2", "value4", "ns");
        Element parent = new Element("parent");
        parent.addContent(root);
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*"));

        // Test setPosition to the 2nd attribute
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        NodePointer nodePointer = iterator.next();
        assertEquals("att2", nodePointer.getName().getName());

        // Test setPosition to the first attribute
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        nodePointer = iterator.next();
        assertEquals("att1", nodePointer.getName().getName());

        // Test setPosition to the end of the iterator
        assertTrue(iterator.setPosition(6));
        assertEquals(5, iterator.getPosition());
        nodePointer = iterator.next();
        assertNull(nodePointer);

        // Test setPosition to an invalid position
        assertFalse(iterator.setPosition(6));
        assertEquals(5, iterator.getPosition());
    }

    /**
     * Test hasNext.
     *
     * @throws Exception if the test case is failed.
     */
    @org.junit.Test
    public void testHasNext() throws Exception {
        Element root = new Element("root");
        root.addAttribute(new Attribute("att1", "value1"));
        root.addAttribute(new Attribute("att2", "value2"));
        Element element = new Element("element");
        element.addNamespaceDeclaration("xml", Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration("ns", new Namespace("ns", "http://test.com"));
        element.addAttribute("att1", "value3", "ns");
        element.addAttribute("att2", "value4", "ns");
        Element parent = new Element("parent");
        parent.addContent(root);
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*"));

        // Test hasNext on first attribute
        assertTrue(iterator.hasNext());

        // Test hasNext on last attribute
        assertTrue(iterator.hasNext());
        NodePointer nodePointer = iterator.next();
        assertEquals("att2", nodePointer.getName().getName());

        // Test hasNext on end of iteration
        assertFalse(iterator.hasNext());
    }
}