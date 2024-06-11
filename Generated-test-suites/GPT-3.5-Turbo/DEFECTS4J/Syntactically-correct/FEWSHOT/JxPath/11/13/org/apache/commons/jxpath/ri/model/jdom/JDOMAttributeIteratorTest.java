package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;

    @Before
    public void setup() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        name = new QName("", "attr1");
    }

    @Test
    public void testConstructor() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(parent, iterator.parent);
        assertEquals(1, iterator.attributes.size());
        assertTrue(iterator.attributes.get(0) instanceof Attribute);
        Attribute attribute = (Attribute) iterator.attributes.get(0);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        assertEquals(parent, attributePointer.getParent());
        Attribute attribute = attributePointer.getAttribute();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(3));
    }
}