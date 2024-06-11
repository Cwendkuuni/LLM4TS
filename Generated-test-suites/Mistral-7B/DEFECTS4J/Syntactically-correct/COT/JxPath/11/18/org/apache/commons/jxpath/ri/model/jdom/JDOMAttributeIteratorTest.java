import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("xml:attr2", "value2");
        root.addAttribute("attr3", "value3");

        parentNode = new NodePointer(root);
    }

    @Test
    public void testConstructorWithQName() {
        QName attrName = new QName("", "attr1");
        attributeIterator = new JDOMAttributeIterator(parentNode, attrName);
        Assert.assertEquals(1, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.hasNext());

        Attribute expectedAttr = parentNode.getNode().getAttribute("attr1");
        Attribute actualAttr = ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertNotNull(actualAttr);
        Assert.assertEquals(expectedAttr.getValue(), actualAttr.getValue());

        attributeIterator.next();
        Assert.assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testConstructorWithWildcardQName() {
        attributeIterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
        Assert.assertEquals(3, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.hasNext());

        attributeIterator.next();
        Assert.assertEquals(2, attributeIterator.getPosition());
        Attribute expectedAttr2 = parentNode.getNode().getAttribute("xml:attr2");
        Attribute actualAttr2 = ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertNotNull(actualAttr2);
        Assert.assertEquals(expectedAttr2.getValue(), actualAttr2.getValue());

        attributeIterator.next();
        Assert.assertEquals(1, attributeIterator.getPosition());
        Attribute expectedAttr3 = parentNode.getNode().getAttribute("attr3");
        Attribute actualAttr3 = ((JDOMAttribePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertNotNull(actualAttr3);
        Assert.assertEquals(expectedAttr3.getValue(), actualAttr3.getValue());

        attributeIterator.next();
        Assert.assertFalse(attributeIterator.hasNext());
    }

    @Test
    public void testGetPosition() {
        Attribute attr1 = ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertEquals(1, attributeIterator.getPosition());

        attributeIterator.next();
        Attribute attr2 = ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertEquals(2, attributeIterator.getPosition());

        attributeIterator.next();
        Attribute attr3 = ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        attributeIterator.next();
        attributeIterator.next();

        attributeIterator.setPosition(0);
        Attribute attr1 = ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute();
        Assert.assertEquals(1, attributeIterator.getPosition());
        Assert.assertEquals(attr1, ((JDOMAttributePointer) attributeIterator.getNodePointer()).getAttribute());

        attributeIterator.setPosition(3);
        Assert.assertFalse(attributeIterator.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionOutOfBounds() {
        attributeIterator.setPosition(4);
    }

    @Test
    public void testHasNext() {
        Assert.assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        Assert.assertTrue(attributeIterator.hasNext());
        attributeIterator.next();
        Assert.assertFalse(attributeIterator.hasNext());
    }
}