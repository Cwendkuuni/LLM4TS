package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private List<Attribute> attributes;
    private int position;

    public JDOMAttributeIteratorTest() {
        parent = new NodePointer();
        attributes = new ArrayList<>();
        position = 0;
    }

    @Test
    public void testConstructor() {
        QName name = new QName("attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(parent, iterator.getParent());
        assertNull(iterator.getAttributes());

        name = new QName("attribute", "http://www.example.com");
        iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(parent, iterator.getParent());
        assertNull(iterator.getAttributes());
    }

    @Test
    public void testGetNodePointer() {
        Attribute attr = new Attribute("attr", "value");
        attributes.add(attr);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));
        assertEquals(new JDOMAttributePointer(parent, attr), iterator.getNodePointer());

        attributes.clear();
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertEquals(null, iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));
        assertEquals(0, iterator.getPosition());

        position = 2;
        assertEquals(position, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributes.add(new Attribute("attr1", "value1"));
        attributes.add(new Attribute("attr2", "value2"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));

        assertEquals(true, iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertEquals(true, iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertEquals(false, iterator.setPosition(3));
        assertEquals(2, iterator.getPosition());
    }
}