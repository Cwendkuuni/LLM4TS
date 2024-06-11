import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructorWithNamespace() {
        Element element = new Element("root", "prefix:element");
        Namespace namespace = new Namespace("prefix", "prefix-uri");
        Attribute attribute = new Attribute("attribute", "attribute-value", namespace);
        element.addAttribute(attribute);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix:attribute"));

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute, ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute());

        Assert.assertFalse(iterator.setPosition(2));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithNoNamespace() {
        Element element = new Element("root", "element");
        Attribute attribute = new Attribute("attribute", "attribute-value");
        element.addAttribute(attribute);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute, ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute());

        Assert.assertFalse(iterator.setPosition(2));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithWildcard() {
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute3", "value3");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));

        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute1, ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute());

        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(attribute2, iterator.getNodePointer().getAttribute());

        Assert.assertFalse(iterator.setPosition(4));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorConstructorWithNullParent() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorConstructorWithNullQName() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointer() {
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute3", "value3");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertEquals(attribute1, nodePointer.getAttribute());

        iterator.setPosition(1);
        nodePointer = iterator.getNodePointer();
        Assert.assertEquals(attribute2, nodePointer.getAttribute());

        iterator.setPosition(4);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorSetPosition() {
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute3", "value3");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));

        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(attribute1, iterator.getNodePointer().getAttribute());

        Assert.assertTrue(iterator.setPosition(3));
        Assert.assertEquals(attribute3, iterator.getNodePointer().getAttribute());

        Assert.assertFalse(iterator.setPosition(5));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorSetPositionWithNegativePosition() {
        Element element = new Element("root", null);
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute3", "value3");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));

        iterator.setPosition(-1);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorWithEmptyList() {
        Element element = new Element("root", null);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute"));

        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }
}