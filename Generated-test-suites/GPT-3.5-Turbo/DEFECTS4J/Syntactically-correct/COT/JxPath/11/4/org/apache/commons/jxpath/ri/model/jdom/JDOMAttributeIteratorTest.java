
import org.apache.commons.jxpath.ri.QName;
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
    private JDOMAttributeIterator iterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        Assert.assertTrue(result);

        int position = iterator.getPosition();
        Assert.assertEquals(1, position);

        result = iterator.setPosition(-1);
        Assert.assertFalse(result);

        result = iterator.setPosition(3);
        Assert.assertFalse(result);
    }

    @Test
    public void testParentNotElement() {
        Element element = new Element("test");
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testPrefixXml() {
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        element.setAttribute("xml:attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("xml:attr1"));

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testPrefixNotXmlNamespaceNull() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        QName name = new QName("prefix:attr1");
        iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testWildcardNameNoAttributes() {
        Element element = new Element("test");
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("*"));

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testSetPositionInvalid() {
        boolean result = iterator.setPosition(0);
        Assert.assertFalse(result);

        result = iterator.setPosition(5);
        Assert.assertFalse(result);
    }
}

