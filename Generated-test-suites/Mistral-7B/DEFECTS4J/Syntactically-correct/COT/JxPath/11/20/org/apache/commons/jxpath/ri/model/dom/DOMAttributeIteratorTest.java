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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testConstructorWithWildcardName() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        QName name = new QName("*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testConstructorWithEmptyList() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.next() == null);
    }

    @Test
    public void testConstructorWithAttributes() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute = iterator.next();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getNodeValue());
    }

    @Test
    public void testConstructorWithNamespace() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root xmlns:x='http://test.com' id='root' x:attr1='value1'/>");
        QName name = new QName("{http://test.com}", "attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute = iterator.next();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getNodeValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        QName name = new QName("test");
        new DOMAttributeIterator(null, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        new DOMAttributeIterator(parent, null);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.next();
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        Attr attribute = attributePointer.getAttribute();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getNodeValue());
    }

    @Test
    public void testGetNodePointerNext() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer1 = iterator.next();
        NodePointer nodePointer2 = iterator.next();
        Attr attribute1 = ((DOMAttributePointer) nodePointer1).getAttribute();
        Attr attribute2 = ((DOMAttributePointer) nodePointer2).getAttribute();
        Assert.assertNotNull(attribute1);
        Assert.assertEquals("value1", attribute1.getNodeValue());
        Assert.assertNotNull(attribute2);
        Assert.assertEquals("value2", attribute2.getNodeValue());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        int position = iterator.getPosition();
        Assert.assertEquals(1, position);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.next();
        Attr attribute = ((DOMAttributePointer) nodePointer).getAttribute();
        Assert.assertEquals("value2", attribute.getNodeValue());
    }

    @Test
    public void testSetPositionOutOfRange() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(3);
        NodePointer nodePointer = iterator.next();
        Attr attribute = ((DOMAttributePointer) nodePointer).getAttribute();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value2", attribute.getNodeValue());
        iterator.setPosition(0);
        assertNull(iterator.next());
    }

    @Test
    public void testSetPositionToFirst() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(0);
        NodePointer nodePointer = iterator.next();
        Attr attribute = ((DOMAttributePointer) nodePointer).getAttribute();
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value1", attribute.getNodeValue());
    }

    @Test
    public void testSetPositionToLast() {
        NodePointer parent = new DOMPoint(new DOMElement("root"));
        parent.setNodeValue("<root id='root' attr1='value1' attr2='value2'/>");
        QName name = new QName("attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.next();
        Attr attribute = ((DOMAttributePointer) nodePointer).getAttribute();
        Assert.assertEquals("value2", attribute.getNodeValue());
        iterator.setPosition(attributes.size());
        assertNull(iterator.next());
    }
}