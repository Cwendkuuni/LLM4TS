import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new Element("root");
        ParentNamespaceResolver namespaceResolver = new ParentNamespaceResolver();
        parentNode = new NodePointer(element, namespaceResolver);
    }

    @Test
    public void testEmptyAttributeIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithOneAttribute() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));

        parentNode.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "attr1"));
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
        Attribute expectedAttribute = iterator.getNodePointer().getNode().getAttribute("attr1");
        Assert.assertEquals(expectedAttribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testAttributeIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));
        element.addAttribute(new Attribute("attr2", "value2"));

        parentNode.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
        Assert.assertEquals(2, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());

        Attribute expectedAttribute1 = iterator.getNodePointer().getNode().getAttribute("attr1");
        Attribute expectedAttribute2 = iterator.getNodePointer().getNode().getAttribute("attr2");
        Assert.assertEquals(expectedAttribute1, iterator.getNodePointer().getNode());
        Assert.assertEquals(expectedAttribute2, iterator.next().getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttributeIteratorWithNegativePosition() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));

        parentNode.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "attr1"));

        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttributeIteratorWithPositionGreaterThanSize() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));

        parentNode.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "attr1"));

        iterator.setPosition(3);
    }

    @Test
    public void testAttributeIteratorWithNullName() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));

        parentNode.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithEmptyNamespace() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("xml:attr1", "value1"));

        ParentNamespaceResolver namespaceResolver = new ParentNamespaceResolver();
        namespaceResolver.putNamespace("xml", "http://www.w3.org/XML/1998/namespace");
        parentNode.setNode(element);
        parentNode.setNamespaceResolver(namespaceResolver);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("xml", "attr1"));
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithNonExistentName() {
        Element element = new Element("root");

        parentNode.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("nonExistent", "nonExistentName"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.hasNext());
    }
}