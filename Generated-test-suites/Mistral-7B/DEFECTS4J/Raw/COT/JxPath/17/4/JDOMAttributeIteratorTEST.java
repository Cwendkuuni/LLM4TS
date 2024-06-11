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

    private NodePointer nodePointer;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        Element element = new Element("root");
        element.addAttribute("attr1", "value1");
        element.addAttribute("attr2", "value2");
        nodePointer = new NodePointer(element);
    }

    @Test
    public void testConstructorWithSpecificAttribute() {
        QName name = new QName("*:attr1");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertEquals(1, iterator.getPosition());
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testConstructorWithAllAttributes() {
        QName name = new QName("*:*");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertEquals(2, iterator.getPosition());
        Attribute firstAttribute = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("attr1", firstAttribute.getName());
        Assert.assertEquals("value1", firstAttribute.getValue());
        iterator.next();
        Attribute secondAttribute = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("attr2", secondAttribute.getName());
        Assert.assertEquals("value2", secondAttribute.getValue());
    }

    @Test
    public void testConstructorWithNoAttributes() {
        Element emptyElement = new Element("root");
        nodePointer = new NodePointer(emptyElement);
        QName name = new QName("*:attr1");
        iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNextWithEndOfList() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("*:*"));
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.next();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithNegativeNumber() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("*:*"));
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithNumberGreaterThanSize() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("*:*"));
        iterator.setPosition(3);
    }

    @Test
    public void testSetPositionWithValidNumber() {
        iterator = new JDOMAttributeIterator(nodePointer, new QName("*:*"));
        iterator.setPosition(1);
        Attribute attribute = (Attribute) iterator.getNodePointer().getNode();
        Assert.assertEquals("attr1", attribute.getName());
        Assert.assertEquals("value1", attribute.getValue());
    }
}