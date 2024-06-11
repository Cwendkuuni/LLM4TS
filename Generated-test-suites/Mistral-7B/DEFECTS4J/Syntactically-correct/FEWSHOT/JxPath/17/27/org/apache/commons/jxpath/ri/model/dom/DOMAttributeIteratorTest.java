package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.NodeIterator;
import org.apache.commons.jxpath.ri.model.dom.NodePointerImpl;
import org.apache.commons.jxpath.ri.model.dom.domlite.DOMMutableNodes;
import org.apache.commons.jxpath.ri.model.dom.domlite.DOMNodeFactory;
import org.apache.commons.jxpath.ri.model.dom.domlite.DOMNodeWrapper;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    private NodePointer parentNode;
    private DOMAttributeIterator attributeIterator;
    private DOMMutableNodes nodes;
    private Node rootNode;

    @Before
    public void setUp() {
        nodes = new DOMMutableNodes();
        rootNode = nodes.createDocument(null, "root", null);
        Element rootElement = (Element) rootNode;
        parentNode = new NodePointerImpl(rootElement);
    }

    @Test
    public void testNewIterationOnEmptyNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, null);
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNewIterationOnElementNodeWithNoAttributes() {
        Element rootElement = (Element) rootNode;
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), null);
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNewIterationOnElementNodeWithSingleAttribute() {
        String attributeName = "attr";
        String attributeValue = "value";
        Element rootElement = (Element) rootNode;
        Attr attribute = nodes.createAttribute(attributeName);
        attribute.setValue(attributeValue);
        rootElement.setAttributeNode(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), new QName(null, attributeName));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(attribute, attributePointer.getNode());
        assertTrue(iterator.hasNext());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNewIterationOnElementNodeWithMultipleAttributes() {
        String attribute1Name = "attr1";
        String attribute1Value = "value1";
        String attribute2Name = "attr2";
        String attribute2Value = "value2";

        Element rootElement = (Element) rootNode;
        Attr attribute1 = nodes.createAttribute(attribute1Name);
        attribute1.setValue(attribute1Value);
        rootElement.setAttributeNode(attribute1);

        Attr attribute2 = nodes.createAttribute(attribute2Name);
        attribute2.setValue(attribute2Value);
        rootElement.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), null);
        int expectedSize = 2;
        assertTrue(iterator.hasNext());

        for (int i = 0; i < expectedSize; i++) {
            DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
            Attr attribute = (Attr) attributePointer.getNode();
            if (i == 0) {
                assertSame(attribute1, attribute);
            } else {
                assertSame(attribute2, attribute);
            }
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationWithNullQName() {
        Element rootElement = (Element) rootNode;
        Attr attribute = nodes.createAttribute("attr");
        attribute.setValue("value");
        rootElement.setAttributeNode(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), null);
        assertTrue(iterator.hasNext());

        for (int i = 0; i < 2; i++) {
            DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
            Attr attribute = (Attr) attributePointer.getNode();
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationWithQNameThatMatchesNoAttributes() {
        Element rootElement = (Element) rootNode;
        Attr attribute = nodes.createAttribute("nonExistingAttr");
        rootElement.setAttributeNode(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), new QName("test", "nonExistingAttr"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationWithQNameThatMatchesMultipleAttributes() {
        String attributeName = "attr";
        String attributeValue1 = "value1";
        String attributeValue2 = "value2";

        Element rootElement = (Element) rootNode;
        Attr attribute1 = nodes.createAttribute(attributeName);
        attribute1.setValue(attributeValue1);
        rootElement.setAttributeNode(attribute1);

        Attr attribute2 = nodes.createAttribute(attributeName);
        attribute2.setValue(attributeValue2);
        rootElement.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), new QName(null, attributeName));
        int expectedSize = 2;
        assertTrue(iterator.hasNext());

        for (int i = 0; i < expectedSize; i++) {
            DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
            Attr attribute = (Attr) attributePointer.getNode();
            iterator.next();
        }

        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationWithQNameThatMatchesSingleAttribute() {
        String attributeName = "attr";
        String attributeValue = "value";

        Element rootElement = (Element) rootNode;
        Attr attribute = nodes.createAttribute(attributeName);
        attribute.setValue(attributeValue);
        rootElement.setAttributeNode(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), new QName(null, attributeName));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(attribute, attributePointer.getNode());
        assertTrue(iterator.hasNext());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationWithQNameThatMatchesSingleAttributeWithPrefix() {
        String prefix = "prefix";
        String localName = "attr";
        String attributeValue = "value";

        Element rootElement = (Element) rootNode;
        Attr attribute = nodes.createAttribute(prefix + ": " + localName);
        attribute.setValue(attributeValue);
        rootElement.setAttributeNode(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), new QName(prefix, localName));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(attribute, attributePointer.getNode());
        assertTrue(iterator.hasNext());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterationWithQNameThatMatchesSingleAttributeWithNamespaceURI() {
        String namespaceURI = "http://test.com/";
        String localName = "attr";
        String attributeValue = "value";

        Element rootElement = (Element) rootNode;
        Attr attribute = nodes.createAttributeNS(namespaceURI, localName);
        attribute.setValue(attributeValue);
        rootElement.setAttributeNodeNS(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), new QName(namespaceURI, localName));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(attribute, attributePointer.getNode());
        assertTrue(iterator.hasNext());

        iterator.next();
        assertNull(iterator.getNodePointer());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetPosition() {
        Element rootElement = (Element) rootNode;
        Attr attribute1 = nodes.createAttribute("attr1");
        rootElement.setAttributeNode(attribute1);
        Attr attribute2 = nodes.createAttribute("attr2");
        rootElement.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), null);
        iterator.next();
        iterator.next();

        assertTrue(iterator.getPosition() == 2);
    }

    @Test
    public void testSetPosition() {
        Element rootElement = (Element) rootNode;
        Attr attribute1 = nodes.createAttribute("attr1");
        rootElement.setAttributeNode(attribute1);
        Attr attribute2 = nodes.createAttribute("attr2");
        rootElement.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointerImpl(rootElement), null);
        iterator.next();
        iterator.next();

        assertTrue(iterator.setPosition(1));
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertSame(attribute1, attributePointer.getNode());

        assertFalse(iterator.setPosition(3));
    }
}