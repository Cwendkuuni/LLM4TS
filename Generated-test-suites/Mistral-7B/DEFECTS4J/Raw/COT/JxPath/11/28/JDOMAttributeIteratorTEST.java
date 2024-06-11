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
 * Test cases for JDOMAttributeIterator class
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructorWithQualifiedName() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1", "prefix1", "uri1");
        NodePointer nodePointer = new NodePointer(root);
        QName qName = new QName("prefix1:attr1", "uri1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qName);
        Attribute expectedAttribute = root.getAttribute("attr1", "uri1");
        Assert.assertEquals(expectedAttribute, ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute());
    }

    @Test
    public void testConstructorWithWildcardName() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        List<Attribute> attributes = root.getAttributes();
        Assert.assertEquals(attributes.get(0), ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute());
        Assert.assertEquals(attributes.get(1), ((JDOMAttributePointer) iterator.nextNodePointer()).getAttribute());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullParent() {
        NodePointer nodePointer = null;
        JDOMAttribeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
    }

    @Test
    public void testGetPosition() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        iterator.nextNodePointer();
        int expectedPosition = 1;
        int actualPosition = iterator.getPosition();
        Assert.assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testSetPositionWithValidIndex() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        int expectedPosition = 1;
        iterator.setPosition(expectedPosition);
        int actualPosition = iterator.getPosition();
        Assert.assertEquals(expectedPosition, actualPosition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionWithInvalidIndex() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        int invalidPosition = -1;
        iterator.setPosition(invalidPosition);
    }

    @Test
    public void testGetNodePointer() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        Attribute expectedAttribute1 = root.getAttribute("attr1");
        Attribute expectedAttribute2 = root.getAttribute("attr2");
        Assert.assertEquals(expectedAttribute1, iterator.getNodePointer().getAttribute());
        Assert.assertEquals(expectedAttribute2, iterator.nextNodePointer().getAttribute());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNodePointerWithEndOfIterator() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        iterator.nextNodePointer();
        iterator.nextNodePointer();
        NodePointer nodePointer = iterator.getNodePointer();
    }

    @Test
    public void testIteratorBehavior() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        List<Attribute> attributes = root.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute expectedAttribute = attributes.get(i);
            Attribute actualAttribute = iterator.getNodePointer().getAttribute();
            Assert.assertEquals(expectedAttribute, actualAttribute);
            iterator.nextNodePointer();
        }
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testEmptyIterator() {
        Element root = new Element("root");
        NodePointer nodePointer = new NodePointer(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        Assert.assertNull(iterator.getNodePointer());
    }

}