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

    private NodePointer rootNode;
    private NodeIterator iterator;

    @Before
    public void setUp() {
        Element rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");
        rootElement.addAttribute("xml:id", "rootId");

        rootNode = new NodePointer(rootElement);
    }

    @Test
    public void testNextAttribute() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("", "attr1"));
        Attribute expected = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("value1", expected.getValue());

        iterator.next();
        Attribute actual = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("value2", actual.getValue());

        iterator.next(); // no more attributes
        Assert.assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testNextAttributeWithEmptyList() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("", "nonExistentAttr"));
        Assert.assertNull(iterator.getNodePointer().getNode());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNextAttributeWithXmlNamespace() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("xml", null, "id"));
        Attribute expected = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("rootId", expected.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextAttributeWithWrongNamespace() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("wrong", null, "attr"));
        iterator.next(); // should throw an exception
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionWithNegativeIndex() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("", "attr1"));
        iterator.next();

        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithIndexGreaterThanSize() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("", "attr1"));
        iterator.next();
        iterator.next();

        iterator.setPosition(3);
    }

    @Test
    public void testSetPositionWithValidIndex() {
        iterator = new JDOMAttributeIterator(rootNode, new QName("", "attr1"));
        iterator.next();

        iterator.setPosition(0);
        Attribute expected = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("value1", expected.getValue());
    }
}