package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
    public void testSimpleAttributeIterator() {
        Element root = new Element("root");
        root.addAttribute("id", "1");
        NodePointer parent = new JDOMPointer(root);
        QName name = new QName("*", "*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals("1", attributePointer.getValue());
        assertNotNull(attributePointer.getNamespaceURI());
        assertNull(attributePointer.getNode());
        assertNull(attributePointer.getParent());

        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testEmptyAttributeIterator() {
        Element root = new Element("root");
        NodePointer parent = new JDOMPointer(root);
        QName name = new QName("*", "*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNameSpaceAttributeIterator() {
        Element root = new Element("root");
        root.addNamespaceDeclaration("xml", "http://www.w3.org/XML/1998/namespace");
        root.addAttribute("xml:id", "1");
        NodePointer parent = new JDOMPointer(root);
        QName name = new QName("xml", "id", "http://www.w3.org/XML/1998/namespace");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals("1", attributePointer.getValue());
        assertNotNull(attributePointer.getNamespaceURI());
        assertEquals("http://www.w3.org/XML/1998/namespace", attributePointer.getNamespaceURI());
        assertNull(attributePointer.getNode());
        assertNull(attributePointer.getParent());

        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element root = new Element("root");
        root.addAttribute("id", "1");
        root.addAttribute("class", "foo");
        NodePointer parent = new JDOMPointer(root);
        QName name = new QName("*", "*", null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        List<NodePointer> attributes = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            assertNotNull(attributePointer);
            if (i == 0) {
                assertEquals("1", attributePointer.getValue());
            } else {
                assertEquals("foo", attributePointer.getValue());
            }
            attributes.add(attributePointer);
            iterator.setPosition(iterator.getPosition() + 1);
        }

        for (NodePointer attribute : attributes) {
            assertNull(attribute.getNode());
            assertNull(attribute.getParent());
        }
    }
}