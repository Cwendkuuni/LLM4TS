
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute(new Attribute("attr2", "value2", Namespace.XML_NAMESPACE));
        element.setAttribute("attr3", "value3", Namespace.NO_NAMESPACE);
        parent = new JDOMNodePointer(element);
    }

    @Test
    public void testGetPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals(false, iterator.setPosition(-1));
        assertEquals(false, iterator.setPosition(0));
        assertEquals(true, iterator.setPosition(1));
        assertEquals(false, iterator.setPosition(2));
    }

    @Test
    public void testGetNodePointer() {
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr1", pointer.getAttribute().getName());

        iterator.setPosition(1);
        pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr1", pointer.getAttribute().getName());

        iterator.setPosition(2);
        pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr2", pointer.getAttribute().getName());

        iterator.setPosition(3);
        pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr3", pointer.getAttribute().getName());
    }
}
