package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, attributeIterator.parent);
        assertNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void getNodePointer() {
        JDOMAttributePointer expected = new JDOMAttributePointer(parent, (Attribute) parent.getNode());
        assertEquals(expected, attributeIterator.getNodePointer());
    }

    @Test
    public void getPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void setPosition() {
        assertEquals(false, attributeIterator.setPosition(0));
        assertEquals(1, attributeIterator.getPosition());
        assertEquals(true, attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
        assertEquals(false, attributeIterator.setPosition(3));
    }
}