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
    public void testConstructorWithQualifiedName() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", "prefix1:namespace1");
        attribute1.setNamespace(new Namespace("prefix1", "namespace1"));
        root.addAttribute(attribute1);

        Element childElement = new Element("child");
        Attribute attribute2 = new Attribute("attr2", "value2", null);
        childElement.addAttribute(attribute2);
        root.addContent(childElement);

        NodePointer parentPointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("prefix1:namespace1", "attr1"));

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.getNodePointer().getNode() instanceof Attribute);
        Assert.assertEquals("attr1", ((Attribute) iterator.getNodePointer().getNode()).getName());
        Assert.assertEquals("value1", ((Attribute) iterator.getNodePointer().getNode()).getValue());
    }

    @Test
    public void testConstructorWithWildcardName() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", null);
        root.addAttribute(attribute1);

        Attribute attribute2 = new Attribute("attr2", "value2", "prefix2:namespace2");
        root.addAttribute(attribute2);

        NodePointer parentPointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("*", ""));

        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertTrue(iterator.getNodePointer().getNode() instanceof Attribute);
        Assert.assertEquals("attr1", ((Attribute) iterator.getNodePointer().getNode()).getName());
        Assert.assertEquals("value1", ((Attribute) iterator.getNodePointer().getNode()).getValue());

        iterator.setPosition(1);
        Assert.assertTrue(iterator.getNodePointer().getNode() instanceof Attribute);
        Assert.assertEquals("attr2", ((Attribute) iterator.getNodePointer().getNode()).getName());
        Assert.assertEquals("value2", ((Attribute) iterator.getNodePointer().getNode()).getValue());
    }

    @Test
    public void testEmptyConstructor() {
        Element root = new Element("root");
        NodePointer parentPointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
        Assert.assertTrue(iterator.attributes == Collections.EMPTY_LIST);
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullParentPointer() {
        NodePointer parentPointer = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullQName() {
        NodePointer parentPointer = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, null);
    }

    @Test
    public void testSetPositionWithValidIndex() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", null);
        Attribute attribute2 = new Attribute("attr2", "value2", null);
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);

        NodePointer parentPointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));

        iterator.setPosition(1);
        Assert.assertEquals("attr1", ((Attribute) iterator.getNodePointer().getNode()).getName());

        iterator.setPosition(2);
        Assert.assertEquals("attr2", ((Attribute) iterator.getNodePointer().getNode()).getName());
    }

    @Test
    public void testSetPositionWithOutOfBoundsIndex() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", null);
        Attribute attribute2 = new Attribute("attr2", "value2", null);
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);

        NodePointer parentPointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));

        iterator.setPosition(10);
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertEquals("attr2", ((Attribute) iterator.getNodePointer().getNode()).getName());
    }

    @Test
    public void testSetPositionWithInvalidIndex() {
        Element root = new Element("root");
        Attribute attribute1 = new Attribute("attr1", "value1", null);
        Attribute attribute2 = new Attribute("attr2", "value2", null);
        root.addAttribute(attribute1);
        root.addAttribute(attribute2);

        NodePointer parentPointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", ""));

        iterator.setPosition(-1);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals("attr1", ((Attribute) iterator.getNodePointer().getNode()).getName());
    }
}