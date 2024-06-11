package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private Element elementWithAttributes;
    private Element elementWithoutAttributes;
    private JDOMAttributeIterator attributeIteratorWithAttributes;
    private JDOMAttributeIterator attributeIteratorWithoutAttributes;

    @Before
    public void setUp() {
        Namespace namespace = Namespace.getNamespace("testNamespace", "testURI");

        // Element with attributes
        elementWithAttributes = new Element("element", namespace);
        elementWithAttributes.setAttribute(new Attribute("attr1", "value1"));
        elementWithAttributes.setAttribute(new Attribute("attr2", "value2"));

        // Element without attributes
        elementWithoutAttributes = new Element("element", namespace);

        // Attribute iterators
        attributeIteratorWithAttributes = new JDOMAttributeIterator(new JDOMNodePointer(elementWithAttributes), new QName("*"));
        attributeIteratorWithoutAttributes = new JDOMAttributeIterator(new JDOMNodePointer(elementWithoutAttributes), new QName("*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIteratorWithAttributes.getNodePointer();
        assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetNodePointerWithEmptyIterator() {
        NodePointer nodePointer = attributeIteratorWithoutAttributes.getNodePointer();
        assertFalse(nodePointer != null);
     }

    @Test
    public void testGetPosition() {
        attributeIteratorWithAttributes.setPosition(1);
        assertEquals(1, attributeIteratorWithAttributes.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertEquals(true, attributeIteratorWithAttributes.setPosition(1));
        assertEquals(false, attributeIteratorWithAttributes.setPosition(3));
    }

    @Test(expected = JXPathException.class)
    public void testSetPositionWithInvalidPosition() {
        attributeIteratorWithAttributes.setPosition(-1);
    }
}