import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor_EmptyList() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assert.assertEquals(Collections.EMPTY_LIST, iterator.getAttributes());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");
        Attribute attribute = new Attribute("test", "value", null);
        parent.getNode().addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        List<Attribute> attributes = iterator.getAttributes();
        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals(attribute, attributes.get(0));
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");

        Attribute attribute1 = new Attribute("test1", "value1", null);
        Attribute attribute2 = new Attribute("test2", "value2", null);
        parent.getNode().addAttribute(attribute1);
        parent.getNode().addAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        List<Attribute> attributes = iterator.getAttributes();
        Assert.assertEquals(2, attributes.size());
        Assert.assertNotSame(attribute1, attributes.get(1));
        Assert.assertNotSame(attribute2, attributes.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullParent() {
        NodePointer parent = null;
        QName attributeName = new QName("test");
        new JDOMAttributeIterator(parent, attributeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullName() {
        NodePointer parent = new NodePointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testGetNodePointer_FirstAttribute() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");
        Attribute attribute = new Attribute("test", "value", null);
        parent.getNode().addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_LastAttribute() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");

        Attribute attribute1 = new Attribute("test1", "value1", null);
        Attribute attribute2 = new Attribute("test2", "value2", null);
        parent.getNode().addAttribute(attribute2);
        parent.getNode().addAttribute(attribute1);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.setPosition(iterator.getPosition() + iterator.getAttributes().size() - 1);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute1, nodePointer.getNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNodePointer_EndOfList() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");
        Attribute attribute = new Attribute("test", "value", null);
        parent.getNode().addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.setPosition(iterator.getAttributes().size());
        iterator.getNodePointer();
    }

    @Test
    public void testGetPosition_Start() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");
        Attribute attribute = new Attribute("test", "value", null);
        parent.getNode().addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        int position = iterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testGetPosition_End() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");

        Attribute attribute1 = new Attribute("test1", "value1", null);
        Attribute attribute2 = new Attribute("test2", "value2", null);
        parent.getNode().addAttribute(attribute1);
        parent.getNode().addAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.setPosition(iterator.getAttributes().size());
        int position = iterator.getPosition();
        Assert.assertEquals(iterator.getAttributes().size(), position);
    }

    @Test
    public void testSetPosition_Start() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");
        Attribute attribute = new Attribute("test", "value", null);
        parent.getNode().addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.setPosition(0);
        boolean set = iterator.setPosition(1);
        Assert.assertTrue(set);
        int position = iterator.getPosition();
        Assert.assertEquals(1, position);
    }

    @Test
    public void testSetPosition_End() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");

        Attribute attribute1 = new Attribute("test1", "value1", null);
        Attribute attribute2 = new Attribute("test2", "value2", null);
        parent.getNode().addAttribute(attribute1);
        parent.getNode().addAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.setPosition(iterator.getAttributes().size());
        boolean set = iterator.setPosition(iterator.getAttributes().size());
        Assert.assertFalse(set);
        int position = iterator.getPosition();
        Assert.assertEquals(iterator.getAttributes().size(), position);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_InvalidPosition() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test");
        Attribute attribute = new Attribute("test", "value", null);
        parent.getNode().addAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.setPosition(-1);
    }
}