package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() throws Exception {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("attr3", "value3", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        root.addAttribute("attr4", "value4", "ns:prefix", "http://example.com/ns");

        Element element = new Element("element");
        element.addAttribute("attr5", "value5");
        root.addContent(element);

        parentNode = new JDOMAttrributePointer(
            new JDOMNodePointer(root),
            root.getAttribute("attr1")
        );
    }

    @Test
    public void testConstructorWithSimpleQName() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        Assert.assertEquals("value1", iterator.getNodePointer().getNode().getValue());
    }

    @Test
    public void testConstructorWithWildcardQName() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("*"));
        Assert.assertEquals("value1", iterator.getNodePointer().getNode().getValue());
        Assert.assertEquals("value2", iterator.getNodePointer(1).getNode().getValue());
        Assert.assertEquals("value3", iterator.getNodePointer(2).getNode().getValue());
        Assert.assertEquals("value4", iterator.getNodePointer(3).getNode().getValue());
        Assert.assertEquals("value5", iterator.getNodePointer(4).getNode().getValue());
    }

    @Test
    public void testConstructorWithNamespaceQName() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("xmlns:xsi", ""));
        Assert.assertEquals("http://www.w3.org/2001/XMLSchema-instance", iterator.getNodePointer().getNode().getNamespaceURI());
    }

    @Test
    public void testConstructorWithInvalidNamespacePrefix() {
        try {
            iterator = new JDOMAttributeIterator(parentNode, new QName("invalid:prefix", ""));
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testConstructorWithNullParentPointer() {
        try {
            iterator = new JDOMAttributeIterator(null, new QName("attr1"));
            Assert.fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = JDOMException.class)
    public void testConstructorWithNullElement() {
        NodePointer nullParent = new JDOMNodePointer(null);
        iterator = new JDOMAttributeIterator(nullParent, new QName("attr1"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerWithNegativeIndex() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        iterator.getNodePointer(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerWithIndexGreaterThanSize() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        iterator.setPosition(5);
        iterator.getNodePointer(1);
    }

    @Test
    public void testSetPositionWithNegativeIndex() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        Assert.assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testSetPositionWithIndexGreaterThanSize() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        Assert.assertFalse(iterator.setPosition(5));
    }

    @Test
    public void testSetPositionWithValidIndex() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        iterator.setPosition(2);
        Assert.assertEquals("value3", iterator.getNodePointer().getNode().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionWithInvalidIndex() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        iterator.setPosition(10);
    }

    @Test
    public void testGetPosition() {
        iterator = new JDOMAttributeIterator(parentNode, new QName("attr1"));
        Assert.assertEquals(1, iterator.getPosition());
        iterator.setPosition(3);
        Assert.assertEquals(3, iterator.getPosition());
    }
}