import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructorWithNullParent() {
        Assert.assertNull(new JDOMAttributeIterator(null, new QName("", "")));
    }

    @Test
    public void testConstructorWithEmptyNamespace() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testConstructorWithWildcard() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "*"));
        Attribute firstAttribute = element.getAttribute("attr1");
        Attribute secondAttribute = element.getAttribute("attr2");
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(firstAttribute, iterator.getNodePointer().getNode());
        Assert.assertTrue(iterator.setPosition(2));
        Attribute thirdAttribute = element.getAttribute("attr3");
        Assert.assertEquals(thirdAttribute, iterator.getNodePointer().getNode());
        Assert.assertEquals(3, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testConstructorWithSpecificAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attr", "value");
        element.addAttribute(attribute);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attr"));
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPrefix() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("x", "attr"));
    }

    @Test
    public void testConstructorWithNamespace() {
        Element element = new Element("root", new QName("ns", ""));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("ns", "attr"));
        Attribute attribute = element.getAttribute("attr");
        Assert.assertNotNull(iterator.getNodePointer());
        Assert.assertEquals(1, iterator.getPosition());
        Assert.assertEquals(attribute, iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetNodePointerWithEndOfList() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        Attribute firstAttribute = element.getAttribute("attr1");
        Attribute secondAttribute = element.getAttribute("attr2");
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        Attribute firstAttribute = element.getAttribute("attr1");
        iterator.setPosition(2);
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithEndOfList() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", ""));
        Attribute firstAttribute = element.getAttribute("attr1");
        Attribute secondAttribute = element.getAttribute("attr2");
        iterator.setPosition(2);
        iterator.setPosition(3);
        Assert.assertFalse(iterator.setPosition(4));
        Assert.assertEquals(2, iterator.getPosition());
    }
}