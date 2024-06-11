import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructorWithQName() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        QName qname = new QName("*,xml:attr1");

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qname);

        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithWildcardQName() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*", "*"));

        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testJDOMAttributeIteratorEmptyList() {
        Element element = new Element("root");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorNullParent() {
        NodePointer parent = null;
        QName qname = new QName("", "*");
        new JDOMAttributeIterator(parent, qname);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorNullQName() {
        NodePointer parent = new NodePointer(new Element("root"));
        new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testSetPositionLessThanOne() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(-1);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPositionGreaterThanSize() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(3);
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testSetPositionWithinSize() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(2);
        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertEquals(attribute3, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointerWhenPositionZero() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(0);
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointerWhenPositionOne() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(1);
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointerWhenPositionTwo() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(2);
        Assert.assertEquals(attribute3, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointerWhenPositionGreaterThanSize() {
        Element element = new Element("root");
        Namespace xmlNamespace = Namespace.getNamespace("xml");
        Attribute attribute1 = new Attribute("attr1", "value1", xmlNamespace);
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(4);
        Assert.assertNull(iterator.getNodePointer());
    }
}