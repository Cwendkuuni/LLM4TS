package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.equalStrings;
import static org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator.testAttr;

/**
 * Tests for {@link DOMAttributeIterator} class.
 */
public class DOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new DOMPointer("", null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSimpleIterator() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem name=\"a\" attr1=\"value1\" attr2=\"value2\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "name", "*"));

        Assert.assertTrue(iterator.hasNext());
        NodePointer attrPointer = iterator.next();
        Assert.assertEquals("attr1", attrPointer.getName().getLocalPart());
        Assert.assertEquals("value1", attrPointer.getNodeValue().getNodeValue());

        Assert.assertTrue(iterator.hasNext());
        attrPointer = iterator.next();
        Assert.assertEquals("attr2", attrPointer.getName().getLocalPart());
        Assert.assertEquals("value2", attrPointer.getNodeValue().getNodeValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNameFiltering() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem name=\"a\" attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x", "attr2", "*"));

        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(parent, new QName("", "name", "attr3"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer attrPointer = iterator.next();
        Assert.assertEquals("attr3", attrPointer.getName().getLocalPart());
        Assert.assertEquals("value3", attrPointer.getNodeValue().getNodeValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespaceFiltering() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem xmlns:z=\"http://test.com/z\" name=\"a\" attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(null, null, "x:attr2"));

        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(parent, new QName(null, null, "z:attr3"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer attrPointer = iterator.next();
        Assert.assertEquals("attr3", attrPointer.getName().getLocalPart());
        Assert.assertEquals("value3", attrPointer.getNodeValue().getNodeValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithPrefixFiltering() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem xmlns:z=\"http://test.com/z\" name=\"a\" attr1=\"value1\" xmlns:x1=\"http://test.com/x1\">" +
                "attr2=\"value2\" attr3=\"value3\" xmlns:x2=\"http://test.com/x2\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x1", "attr2", "*"));

        Assert.assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(parent, new QName("", null, "xmlns:x1"));
        Assert.assertTrue(iterator.hasNext());
        NodePointer attrPointer = iterator.next();
        Assert.assertEquals("xmlns:x1", attrPointer.getName().getNamespaceURI());
        Assert.assertEquals("attr2", attrPointer.getName().getLocalPart());
        Assert.assertEquals("value2", attrPointer.getNodeValue().getNodeValue());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("", "name", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullName() {
        NodePointer parent = new DOMPointer("", null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
    }

    @Test
    public void testNextAfterLastPosition() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem name=\"a\" attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "name", "*"));

        Assert.assertTrue(iterator.hasNext());
        NodePointer attrPointer = iterator.next();
        Assert.assertEquals("attr1", attrPointer.getName().getLocalPart());

        Assert.assertTrue(iterator.hasNext());
        attrPointer = iterator.next();
        Assert.assertEquals("attr2", attrPointer.getName().getLocalPart());

        Assert.assertTrue(iterator.hasNext());
        attrPointer = iterator.next();
        Assert.assertEquals("attr3", attrPointer.getName().getLocalPart());

        Assert.assertFalse(iterator.hasNext());

        iterator.next(); // position should be 4 now, but we have only 3 attributes
        Assert.fail();
    }

    @Test
    public void testSetPositionAfterLastPosition() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem name=\"a\" attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "name", "*"));

        iterator.next();
        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testSetPositionBeforeFirstPosition() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem name=\"a\" attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "name", "*"));

        Assert.assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testSetPositionBeforeFirstAttribute() {
        String xml = "<root xmlns:x=\"http://test.com/x\" xmlns:y=\"http://test.com/y\">" +
                "<elem name=\"a\" attr1=\"value1\" attr2=\"value2\" attr3=\"value3\"/>" +
                "</root>";

        NodePointer parent = new DOMPointer(xml, null, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "name", "*"));

        Assert.assertTrue(iterator.next().hasNext()); // move to the second attribute

        Assert.assertTrue(iterator.setPosition(0));
        Assert.assertEquals("attr1", iterator.next().getName().getLocalPart());
    }
}