import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;
    private QName name;
    private Element element;

    @Before
    public void setUp() {
        name = new QName("http://example.com", "test", "attr");
        Element testElement = new Element("test");
        testElement.setAttributeNode(new Attr("xmlns:x", "http://example.com"));
        testElement.setAttributeNode(new Attr("test", "value1"));
        testElement.setAttributeNode(new Attr("attr", "value2"));
        element = testElement;
    }

    @Test
    public void testConstructor_SingleAttribute() {
        NodePointer parent = new DOMPointNode(element);
        iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull("Expected iterator to return an attribute node", nodePointer);
        Assert.assertEquals("Expected attribute node to match the given name", name, nodePointer.getName());
    }

    @Test
    public void testConstructor_MultipleAttributes() {
        NodePointer parent = new DOMPointNode(element);
        iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        List<NodePointer> nodes = new ArrayList<>();

        for (int i = 0; i < iterator.getSize(); i++) {
            nodes.add(iterator.getNodePointer());
            iterator.next();
        }

        for (int i = 0; i < nodes.size(); i++) {
            NodePointer nodePointer = nodes.get(i);
            Assert.assertNotNull("Expected iterator to return an attribute node", nodePointer);
            Assert.assertEquals("Expected " + i + "th attribute node to match the given name", new QName("", "attr"), nodePointer.getName());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructor_NoMatchingAttribute() {
        NodePointer parent = new DOMPointNode(element);
        iterator = new DOMAttributeIterator(parent, new QName("http://example.com", "nonexistent", "attr"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test
    public void testConstructor_NamespaceAttribute() {
        NodePointer parent = new DOMPointNode(element);
        iterator = new DOMAttributeIterator(parent, new QName("http://example.com", "test", "xmlns:x"));
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull("Expected iterator to return a namespace node", nodePointer);
        Assert.assertEquals("Expected namespace node to match the given name", new QName("http://example.com", "test", "xmlns:x"), nodePointer.getName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPosition_NegativeIndex() {
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPosition_ZeroIndex() {
        iterator.setPosition(0);
        iterator.next();
        iterator.setPosition(-1);
    }

    @Test
    public void testGetPosition_ValidIndex() {
        iterator.setPosition(1);
        iterator.next();
        iterator.next();
        iterator.setPosition(3);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("Expected 3rd attribute node to be returned", 3, iterator.getPosition());
        Assert.assertEquals("Expected 3rd attribute node to match the given name", new QName("", "attr"), nodePointer.getName());
    }

    @Test
    public void testSetPosition_NegativeIndex() {
        iterator.setPosition(-1);
        Assert.assertFalse("Expected setPosition to fail with negative index", iterator.setPosition(-1));
    }

    @Test
    public void testSetPosition_ZeroIndex() {
        iterator.setPosition(0);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("Expected 1st attribute node to be returned", 1, iterator.getPosition());
        Assert.assertEquals("Expected 1st attribute node to match the given name", name, nodePointer.getName());
    }

    @Test
    public void testSetPosition_ValidIndex() {
        iterator.setPosition(1);
        iterator.next();
        iterator.setPosition(3);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertEquals("Expected 3rd attribute node to be returned", 3, iterator.getPosition());
        Assert.assertEquals("Expected 3rd attribute node to match the given name", new QName("", "attr"), nodePointer.getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_InvalidIndex() {
        iterator.setPosition(6);
    }
}