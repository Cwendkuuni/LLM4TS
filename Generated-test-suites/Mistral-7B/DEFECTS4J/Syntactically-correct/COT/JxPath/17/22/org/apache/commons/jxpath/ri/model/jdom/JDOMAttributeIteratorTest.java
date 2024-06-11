import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeConstants;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JDOMAttributeIterator test cases.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_NullParent() {
        try {
            new JDOMAttributeIterator(null, new QName("", ""));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructor_EmptyAttributeList() {
        NodePointer parent = new NodePointer(new Element("root"), new Namespace("", ""), NodeConstants.ROOT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute = new Attribute("bar", "value", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(parent, iterator.getNodePointer().getParent());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute1 = new Attribute("bar1", "value1", new Namespace("foo", "http://foo.com"));
        Attribute attribute2 = new Attribute("bar2", "value2", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(new Element("child", new QName("foo:child"), new Namespace("foo", "http://foo.com")));
        parent.getChildNodes().add(attribute1);
        parent.getChildNodes().add(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(parent, iterator.getNodePointer().getParent());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertEquals(parent, iterator.getNodePointer().getParent());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructor_NoMatchingAttribute() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "bar"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetNodePointer_NoAttributes() {
        NodePointer parent = new NodePointer(new Element("root"), new Namespace("", ""), NodeConstants.ROOT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointer_SingleAttribute() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute = new Attribute("bar", "value", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Iterator<NodePointer> nodes = iterator;
        Assert.assertTrue(nodes.hasNext());
        NodePointer nodePointer = nodes.next();
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
        Assert.assertEquals(parent, nodePointer.getParent());
        Assert.assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_MultipleAttributes() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute1 = new Attribute("bar1", "value1", new Namespace("foo", "http://foo.com"));
        Attribute attribute2 = new Attribute("bar2", "value2", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(new Element("child", new QName("foo:child"), new Namespace("foo", "http://foo.com")));
        parent.getChildNodes().add(attribute1);
        parent.getChildNodes().add(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Iterator<NodePointer> nodes = iterator;
        Assert.assertTrue(nodes.hasNext());
        NodePointer nodePointer1 = nodes.next();
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer1.getClass());
        Assert.assertEquals(parent, nodePointer1.getParent());
        Assert.assertEquals(attribute1, nodePointer1.getNode());
        Assert.assertTrue(nodes.hasNext());
        NodePointer nodePointer2 = nodes.next();
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer2.getClass());
        Assert.assertEquals(parent, nodePointer2.getParent());
        Assert.assertEquals(attribute2, nodePointer2.getNode());
        Assert.assertFalse(nodes.hasNext());
    }

    @Test
    public void testGetPosition_NoAttributes() {
        NodePointer parent = new NodePointer(new Element("root"), new Namespace("", ""), NodeConstants.ROOT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition_SingleAttribute() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute = new Attribute("bar", "value", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Assert.assertEquals(0, iterator.getPosition());
        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPosition_MultipleAttributes() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute1 = new Attribute("bar1", "value1", new Namespace("foo", "http://foo.com"));
        Attribute attribute2 = new Attribute("bar2", "value2", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(new Element("child", new QName("foo:child"), new Namespace("foo", "http://foo.com")));
        parent.getChildNodes().add(attribute1);
        parent.getChildNodes().add(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Assert.assertEquals(0, iterator.getPosition());
        iterator.next();
        Assert.assertEquals(1, iterator.getPosition());
        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_OutOfBounds() {
        NodePointer parent = new NodePointer(new Element("root"), new Namespace("", ""), NodeConstants.ROOT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        iterator.setPosition(10);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_NullIterator() {
        NodePointer parent = new NodePointer(new Element("root"), new Namespace("", ""), NodeConstants.ROOT_NAME);
        JDOMAttributeIterator iterator = null;
        iterator.setPosition(0);
    }

    @Test
    public void testHasNext_NoAttributes() {
        NodePointer parent = new NodePointer(new Element("root"), new Namespace("", ""), NodeConstants.ROOT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext_SingleAttribute() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute = new Attribute("bar", "value", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext_MultipleAttributes() {
        NodePointer parent = new NodePointer(new Element("root", new QName("foo", ""), new Namespace("foo", "http://foo.com")), new Namespace("foo", "http://foo.com"), NodeConstants.ELEMENT_NAME);
        Attribute attribute1 = new Attribute("bar1", "value1", new Namespace("foo", "http://foo.com"));
        Attribute attribute2 = new Attribute("bar2", "value2", new Namespace("foo", "http://foo.com"));
        parent.getChildNodes().add(new Element("child", new QName("foo:child"), new Namespace("foo", "http://foo.com")));
        parent.getChildNodes().add(attribute1);
        parent.getChildNodes().add(attribute2);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("foo:bar"));
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }
}