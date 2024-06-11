package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private List<Attribute> attributes;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        parent = new JDOMNodePointer(null, new Element("element"), null);
        attributes = new ArrayList<>();

        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");

        attributes.add(attr1);
        attributes.add(attr2);

        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, attributeIterator.parent);

        assertEquals(attributes, attributeIterator.attributes);

        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.position = 2;
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(0, attributeIterator.getPosition());

        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());

        assertFalse(attributeIterator.setPosition(3));
        assertEquals(1, attributeIterator.getPosition());
    }
}