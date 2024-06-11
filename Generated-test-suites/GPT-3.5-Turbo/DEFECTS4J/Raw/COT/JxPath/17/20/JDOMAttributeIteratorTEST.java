
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNodeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNodeListIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNodeListPointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNamespaceIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNamespacePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer;
import org.apache.commons.jxpath.ri.model.NullNodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");

        Element element = new Element("testElement");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        NodePointer parent = new JDOMNodePointer(null, element);

        attributeIterator = new JDOMAttributeIterator(parent, new QName(null, "*", null));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer attributePointer = attributeIterator.getNodePointer();
        Assert.assertTrue(attributePointer instanceof JDOMAttributePointer);
        Assert.assertEquals("attribute1", ((Attribute) attributePointer.getNode()).getName());
        Assert.assertEquals("value1", ((Attribute) attributePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Assert.assertTrue(attributeIterator.setPosition(1));
        Assert.assertEquals(1, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.setPosition(2));
        Assert.assertEquals(2, attributeIterator.getPosition());
        Assert.assertFalse(attributeIterator.setPosition(3));
        Assert.assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testEmptyAttributesList() {
        NodePointer parent = new JDOMNodePointer(null, new Element("testElement"));
        JDOMAttributeIterator emptyIterator = new JDOMAttributeIterator(parent, new QName(null, "*", null));

        Assert.assertNull(emptyIterator.getNodePointer());
        Assert.assertEquals(0, emptyIterator.getPosition());
        Assert.assertFalse(emptyIterator.setPosition(1));
    }

    @Test
    public void testXmlNamespacePrefix() {
        Attribute xmlAttribute = new Attribute("attribute", "value", Namespace.XML_NAMESPACE);
        Element element = new Element("testElement");
        element.setAttribute(xmlAttribute);

        NodePointer parent = new JDOMNodePointer(null, element);

        JDOMAttributeIterator xmlIterator = new JDOMAttributeIterator(parent, new QName("xml", "attribute", ""));

        NodePointer attributePointer = xmlIterator.getNodePointer();
        Assert.assertTrue(attributePointer instanceof JDOMAttributePointer);
        Assert.assertEquals(xmlAttribute, attributePointer.getNode());
        Assert.assertEquals(1, xmlIterator.getPosition());
        Assert.assertFalse(xmlIterator.setPosition(2));
    }

    @Test
    public void testNonXmlNamespacePrefix() {
        String namespaceUri = "http://example.com";
        Namespace namespace = Namespace.getNamespace("prefix", namespaceUri);

        Attribute attribute = new Attribute("attribute", "value", namespace);
        Element element = new Element("testElement");
        element.setAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(null, element);

        JDOMAttributeIterator nsIterator = new JDOMAttributeIterator(parent, new QName("prefix", "attribute", namespaceUri));

        NodePointer attributePointer = nsIterator.getNodePointer();
        Assert.assertTrue(attributePointer instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute, attributePointer.getNode());
        Assert.assertEquals(1, nsIterator.getPosition());
        Assert.assertFalse(nsIterator.setPosition(2));
    }

    @Test
    public void testNoNamespacePrefix() {
        Attribute attribute = new Attribute("attribute", "value");
        Element element = new Element("testElement");
        element.setAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(null, element);

        JDOMAttributeIterator nsIterator = new JDOMAttributeIterator(parent, new QName("", "attribute", ""));

        NodePointer attributePointer = nsIterator.getNodePointer();
        Assert.assertTrue(attributePointer instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute, attributePointer.getNode());
        Assert.assertEquals(1, nsIterator.getPosition());
        Assert.assertFalse(nsIterator.setPosition(2));
    }

    @Test
    public void testWildcardName() {
        Attribute attribute1 = new Attribute("attribute1", Namespace.NO_NAMESPACE);
        Attribute attribute2 = new Attribute("attribute2", Namespace.getNamespace("prefix", "http://example.com"));
        Attribute attribute3 = new Attribute("attribute3", Namespace.XML_NAMESPACE);

        Element element = new Element("testElement");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        NodePointer parent = new JDOMNodePointer(null, element);

        JDOMAttributeIterator wildcardIterator = new JDOMAttributeIterator(parent, new QName(null, "*", null));

        NodePointer attributePointer1 = wildcardIterator.getNodePointer();
        Assert.assertTrue(attributePointer1 instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute1, attributePointer1.getNode());
        Assert.assertEquals(1, wildcardIterator.getPosition());
        Assert.assertTrue(wildcardIterator.setPosition(2));

        NodePointer attributePointer2 = wildcardIterator.getNodePointer();
        Assert.assertTrue(attributePointer2 instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute2, attributePointer2.getNode());
        Assert.assertEquals(2, wildcardIterator.getPosition());
        Assert.assertTrue(wildcardIterator.setPosition(3));

        NodePointer attributePointer3 = wildcardIterator.getNodePointer();
        Assert.assertTrue(attributePointer3 instanceof JDOMAttributePointer);
        Assert.assertEquals(attribute3, attributePointer3.getNode());
        Assert.assertEquals(3, wildcardIterator.getPosition());
        Assert.assertFalse(wildcardIterator.setPosition(4));
    }

    @Test
    public void testNonElementNode() {
        NodePointer parent = new JDOMNodePointer(null, new Attribute("attribute", "value"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(null, "*", null));

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(1));
    }
}
