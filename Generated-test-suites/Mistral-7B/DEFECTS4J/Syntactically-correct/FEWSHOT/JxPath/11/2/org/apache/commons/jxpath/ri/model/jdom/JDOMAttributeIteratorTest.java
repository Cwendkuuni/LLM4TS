package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JUnit test cases for JDOMAttributeIterator class.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_empty() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_singleNameSpace() {
        Element element = new Element("root");
        Namespace ns = Namespace.getXmlNamespace();
        element.addAttribute("attribute1", "value1", ns);
        NodePointer parent = new JDOMPointer(element);

        QName attributeName = new QName("attribute1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        assertEquals(1, iterator.getPosition());
        NodePointer attributePointer = iterator.getNodePointer();
        assertEquals("attribute1", attributePointer.getName());
        assertEquals("value1", attributePointer.getValue());

        assertNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_singleNameSpace_multipleAttributes() {
        Element element = new Element("root");
        Namespace ns = Namespace.getXmlNamespace();
        element.addAttribute("attribute1", "value1", ns);
        element.addAttribute("attribute2", "value2", ns);

        NodePointer parent = new JDOMPointer(element);
        QName attributeName = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        assertEquals(2, iterator.getPosition());
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("attribute1", attributePointer1.getName().getName());
        assertEquals("value1", attributePointer1.getValue());

        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals("attribute2", attributePointer2.getName().getName());
        assertEquals("value2", attributePointer2.getValue());

        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_multipleNameSpaces() {
        Element element = new Element("root");
        Namespace xmlNs = Namespace.getXmlNamespace();
        Namespace ns = Namespace.getNamespace("myNs");

        element.addAttribute("xml:attribute1", "value1", xmlNs);
        element.addNamespaceDeclaration("myNs", ns);
        element.addAttribute("myNs:attribute2", "value2", ns);

        NodePointer parent = new JDOMPointer(element);

        QName attributeName1 = new QName("xml", "attribute1");
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(parent, attributeName1);
        assertEquals(1, iterator1.getPosition());
        NodePointer attributePointer1 = iterator1.getNodePointer();
        assertEquals("attribute1", attributePointer1.getName().getName());
        assertEquals("value1", attributePointer1.getValue());

        QName attributeName2 = new QName("myNs", "attribute2");
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(parent, attributeName2);
        assertEquals(2, iterator2.getPosition());
        NodePointer attributePointer2 = iterator2.getNodePointer();
        assertEquals("attribute2", attributePointer2.getName().getName());
        assertEquals("value2", attributePointer2.getValue());

        assertNull(iterator1.getNodePointer());
        assertEquals(1, iterator1.getPosition());

        assertNull(iterator2.getNodePointer());
        assertEquals(2, iterator2.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_moveNext() {
        Element element = new Element("root");
        Namespace ns = Namespace.getXmlNamespace();
        element.addAttribute("attribute1", "value1", ns);
        element.addAttribute("attribute2", "value2", ns);

        NodePointer parent = new JDOMPointer(element);
        QName attributeName = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        NodePointer attributePointer1 = iterator.getNodePointer();
        assertEquals("attribute1", attributePointer1.getName().getName());
        assertEquals("value1", attributePointer1.getValue());

        iterator.next();
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertEquals("attribute2", attributePointer2.getName().getName());
        assertEquals("value2", attributePointer2.getValue());

        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }
}