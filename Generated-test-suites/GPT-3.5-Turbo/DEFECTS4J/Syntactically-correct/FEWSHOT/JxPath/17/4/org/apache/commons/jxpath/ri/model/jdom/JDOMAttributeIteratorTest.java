package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        parent = new JDOMNodePointer(element, null);
    }

    @Test
    public void testConstructorWithQName() {
        QName name = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNotNull(attributeIterator);
        assertEquals(parent, attributeIterator.getParent());
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testConstructorWithWildcardQName() {
        QName name = new QName("", "*");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNotNull(attributeIterator);
        assertEquals(parent, attributeIterator.getParent());
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testConstructorWithNamespaceQName() {
        Namespace namespace = Namespace.getNamespace("ns", "http://example.com");
        QName name = new QName("ns", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNotNull(attributeIterator);
        assertEquals(parent, attributeIterator.getParent());
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(parent, nodePointer.getParent());
        assertEquals(attributeIterator.getPosition() - 1, ((JDOMAttributePointer) nodePointer).getIndex());
        assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetPosition() {
        QName name = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        int position = attributeIterator.getPosition();

        assertEquals(1, position);
    }

    @Test
    public void testSetPositionWithValidPosition() {
        QName name = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        boolean result = attributeIterator.setPosition(2);

        assertTrue(result);
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        QName name = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        boolean result = attributeIterator.setPosition(0);

        assertFalse(result);
        assertEquals(1, attributeIterator.getPosition());
    }
}