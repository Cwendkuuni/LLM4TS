
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;
    private Element element;
    private Attribute attribute;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName();
        element = new Element("test");
        attribute = new Attribute("testAttribute", "testValue");
        element.setAttribute(attribute);
        parent.setValue(element);
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        NodePointer nullParent = null;
        QName nullName = null;
        JDOMAttributeIterator nullIterator = new JDOMAttributeIterator(nullParent, nullName);
        assertNull(nullIterator.attributes);
        assertNull(nullIterator.position);

        QName testQName = new QName("testAttribute", null);
        JDOMAttributeIterator customIterator = new JDOMAttributeIterator(parent, testQName);
        assertFalse(customIterator.attributes.isEmpty());
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testGetPosition() {
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
        assertEquals(2, iterator.getPosition());
    }
}
