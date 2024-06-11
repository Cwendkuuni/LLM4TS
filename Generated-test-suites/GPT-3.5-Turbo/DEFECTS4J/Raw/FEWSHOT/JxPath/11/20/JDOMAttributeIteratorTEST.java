
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Element element;

    @Before
    public void setUp() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        JXPathContext context = JXPathContext.newContext(element);
        NodePointer parent = context.getPointer("/test");
        QName name = new QName(null, "attr2");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attr2", ((Attribute) nodePointer.getNode()).getName());
        assertEquals("value2", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        NodePointer parent = context.getPointer("/test");
        QName name = new QName(null, "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JXPathContext context = JXPathContext.newContext(element);
        NodePointer parent = context.getPointer("/test");
        QName name = new QName(null, "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());

        assertTrue(!iterator.setPosition(4));
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testIterateAllAttributes() {
        JXPathContext context = JXPathContext.newContext(element);
        NodePointer parent = context.getPointer("/test");
        QName name = new QName(null, "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("attr1");
        attributeNames.add("attr2");
        attributeNames.add("attr3");

        int count = 1;

        while (iterator.setPosition(count)) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertTrue(nodePointer instanceof JDOMAttributePointer);

            Attribute attribute = (Attribute) nodePointer.getNode();
            assertTrue(attributeNames.contains(attribute.getName()));

            count++;
        }

        assertEquals(attributeNames.size(), count - 1);
    }

    @Test
    public void testIterateSpecificAttribute() {
        JXPathContext context = JXPathContext.newContext(element);
        NodePointer parent = context.getPointer("/test");
        QName name = new QName(null, "attr2");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertTrue(iterator.setPosition(1));
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        Attribute attribute = (Attribute) nodePointer.getNode();
        assertEquals("attr2", attribute.getName());
        assertEquals("value2", attribute.getValue());

        assertTrue(!iterator.setPosition(2));
        assertTrue(!iterator.setPosition(3));
    }

    @Test
    public void testIterateNoAttributes() {
        Element emptyElement = new Element("emptyTest");

        JXPathContext context = JXPathContext.newContext(emptyElement);
        NodePointer parent = context.getPointer("/emptyTest");
        QName name = new QName(null, "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertTrue(!iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
}
