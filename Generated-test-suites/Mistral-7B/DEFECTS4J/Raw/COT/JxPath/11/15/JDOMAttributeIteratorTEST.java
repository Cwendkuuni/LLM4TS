import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * JDOMAttributeIteratorTest class.
 */
public class JDOMAttributeIteratorTest {

    /**
     * Test constructor JDOMAttributeIterator.
     */
    @Test
    public void testConstructor() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        Assert.assertNotNull(iterator);
    }

    /**
     * Test constructor JDOMAttributeIterator with specific attribute name.
     */
    @Test
    public void testConstructorWithName() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("attr1", new Attribute("attr1", "value1"));
                addAttribute("attr2", new Attribute("attr2", "value2"));
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        Assert.assertNotNull(iterator);
        Assert.assertEquals(1, iterator.getPosition());
    }

    /**
     * Test method getNodePointer.
     */
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("attr1", new Attribute("attr1", "value1"));
                addAttribute("attr2", new Attribute("attr2", "value2"));
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        Assert.assertEquals("attr1", attribute.getName());
        Assert.assertEquals("value1", attribute.getValue());
    }

    /**
     * Test method getPosition.
     */
    @Test
    public void testGetPosition() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("attr1", new Attribute("attr1", "value1"));
                addAttribute("attr2", new Attribute("attr2", "value2"));
                addAttribute("attr3", new Attribute("attr3", "value3"));
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        Assert.assertEquals(1, iterator.getPosition());

        iterator.next();
        Assert.assertEquals(2, iterator.getPosition());

        iterator.next();
        Assert.assertEquals(3, iterator.getPosition());

        iterator.previous();
        Assert.assertEquals(2, iterator.getPosition());
    }

    /**
     * Test method setPosition.
     */
    @Test
    public void testSetPosition() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("attr1", new Attribute("attr1", "value1"));
                addAttribute("attr2", new Attribute("attr2", "value2"));
                addAttribute("attr3", new Attribute("attr3", "value3"));
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        iterator.next();
        iterator.next();

        Assert.assertTrue(iterator.setPosition(3));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        Attribute attribute = (Attribute) attributePointer.getNode();
        Assert.assertEquals("attr3", attribute.getName());
        Assert.assertEquals("value3", attribute.getValue());

        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertFalse(iterator.setPosition(5));
    }

    /**
     * Test edge case when parent node is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParentIsNull() {
        NodePointer nodePointer = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
    }

    /**
     * Test edge case when parent node is not an Element.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParentIsNotElement() {
        NodePointer nodePointer = new NodePointer(new Text("text"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
    }

    /**
     * Test edge case when name is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNameIsNull() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
    }

    /**
     * Test edge case when name is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNameEmpty() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(""));
    }

    /**
     * Test edge case when namespace is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNamespaceIsNull() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("xml:attr", new Attribute("xml:attr", "value"));
            }
        });
        QName name = new QName("", "xml:attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
    }

    /**
     * Test edge case when attribute list is empty.
     */
    @Test
    public void testEmptyAttributeList() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        Assert.assertFalse(iterator.hasNext());
    }

    /**
     * Test edge case when position is greater than the size of attribute list.
     */
    @Test
    public void testPositionGreaterThanSize() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("attr1", new Attribute("attr1", "value1"));
                addAttribute("attr2", new Attribute("attr2", "value2"));
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        iterator.next();
        iterator.next();

        Assert.assertFalse(iterator.setPosition(4));
    }

    /**
     * Test edge case when position is less than 0.
     */
    @Test
    public void testPositionLessThanZero() {
        NodePointer nodePointer = new NodePointer(new Element("root") {
            {
                setName("root");
                addAttribute("attr1", new Attribute("attr1", "value1"));
                addAttribute("attr2", new Attribute("attr2", "value2"));
            }
        });
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));
        iterator.next();

        Assert.assertFalse(iterator.setPosition(-1));
    }
}