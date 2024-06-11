package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testConstructorNullParent() {
        Assert.assertNull(new DOMAttributeIterator(null, new QName("test", "test")));
    }

    @Test
    public void testConstructorNullName() {
        NodePointer parent = new DOMPointer("test", null);
        Assert.assertNull(new DOMAttributeIterator(parent, null));
    }

    @Test
    public void testConstructorNullNode() {
        NodePointer parent = new DOMPointer("test", null);
        Assert.assertNull(new DOMAttributeIterator(parent, new QName("test", "test")));
    }

    @Test
    public void testConstructorEmptyElement() {
        NodePointer parent = new DOMPointer("test", null);
        Element element = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "test"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithAttributes() {
        NodePointer parent = new DOMPointer("test", null);
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "*"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertTrue(next instanceof DOMAttributePointer);
        Assert.assertEquals("attr1", next.getName().getName());
        Assert.assertEquals("value1", next.getNodeValue());
        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertEquals("attr2", next.getName().getName());
        Assert.assertEquals("value2", next.getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithName() {
        NodePointer parent = new DOMPointer("test", null);
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "attr1"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        Assert.assertEquals("attr1", next.getName().getName());
        Assert.assertEquals("value1", next.getNodeValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPositionNegative() {
        NodePointer parent = new DOMPointer("test", null);
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "*"));
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPositionBiggerThanSize() {
        NodePointer parent = new DOMPointer("test", null);
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "*"));
        iterator.setPosition(3);
    }

    @Test
    public void testGetPositionAndGetNodePointer() {
        NodePointer parent = new DOMPointer("test", null);
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "*"));
        iterator.setPosition(1);
        NodePointer next = iterator.next();
        Assert.assertEquals("attr1", next.getName().getName());
        Assert.assertEquals("value1", next.getNodeValue());
        Assert.assertEquals(1, iterator.getPosition());
        next = iterator.next();
        Assert.assertEquals("attr2", next.getName().getName());
        Assert.assertEquals("value2", next.getNodeValue());
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }
}