import org.apache.commons.jxpath.ri.model.NodeIterator;
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
    public void testJDOMAttributeIteratorConstructorWithValidElementAndName() {
        Element element = new Element("root");
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(1, iterator.attributes.size());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithValidElementAndWildcardName() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("*"));
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(2, iterator.attributes.size());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithNullElement() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, new QName("test:attribute"));
        Assert.assertNull(iterator.attributes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorConstructorWithNullName() {
        Element element = new Element("root");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), null);
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointerWithValidPosition() {
        Element element = new Element("root");
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        Attribute attribute = element.getAttribute("test:attribute");
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointerWithInvalidPosition() {
        Element element = new Element("root");
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        Attribute attribute1 = element.getAttribute("test:attribute1", new Namespace("test", "http://test.com"));
        Attribute attribute2 = element.getAttribute("test:attribute2", new Namespace("test", "http://test.com"));
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testJDOMAttributeIteratorGetPositionWithValidPosition() {
        Element element = new Element("root");
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        iterator.setPosition(1);
        int position = iterator.getPosition();
        Assert.assertEquals(1, position);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorSetPositionWithInvalidPosition() {
        Element element = new Element("root");
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(element), name);
        iterator.setPosition(0);
        iterator.setPosition(-1);
    }
}