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
    public void testConstructor_NullParent() {
        Assert.assertNull(new JDOMAttributeIterator(null, new QName("", "attributeName")));
    }

    @Test
    public void testConstructor_EmptyElement() {
        Element emptyElement = new Element("empty");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(emptyElement), new QName("", "attributeName"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertTrue(iterator.getPosition() == 0);
    }

    @Test
    public void testConstructor_ElementWithAttributes() {
        String xmlNamespaceUri = "http://www.w3.org/XML/1998/namespace";
        String xmlPrefix = "xml";
        String prefix = "test";
        String uri = "http://test.com/namespace";
        String namespaceUri = "http://test.com/namespace";
        String name = "testAttribute";

        Element element = new Element("testElement")
                .addNamespaceDeclaration(xmlNamespaceUri, xmlPrefix)
                .addNamespaceDeclaration(namespaceUri, prefix)
                .addAttribute(new Attribute("xml:attr1", "value1", xmlNamespaceUri))
                .addAttribute(new Attribute("test:attr2", "value2", new Namespace(prefix, uri)))
                .addAttribute(new Attribute("test:attr3", "value3", new Namespace(prefix, uri)));

        NodePointer nodePointer = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("test", "testAttribute"));

        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals(3, iterator.getPosition());

        Attribute currentAttribute = iterator.getNodePointer().getNode();
        Assert.assertEquals("test:attr3", currentAttribute.getName());
        Assert.assertEquals("value3", currentAttribute.getValue());

        iterator.setPosition(0);
        currentAttribute = iterator.getNodePointer().getNode();
        Assert.assertEquals("test:attr2", currentAttribute.getName());
        Assert.assertEquals("value2", currentAttribute.getValue());

        iterator.setPosition(1);
        currentAttribute = iterator.getNodePointer().getNode();
        Assert.assertEquals("xml:attr1", currentAttribute.getName());
        Assert.assertEquals("value1", currentAttribute.getValue());

        iterator.setPosition(4);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testConstructor_ElementWithEmptyListOfAttributes() {
        Element element = new Element("testElement");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attributeName"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertTrue(iterator.getPosition() == 0);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullQName() {
        new JDOMAttributeIterator(new NodePointer(new Element("testElement")), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyQName() {
        new JDOMAttributeIterator(new NodePointer(new Element("testElement")), new QName(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullNamespaceResolver() {
        new JDOMAttributeIterator(new NodePointer(new Element("testElement")), new QName("xml", ""));
    }

    @Test
    public void testGetNodePointer_EndOfList() {
        String namespaceUri = "http://test.com/namespace";
        String prefix = "test";
        String name = "testAttribute";

        Element element = new Element("testElement")
                .addNamespaceDeclaration(namespaceUri, prefix)
                .addAttribute(new Attribute("test:attr1", "value1", new Namespace(prefix, namespaceUri)))
                .addAttribute(new Attribute("test:attr2", "value2", new Namespace(prefix, namespaceUri)));

        NodePointer nodePointer = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("test", name));

        for (int i = 0; i < iterator.getSize(); i++) {
            iterator.next();
        }

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        String namespaceUri = "http://test.com/namespace";
        String prefix = "test";
        String name = "testAttribute";

        Element element = new Element("testElement")
                .addNamespaceDeclaration(namespaceUri, prefix)
                .addAttribute(new Attribute("test:attr1", "value1", new Namespace(prefix, namespaceUri)))
                .addAttribute(new Attribute("test:attr2", "value2", new Namespace(prefix, namespaceUri)));

        NodePointer nodePointer = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("test", name));

        iterator.setPosition(-1);
        Assert.assertNull(iterator.getNodePointer());

        iterator.setPosition(5);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetSize() {
        String namespaceUri = "http://test.com/namespace";
        String prefix = "test";
        String name = "testAttribute";

        Element element = new Element("testElement")
                .addNamespaceDeclaration(namespaceUri, prefix)
                .addAttribute(new Attribute("test:attr1", "value1", new Namespace(prefix, namespaceUri)))
                .addAttribute(new Attribute("test:attr2", "value2", new Namespace(prefix, namespaceUri)))
                .addAttribute(new Attribute("test:attr3", "value3", new Namespace(prefix, namespaceUri)));

        NodePointer nodePointer = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("test", name));

        Assert.assertEquals(3, iterator.getSize());
    }
}