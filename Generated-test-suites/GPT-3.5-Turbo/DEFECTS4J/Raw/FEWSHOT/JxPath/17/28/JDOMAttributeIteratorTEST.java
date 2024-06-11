package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private Element element;

    @Before
    public void setUp() throws JDOMException {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        element.setAttribute("attr4", "value4");
        parent = new JDOMElementPointer(element, null, new NamespaceResolver());
    }

    @Test
    public void testConstructor() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals(parent, attributeIterator.getParentPointer());

        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr*"));
        assertEquals(parent, attributeIterator.getParentPointer());

        attributeIterator = new JDOMAttributeIterator(parent, new QName("nonExistingAttr"));
        assertEquals(parent, attributeIterator.getParentPointer());
    }

    @Test
    public void testGetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr2"));
        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr*"));

        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());

        assertTrue(attributeIterator.setPosition(3));
        assertEquals(3, attributeIterator.getPosition());

        assertTrue(attributeIterator.setPosition(5));
        assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(parent, nodePointer.getParentPointer());
        assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test(expected = JXPathException.class)
    public void testGetNodePointerInvalidPosition() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr*"));

        attributeIterator.getNodePointer();
    }
}