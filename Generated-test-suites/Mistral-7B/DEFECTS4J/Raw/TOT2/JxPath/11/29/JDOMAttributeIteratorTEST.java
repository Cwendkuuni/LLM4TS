// Your code here

package org.junit.tests.junit4.junit4testcases.junit4testcasesforjunit4classes;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() throws Exception {
        Element element = new Element("root");

        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        assertNotNull(iterator);

        // Test empty attribute list
        Element emptyElement = new Element("root");
        JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(new NodePointer(emptyElement), name);
        assertTrue(emptyIterator.attributes.isEmpty());
    }

    @Test
    public void testGetNodePointer() throws Exception {
        Element element = new Element("root");
        element.addAttribute("test:attr1", "value1");
        element.addAttribute("test:attr2", "value2");

        QName name = new QName("test:attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);

        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("test:attr1", ((JDOMAttributePointer) nodePointer).getName().getLocalPart());
        assertEquals("value1", ((JDOMAttributePointer) nodePointer).getValue().getValue());

        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("test:attr2", ((JDOMAttributePointer) nodePointer).getName().getLocalPart());
        assertEquals("value2", ((JDOMAttributePointer) nodePointer).getValue().getValue());

        // Test end of iteration
        iterator.setPosition(iterator.getPosition() + 2);
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointerWithEmptyAttributeList() throws Exception {
        Element element = new Element("root");

        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);

        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointerWithNullElement() throws Exception {
        QName name = new QName("test:attribute");
        NodePointer nodePointer = new NodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        NodePointer node = iterator.getNodePointer();
        assertNull(node);
    }

    @Test
    public void testPosition() throws Exception {
        Element element = new Element("root");
        element.addAttribute("test:attr1", "value1");
        element.addAttribute("test:attr2", "value2");

        QName name = new QName("test:attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);

        int initialPosition = iterator.getPosition();
        assertEquals(0, initialPosition);

        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("test:attr1", nodePointer.getName().getLocalPart());
        iterator.setPosition(2);

        nodePointer = iterator.getNodePointer();
        assertEquals("test:attr2", nodePointer.getName().getLocalPart());

        assertEquals(1, iterator.getPosition());

        iterator.setPosition(0);
        nodePointer = iterator.getNodePointer();
        assertEquals("test:attr1", nodePointer.getName().getLocalPart());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testPositionWithEmptyAttributeList() throws Exception {
        Element element = new Element("root");

        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);

        int initialPosition = iterator.getPosition();
        assertEquals(0, initialPosition);

        iterator.setPosition(1);

        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testPositionWithNullElement() throws Exception {
        QName name = new QName("test:attribute");
        NodePointer nodePointer = new NodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);

        int initialPosition = iterator.getPosition();
        assertEquals(0, initialPosition);

        iterator.setPosition(1);

        int position = iterator.getPosition();
        assertEquals(0, position);
    }
}