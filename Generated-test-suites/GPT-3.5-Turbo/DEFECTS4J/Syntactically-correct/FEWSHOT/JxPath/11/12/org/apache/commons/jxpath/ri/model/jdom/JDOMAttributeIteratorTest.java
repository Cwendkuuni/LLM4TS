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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private static final Namespace NS = Namespace.NO_NAMESPACE;

    private JDOMAttributeIterator jdomAttributeIterator;

    private Element element;

    @Before
    public void setup() {
        element = new Element("elementName");
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute3", "value3");

        element.setAttributes(Arrays.asList(attribute1, attribute2, attribute3));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parentPointer = new JDOMElementPointer(null, element);
        QName name = new QName("", "attribute2");
        jdomAttributeIterator = new JDOMAttributeIterator(parentPointer, name);

        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        Attribute expectedAttribute = element.getAttribute("attribute2", NS);

        assertEquals(expectedAttribute, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        NodePointer parentPointer = new JDOMElementPointer(null, element);
        QName name = new QName("", "attribute2");
        jdomAttributeIterator = new JDOMAttributeIterator(parentPointer, name);

        int position = jdomAttributeIterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPositionValidPosition() {
        NodePointer parentPointer = new JDOMElementPointer(null, element);
        QName name = new QName("", "attribute2");
        jdomAttributeIterator = new JDOMAttributeIterator(parentPointer, name);

        boolean setPosition = jdomAttributeIterator.setPosition(1);

        assertEquals(true, setPosition);
        assertEquals(1, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPositionInvalidPosition() {
        NodePointer parentPointer = new JDOMElementPointer(null, element);
        QName name = new QName("", "attribute2");
        jdomAttributeIterator = new JDOMAttributeIterator(parentPointer, name);

        boolean setPosition = jdomAttributeIterator.setPosition(5);

        assertEquals(false, setPosition);
        assertEquals(0, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testEmptyAttributeIterator() {
        // Attributes is null
        NodePointer parentPointer = new JDOMElementPointer(null, new Element("emptyElement"));
        QName name = new QName("", "attribute");
        jdomAttributeIterator = new JDOMAttributeIterator(parentPointer, name);

        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();

        assertNull(nodePointer);
    }
}