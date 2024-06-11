package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyElement() {
        Element emptyElement = new Element("empty");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(emptyElement), new QName("", ""));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttribute() {
        String namespaceUri = "http://example.com/ns";
        String attributeName = "attr";
        String attributeValue = "value";

        Element element = new Element("element");
        Attribute attribute = new Attribute(attributeName, attributeValue, new Namespace(namespaceUri));
        element.addContent(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(namespaceUri, attributeName));
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(new JDOMAttributePointer(new JDOMPointer(element), attribute), nodePointer);
        assertEquals(attributeName, nodePointer.getNodeName().getName());
        assertEquals(namespaceUri, nodePointer.getNodeName().getPrefix());
        assertEquals(attributeValue, nodePointer.getNodeName().getValue());
    }

    @Test
    public void testMultipleAttributes() {
        String namespaceUri = "http://example.com/ns";
        String attributeName1 = "attr1";
        String attributeValue1 = "value1";
        String attributeName2 = "attr2";
        String attributeValue2 = "value2";

        Element element = new Element("element");
        Attribute attribute1 = new Attribute(attributeName1, attributeValue1, new Namespace(namespaceUri));
        Attribute attribute2 = new Attribute(attributeName2, attributeValue2, new Namespace(namespaceUri));
        element.addContent(attribute1);
        element.addContent(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(namespaceUri, "*"));

        List<NodePointer> nodePointers = new ArrayList<>();
        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            nodePointers.add(nodePointer);
            iterator.next();
        }

        for (int i = 0; i < nodePointers.size(); i++) {
            NodePointer nodePointer = nodePointers.get(i);
            assertEquals(new JDOMAttributePointer(new JDOMPointer(element),
                    (Attribute) (i % 2 == 0 ? attribute1 : attribute2)), nodePointer);
            assertEquals(i % 2 == 0 ? attributeName1 : attributeName2, nodePointer.getNodeName().getName());
            assertEquals(namespaceUri, nodePointer.getNodeName().getPrefix());
            assertEquals(i % 2 == 0 ? attributeValue1 : attributeValue2, nodePointer.getNodeName().getValue());
        }
    }

    @Test
    public void testIteratorPositioning() {
        String namespaceUri = "http://example.com/ns";
        String attributeName1 = "attr1";
        String attributeValue1 = "value1";
        String attributeName2 = "attr2";
        String attributeValue2 = "value2";

        Element element = new Element("element");
        Attribute attribute1 = new Attribute(attributeName1, attributeValue1, new Namespace(namespaceUri));
        Attribute attribute2 = new Attribute(attributeName2, attributeValue2, new Namespace(namespaceUri));
        element.addContent(attribute1);
        element.addContent(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(namespaceUri, "*"));

        List<NodePointer> nodePointers = new ArrayList<>();
        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            nodePointers.add(nodePointer);
            iterator.next();
        }

        // Test setting position to out of bounds value
        iterator.setPosition(iterator.getSize() + 1);
        assertNull(iterator.getNodePointer());

        // Test setting position to valid, but not current position
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer.equals(nodePointers.get(1)));

        // Test setting position to current position
        iterator.setPosition(0);
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer.equals(nodePointers.get(0)));
    }

    @Test
    public void testIteratorClosedState() {
        String namespaceUri = "http://example.com/ns";
        String attributeName = "attr";

        Element element = new Element("element");
        element.addAttribute(attributeName, "value", new Namespace(namespaceUri));

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMPointer(element), new QName(namespaceUri, attributeName));

        // Test getting node pointer after close
        iterator.close();
        assertNull(iterator.getNodePointer());

        // Test setting position after close
        iterator.setPosition(0);
        assertNull(iterator.getNodePointer());
    }
}