//Test cases for JDOMAttributeIterator
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {
  
    @Mock
    NodePointer parent;

    JDOMAttributeIterator jdomAttributeIterator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3");
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);
        attributes.add(attribute3);
        element.setAttributes(attributes);
        jdomAttributeIterator = new JDOMAttributeIterator(parent, new QName("attr2"));
    }

    @Test
    public void testGetNodePointer() {
        assertEquals(1, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.setPosition(2);
        assertEquals(2, jdomAttributeIterator.getPosition());
        assertTrue(jdomAttributeIterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals("attr2", ((JDOMAttributePointer) jdomAttributeIterator.getNodePointer()).getLocalName());
        assertEquals("value2", ((JDOMAttributePointer) jdomAttributeIterator.getNodePointer()).getValue());

        // Invalid position
        jdomAttributeIterator.setPosition(4);
        assertNull(jdomAttributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(1, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(jdomAttributeIterator.setPosition(2));
        assertEquals(2, jdomAttributeIterator.getPosition());

        // Invalid positions
        assertFalse(jdomAttributeIterator.setPosition(0));
        assertFalse(jdomAttributeIterator.setPosition(4));
    }

}