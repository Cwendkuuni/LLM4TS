import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyList() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        NodePointer parent = new NodePointer(new Element("root", new QName("xml", "xmlns")));
        parent.getNode().setAttribute("attr", "value");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        NodePointer parent = new NodePointer(new Element("root"));
        parent.getNode().addAttribute("attr1", "value1", null);
        parent.getNode().addAttribute("attr2", "value2", null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        Assert.assertEquals(2, iterator.getPosition());
        NodePointer nodePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer1);
        Assert.assertTrue(nodePointer1 instanceof JDOMAttributePointer);
        Attribute attribute1 = ((JDOMAttributePointer) nodePointer1).getAttribute();
        Assert.assertEquals("value1", attribute1.getValue());

        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer2);
        Assert.assertTrue(nodePointer2 instanceof JDOMAttributePointer);
        Attribute attribute2 = ((JDOMAttributePointer) nodePointer2).getAttribute();
        Assert.assertEquals("value2", attribute2.getValue());
    }

    @Test
    public void testJDOMAttributeIterator_Namespace() {
        NodePointer parent = new NodePointer(new Element("root", new QName("xml", "p:prefix")));
        parent.getNode().addAttribute("attr", "value", new Namespace("xml", "http://www.w3.org/XML/1998/namespace"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "p:prefix", "attr"));
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIterator_OutOfBoundsPosition() {
        NodePointer parent = new NodePointer(new Element("root"));
        NodeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        iterator.setPosition(100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_NullName() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIterator_EmptyName() {
        NodePointer parent = new NodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
    }
}