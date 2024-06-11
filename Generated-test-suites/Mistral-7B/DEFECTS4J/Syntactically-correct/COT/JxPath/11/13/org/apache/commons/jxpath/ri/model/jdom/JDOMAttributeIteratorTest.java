import org.apache.commons.jxpath.ri.NodeIterator;
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
    public void testConstructor_EmptyElement() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(new Element("")), new QName(""));
        Assert.assertNotNull(iterator);
        Assert.assertEquals(Collections.EMPTY_LIST, iterator.attributes);
    }

    @Test
    public void testConstructor_ElementWithSingleAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        String qNameString = xmlns + prefix + ":" + localName;
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "testValue");
        attribute.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        Assert.assertNotNull(iterator);
        Assert.assertEquals(1, iterator.attributes.size());
        Assert.assertEquals(attribute, iterator.attributes.get(0));
    }

    @Test
    public void testConstructor_ElementWithMultipleAttributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        String qNameString = xmlns + prefix + ":" + localName;
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);
        Attribute attribute2 = new Attribute("testAttribute2", "testValue2");
        attribute2.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        Assert.assertNotNull(iterator);
        Assert.assertEquals(2, iterator.attributes.size());
        Assert.assertEquals(attribute1, iterator.attributes.get(0));
        Assert.assertEquals(attribute2, iterator.attributes.get(1));
    }

    @Test
    public void testConstructor_ElementWithNoMatchingAttributes() {
        String prefix = "xml";
        String localName = "test";
        String qNameString = prefix + ":" + localName;
        QName qName = new QName(null, localName);

        Element element = new Element("testElement");

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        Assert.assertNull(iterator);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointer_OutOfBoundsPosition() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);
        Attribute attribute2 = new Attribute("testAttribute2", "testValue2");
        attribute2.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.setPosition(iterator.attributes.size() + 1);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.fail();
    }

    @Test
    public void testGetNodePointer_NextAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);
        Attribute attribute2 = new Attribute("testAttribute2", "testValue2");
        attribute2.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertEquals(attribute1, nodePointer1.getNode());

        iterator.setPosition(2);
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertEquals(attribute2, nodePointer2.getNode());
    }

    @Test
    public void testGetNodePointer_FirstAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);
        Attribute attribute2 = new Attribute("testAttribute2", "testValue2");
        attribute2.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertEquals(attribute1, nodePointer1.getNode());

        iterator.setPosition(1);
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertEquals(attribute2, nodePointer2.getNode());

        iterator.setPosition(0);
        NodePointer nodePointer3 = iterator.getNodePointer();
        Assert.assertEquals(attribute1, nodePointer3.getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPosition_InvalidPosition() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.setPosition(-1);
        Assert.fail();
    }

    @Test
    public void testSetPosition_ValidPosition() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);
        Attribute attribute2 = new Attribute("testAttribute2", "testValue2");
        attribute2.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.setPosition(1);
        Assert.assertTrue(iterator.position == 1);
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());

        iterator.setPosition(0);
        Assert.assertTrue(iterator.position == 0);
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetPosition() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        QName qName = new QName(xmlns, localName);

        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "testValue1");
        attribute1.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute1);
        Attribute attribute2 = new Attribute("testAttribute2", "testValue2");
        attribute2.setNamespace(new Namespace(xmlns, prefix));
        element.addContent(attribute2);

        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());

        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute2, iterator.getNodePointer().getNode());

        iterator.setPosition(0);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(attribute1, iterator.getNodePointer().getNode());
    }
}