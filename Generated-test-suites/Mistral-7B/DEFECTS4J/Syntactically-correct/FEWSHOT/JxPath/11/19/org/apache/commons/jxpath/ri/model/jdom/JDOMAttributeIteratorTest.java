package org.apache.commons.cli.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMAttributePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSingleAttributeIterator() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value");
        element.addContent(attribute);
        NodePointer parent = new JDOMAttributePointer(new JDOMNodePointer(element), null);
        QName name = new QName("name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertSame(attribute, nodePointer.getNode());
        assertEquals(1, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        Element attribute3 = new Element("name3", "value3");
        element.addContent(attribute1);
        element.addContent(attribute2);
        element.addContent(attribute3);
        NodePointer parent = new JDOMAttributePointer(new JDOMNodePointer(element), null);
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(3, iterator.getPosition());
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertSame(attribute1, nodePointer1.getNode());
        assertEquals(3, iterator.getPosition());
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertSame(attribute2, nodePointer2.getNode());
        assertEquals(3, iterator.getPosition());
        NodePointer nodePointer3 = iterator.getNodePointer();
        assertNotNull(nodePointer3);
        assertSame(attribute3, nodePointer3.getNode());
        assertEquals(3, iterator.getPosition());
        assertNull(iterator.getNodePointer());
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testIteratorWithNullParent() {
        NodePointer parent = new JDOMAttributePointer(null, null);
        QName name = new QName("name");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullName() {
        NodePointer parent = new JDOMAttributePointer(new JDOMNodePointer(new Element("root")), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParentAndName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, null);
    }
}