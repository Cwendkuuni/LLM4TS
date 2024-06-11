import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMPointedNode(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorWithOneAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("test", "testValue", "testNs");
        element.addAttribute(attribute);
        NodePointer parent = new JDOMPointedNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("testNs", "test"));
        NodePointer attributePointer = iterator.getNodePointer();
        Assertions.assertEquals("testValue", attributePointer.getNode().getValue());
        Assertions.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("test1", "testValue1", "testNs1");
        Attribute attribute2 = new Attribute("test2", "testValue2", "testNs2");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        NodePointer parent = new JDOMPointedNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("testNs1", "*"));
        NodePointer firstAttributePointer = iterator.getNodePointer();
        Assertions.assertEquals("testValue1", firstAttributePointer.getNode().getValue());
        Assertions.assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        NodePointer secondAttributePointer = iterator.getNodePointer();
        Assertions.assertEquals("testValue2", secondAttributePointer.getNode().getValue());
        Assertions.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testIteratorWithEmptyAttributeName() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("test", "testValue", "testNs");
        element.addAttribute(attribute);
        NodePointer parent = new JDOMPointedNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        List attributes = (List) parent.getNode().getChildren();
        Assertions.assertEquals(1, attributes.size());
        NodePointer attributePointer = iterator.getNodePointer();
        Assertions.assertEquals(attribute, attributePointer.getNode());
        Assertions.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testIteratorWithNullParent() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("", ""));
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorWithNullName() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("test", "testValue", "testNs");
        element.addAttribute(attribute);
        NodePointer parent = new JDOMPointedNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorWithNonExistentAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("test", "testValue", "testNs");
        element.addAttribute(attribute);
        NodePointer parent = new JDOMPointedNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("nonExistentNs", "nonExistentName"));
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIteratorWithEmptyParent() {
        Element root = new Element("root");
        NodePointer parent = new JDOMPointedNode(root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(0, iterator.getPosition());
    }
}