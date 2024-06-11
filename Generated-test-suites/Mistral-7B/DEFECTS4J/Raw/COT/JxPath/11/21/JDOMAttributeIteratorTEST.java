package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        root.addNamespace("ns1", "http://test.com/");
        root.addNamespace("ns2", "http://another.com/");
        parent = new NodePointer(root);
    }

    @Test
    public void testConstructorWithQName() {
        QName name = new QName("ns1:attr1");
        iterator = new JDOMAttributeIterator(parent, name);
        Attribute expected = (Attribute) parent.getNode().getAttribute("attr1", name.getNamespace());
        NodePointer actual = iterator.getNodePointer();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual.getNode());
    }

    @Test
    public void testConstructorWithWildcardQName() {
        QName name = new QName("*");
        iterator = new JDOMAttributeIterator(parent, name);
        List<Attribute> attributes = parent.getNode().getAttributes();
        Attribute expected = attributes.get(0);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual.getNode());
    }

    @Test
    public void testGetNodePointerWithValidPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(1);
        NodePointer expected = new JDOMAttributePointer(parent, (Attribute) parent.getNode().getAttribute("attr2", parent.getNode().getNamespace("ns1")));
        NodePointer actual = iterator.getNodePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNodePointerWithInvalidPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(-1);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertNull(actual);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerWithInvalidPositionBeyondEnd() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(3);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertNull(actual);
    }

    @Test
    public void testGetPositionWithValidPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(1);
        int expected = 1;
        int actual = iterator.getPosition();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPositionWithInvalidPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(-1);
        int actual = iterator.getPosition();
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testSetPositionWithValidPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(1);
        boolean expected = true;
        boolean actual = iterator.setPosition(1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("ns1:attr1"));
        iterator.setPosition(5);
        boolean expected = false;
        boolean actual = iterator.setPosition(5);
        Assert.assertEquals(expected, actual);
    }
}