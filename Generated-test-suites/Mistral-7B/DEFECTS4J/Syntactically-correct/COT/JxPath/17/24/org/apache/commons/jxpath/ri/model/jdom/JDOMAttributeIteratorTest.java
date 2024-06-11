import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructorWithNameAndNamespace() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        Assert.assertEquals(attributes.size(), iterator.size());
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            NodePointer nodePointer = iterator.getNodePointer();
            Assert.assertEquals(attribute, nodePointer.getNode());
            iterator.next();
        }
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructorWithName() {
        Element element = new Element("root");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        Assert.assertEquals(1, iterator.size());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
        iterator.next();
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructorWithNullName() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(0, iterator.size());
    }

    @Test
    public void testConstructorWithEmptyName() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(0, iterator.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        NodePointer parent = null;

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullQName() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testGetNodePointerWithValidPosition() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
        iterator.next();
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointerWithInvalidPosition() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        iterator.next();
        iterator.next();

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPositionWithValidPosition() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        iterator.next();

        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPositionWithInvalidPosition() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        iterator.next();
        iterator.next();
        iterator.next();

        Assert.assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        iterator.next();
        iterator.next();

        Assert.assertTrue(iterator.setPosition(3));
        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.setPosition(3));
        Assert.assertEquals(-1, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionWithPositionGreaterThanSize() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", prefix));

        iterator.next();

        Assert.assertFalse(iterator.setPosition(3));
        Assert.assertEquals(-1, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionWithPositionZero() {
        Element element = new Element("root");
        String prefix = "xml";
        Namespace namespace = Namespace.getNamespace(prefix, "http://example.com/namespace");
        List attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attr1", "value1", namespace);
        Attribute attribute2 = new Attribute("attr2", "value2", namespace);
        attributes.add(attribute1);
        attributes.add(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttribeIterator iterator = new JDOMAttribeIterator(parent, new QName("*", prefix));

        iterator.setPosition(0);

        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
    }
}