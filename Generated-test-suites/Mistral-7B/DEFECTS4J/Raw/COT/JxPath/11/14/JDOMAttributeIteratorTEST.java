import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
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
    public void testConstructor_NullParent() {
        Assert.assertNull(new JDOMAttributeIterator(null, new QName("", "")));
    }

    @Test
    public void testConstructor_EmptyElement() {
        Element emptyElement = new Element("element");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("", ""));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_SingleAttribute() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName = "xmlns";
        Element element = new Element("element");
        element.setAttribute(new QName(xmlns, localName), "http://www.w3.org/2000/xmlns/");
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, localName));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals(xmlns, attributePointer.getNamespaceURI());
        Assert.assertEquals(localName, attributePointer.getName().getLocalPart());
        Assert.assertEquals(element, attributePointer.getNode().getParent());
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals(xmlns, attributePointer1.getNamespaceURI());
        Assert.assertEquals("xmlns", attributePointer1.getName().getLocalPart());
        Assert.assertEquals(element, attributePointer1.getNode().getParent());

        JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals("", attributePointer2.getNamespaceURI());
        Assert.assertEquals("class", attributePointer2.getName().getLocalPart());
        Assert.assertEquals(element, attributePointer2.getNode().getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullName() {
        Element element = new Element("element");
        NodePointer parent = new NodePointer(element);
        new JDOMAttributeIterator(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        Element element = new Element("element");
        NodePointer parent = new NodePointer(element);
        new JDOMAttributeIterator(parent, new QName("", ""));
    }

    @Test
    public void testGetNodePointer_EndOfList() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        iterator.setPosition(iterator.getPosition() + iterator.size());
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertNull(attributePointer);
    }

    @Test
    public void testGetPosition_StartPosition() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition_CurrentPosition() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        iterator.next();
        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_NegativePosition() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_PositionGreaterThanSize() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        iterator.setPosition(iterator.size() + 1);
    }

    @Test
    public void testSetPosition_PositionInTheMiddle() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*"));
        iterator.next();
        iterator.next();
        iterator.setPosition(1);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Assert.assertEquals(xmlns, attributePointer.getNamespaceURI());
        Assert.assertEquals(localName1, attributePointer.getName().getLocalPart());
        Assert.assertEquals(element, attributePointer.getNode().getParent());
    }

    @Test
    public void testSize_EmptyList() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName = "xmlns";
        Element element = new Element("element");
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, ""));
        Assert.assertEquals(0, iterator.size());
    }

    @Test
    public void testSize_SingleAttribute() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName = "xmlns";
        String value = "http://www.w3.org/2000/xmlns/";
        Element element = new Element("element");
        Attribute attribute = new Attribute(new QName(xmlns, localName), value);
        element.addContent(attribute);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, ""));
        Assert.assertEquals(1, iterator.size());
    }

    @Test
    public void testSize_MultipleAttributes() {
        String xmlns = "xml";
        String prefix = "xml";
        String localName1 = "xmlns";
        String localName2 = "class";
        String value1 = "http://www.w3.org/2000/xmlns/";
        String value2 = "foo";
        Element element = new Element("element");
        Attribute attribute1 = new Attribute(new QName(xmlns, localName1), value1);
        Attribute attribute2 = new Attribute(new QName("", localName2), value2);
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new NodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, ""));
        Assert.assertEquals(2, iterator.size());
    }
}