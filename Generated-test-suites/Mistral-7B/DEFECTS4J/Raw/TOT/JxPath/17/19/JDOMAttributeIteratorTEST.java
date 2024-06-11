package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

/**
 * Test cases for JDOMAttributeIterator class.
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testIteratingAttributesWithSingleNamespace() throws Exception {
        String xml = "<root xmlns:x='http://test.com'><elem x:attr='value'/></root>";
        SAXBuilder saxBuilder = new SAXBuilder();
        Element root = saxBuilder.build(new StringReader(xml));
        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("{http://test.com}", "attr"));

        List expectedAttributes = new ArrayList();
        expectedAttributes.add(root.getAttribute("attr"));

        for (int i = 0; i < 2; i++) {
            NodePointer attrPointer = iterator.getNodePointer();
            Attribute attr = (Attribute) attrPointer.getNode();
            Assert.assertEquals(expectedAttributes.get(i), attr);
            iterator.next();
        }
    }

    @Test
    public void testIteratingAttributesWithDefaultNamespace() throws Exception {
        String xml = "<root><elem attr='value'/></root>";
        SAXBuilder saxBuilder = new SAXBuilder();
        Element root = saxBuilder.build(new StringReader(xml));
        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr"));

        List expectedAttributes = new ArrayList();
        expectedAttributes.add(root.getAttribute("attr"));

        for (int i = 0; i < 2; i++) {
            NodePointer attrPointer = iterator.getNodePointer();
            Attribute attr = (Attribute) attrPointer.getNode();
            Assert.assertEquals(expectedAttributes.get(i), attr);
            iterator.next();
        }
    }

    @Test
    public void testIteratingAttributesWithNoAttributes() throws Exception {
        String xml = "<root/>";
        SAXBuilder saxBuilder = new SAXBuilder();
        Element root = saxBuilder.build(new StringReader(xml));
        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));

        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratingAttributesWithInvalidNamespace() throws Exception {
        String xml = "<root xmlns:x='http://test.com'><elem x:attr='value'/></root>";
        SAXBuilder saxBuilder = new SAXBuilder();
        Element root = saxBuilder.build(new StringReader(xml));
        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("{invalid}", "attr"));

        iterator.next();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratingAttributesWithInvalidLocalName() throws Exception {
        String xml = "<root xmlns:x='http://test.com'><elem x:attr='value'/></root>";
        SAXBuilder saxBuilder = new SAXBuilder();
        Element root = saxBuilder.build(new StringReader(xml));
        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("{http://test.com}", "invalidAttr"));

        iterator.next();
    }
}