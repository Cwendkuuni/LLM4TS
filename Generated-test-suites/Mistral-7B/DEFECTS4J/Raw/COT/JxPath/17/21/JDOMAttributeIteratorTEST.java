import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructorWithQualifiedName() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("xmlns:test", "test", "http://test.com");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        List<Attribute> expectedAttributes = new ArrayList<>();
        Attribute attribute = new Attribute("test", "test", "http://test.com");
        attribute.setNamespace(new Namespace("xmlns:test", "http://test.com"));
        expectedAttributes.add(attribute);

        Assert.assertEquals(expectedAttributes, iterator.attributes);
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithUnqualifiedName() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        List<Attribute> expectedAttributes = new ArrayList<>();
        Attribute attribute = new Attribute("test");

        Element element = new Element("root");
        expectedAttributes.add(attribute);
        element.addContent(attribute);

        Assert.assertEquals(expectedAttributes, iterator.attributes);
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithEmptyList() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertTrue(iterator.attributes.isEmpty());
    }

    @Test
    public void testGetNodePointer_ValidPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attribute attribute1 = new Attribute("test1", "test1");
        Attribute attribute2 = new Attribute("test2", "test2");
        element.addContent(attribute1);
        element.addContent(attribute2);

        iterator.attributes = new ArrayList<>(List.of(attribute1, attribute2));

        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute1);

        NodePointer actualNodePointer = iterator.getNodePointer();

        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testGetNodePointer_InvalidPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attribute attribute1 = new Attribute("test1", "test1");
        Attribute attribute2 = new Attribute("test2", "test2");
        element.addContent(attribute1);
        element.addContent(attribute2);

        iterator.attributes = new ArrayList<>(List.of(attribute1, attribute2));

        boolean isNodePointerNull = iterator.getNodePointer() == null;

        Assert.assertTrue(isNodePointerNull);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_InvalidPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attribute attribute1 = new Attribute("test1", "test1");
        Attribute attribute2 = new Attribute("test2", "test2");
        element.addContent(attribute1);
        element.addContent(attribute2);

        iterator.attributes = new ArrayList<>(List.of(attribute1, attribute2));

        iterator.setPosition(-1);
    }

    @Test
    public void testSetPosition_ValidPosition() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attribute attribute1 = new Attribute("test1", "test1");
        Attribute attribute2 = new Attribute("test2", "test2");
        element.addContent(attribute1);
        element.addContent(attribute2);

        iterator.attributes = new ArrayList<>(List.of(attribute1, attribute2));

        iterator.setPosition(1);

        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute2);

        NodePointer actualNodePointer = iterator.getNodePointer();

        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }
}