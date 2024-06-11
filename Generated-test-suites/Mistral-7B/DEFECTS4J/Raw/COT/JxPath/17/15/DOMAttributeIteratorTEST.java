package org.apache.commons.jxpath.ri.model.dom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithElement() {
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithWildcard() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        QName name = new QName("*", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testConstructorWithMatchingAttribute() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        Assert.assertNotNull(iterator);
        Attr attribute = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals(name.getName(), attribute.getName());
        Assert.assertEquals(name.getPrefix(), attribute.getPrefix());
        Assert.assertEquals(parentPointer.getNode().getNodeValue(), attribute.getOwnerElement().getNodeValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, null);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attribute = (Attr) nodePointer.getNode();
        Assert.assertEquals(name.getName(), attribute.getName());
        Assert.assertEquals(name.getPrefix(), attribute.getPrefix());
        Assert.assertEquals(parentPointer.getNode().getNodeValue(), attribute.getOwnerElement().getNodeValue());
    }

    @Test
    public void testGetNodePointerWithWildcard() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        QName name = new QName("*", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attribute = (Attr) nodePointer.getNode();
        Assert.assertNotNull(attribute);
    }

    @Test
    public void testGetNodePointerNext() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer1);
        iterator.next();
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer2);
        Assert.assertEquals(nodePointer1.getNode(), nodePointer2.getNode());
    }

    @Test
    public void testGetNodePointerNextDifferentAttributes() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name1 = new QName("test1", null, null);
        QName name2 = new QName("test2", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name1);
        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer1);
        iterator.next();
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer2);
        Assert.assertNotSame(nodePointer1.getNode(), nodePointer2.getNode());
        iterator.next();
        NodePointer nodePointer3 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer3);
        Assert.assertNotSame(nodePointer1.getNode(), nodePointer3.getNode());
        Assert.assertNotSame(nodePointer2.getNode(), nodePointer3.getNode());
    }

    @Test
    public void testGetNodePointerPrevious() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        NodePointer nodePointer1 = iterator.getNodePointer();
        iterator.next();
        NodePointer nodePointer2 = iterator.getNodePointer();
        iterator.previous();
        NodePointer nodePointer3 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer3);
        Assert.assertEquals(nodePointer2.getNode(), nodePointer3.getNode());
    }

    @Test
    public void testGetNodePointerPreviousFirst() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        NodePointer nodePointer = iterator.getNodePointer();
        iterator.previous();
        NodePointer nodePointerPrev = iterator.getNodePointer();
        Assert.assertNull(nodePointerPrev);
    }

    @Test
    public void testGetNodePointerPreviousLast() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        NodePointer nodePointerLast = iterator.getNodePointer();
        iterator.next();
        NodePointer nodePointerPrev = iterator.getNodePointer();
        iterator.next();
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        iterator.previous();
        NodePointer nodePointerPrev2 = iterator.getNodePointer();
        Assert.assertEquals(nodePointerLast.getNode(), nodePointerPrev2.getNode());
    }

    @Test
    public void testGetPosition() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        int position = iterator.getPosition();
        iterator.next();
        position = iterator.getPosition();
        iterator.next();
        position = iterator.getPosition();
        iterator.previous();
        Assert.assertEquals(position, 2);
        iterator.previous();
        Assert.assertEquals(position, 1);
        iterator.previous();
        Assert.assertEquals(position, 0);
    }

    @Test
    public void testSetPosition() {
        NodePointer parentPointer = new DOMPointNodeTest().getNodePointer();
        Element element = new DOMElementTest().createTestElement();
        QName name = new QName("test", null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMElementPointer(element), name);
        int position = iterator.getPosition();
        Assert.assertEquals(position, 0);
        iterator.setPosition(1);
        position = iterator.getPosition();
        Assert.assertEquals(position, 1);
        iterator.setPosition(2);
        position = iterator.getPosition();
        Assert.assertEquals(position, 1); // The iterator should not allow position > size()

        // Test with a negative position
        iterator.setPosition(-1);
        position = iterator.getPosition();
        Assert.assertEquals(position, 0);
    }
}