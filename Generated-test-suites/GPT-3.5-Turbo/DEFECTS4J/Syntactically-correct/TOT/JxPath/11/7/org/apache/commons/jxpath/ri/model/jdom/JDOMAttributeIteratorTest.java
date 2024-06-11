import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer_WithValidElementAndNamespace() {
        Element element = new Element("testElement");
        Element childElement = new Element("childElement");
        Namespace ns = Namespace.getNamespace("prefix", "http://example.com");

        Attribute attr1 = new Attribute("name", "value1");
        element.addAttribute(attr1);
        attr1.setNamespace(ns);

        Attribute attr2 = new Attribute("name", "value2", ns);
        childElement.addAttribute(attr2);

        element.addContent(childElement);

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix", "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        assertEquals(attr2, attributePointer.getAttribute());
    }

    @Test
    public void testGetNodePointer_WithValidElementAndNoNamespace() {
        Element element = new Element("testElement");
        Attribute attr1 = new Attribute("name", "value1");
        element.addAttribute(attr1);

        NodePointer parent = new NodePointer(element);
        QName name = new QName(null, "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        assertEquals(attr1, attributePointer.getAttribute());
    }

    @Test
    public void testGetNodePointer_WithInvalidElement() {
        Element element = new Element("testElement");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix", "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_WithInvalidNamespace() {
        Element element = new Element("testElement");
        Attribute attr1 = new Attribute("name", "value1");
        element.addAttribute(attr1);

        NodePointer parent = new NodePointer(element);
        QName name = new QName("invalidPrefix", "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition_WithValidPosition() {
        Element element = new Element("testElement");
        Attribute attr1 = new Attribute("name", "value1");
        Attribute attr2 = new Attribute("name", "value2");
        element.addAttribute(attr1);
        element.addAttribute(attr2);

        NodePointer parent = new NodePointer(element);
        QName name = new QName(null, "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testGetPosition_WithInvalidPosition() {
        Element element = new Element("testElement");
        Attribute attr1 = new Attribute("name", "value1");
        element.addAttribute(attr1);

        NodePointer parent = new NodePointer(element);
        QName name = new QName(null, "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(2));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition_WithEmptyAttributes() {
        Element element = new Element("testElement");
        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix", "name");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(1));
        assertEquals(0, iterator.getPosition());
    }

}