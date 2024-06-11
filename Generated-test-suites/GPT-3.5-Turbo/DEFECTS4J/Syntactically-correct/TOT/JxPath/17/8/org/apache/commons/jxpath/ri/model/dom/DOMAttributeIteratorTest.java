
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        Node node = new ElementMock();
        parent = new DOMNodePointer(node);
        name = new QName("testName");
    }

    @Test
    public void testGetNodePointer_withMatchingAttribute_returnsCorrectNodePointer() {
        Element element = new ElementMock();
        Attr attr = new AttrMock(name.getName(), "testValue");
        element.setAttributeNode(attr);

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("NodePointer should not be null", nodePointer);
        Assert.assertEquals("NodePointer should point to the correct attribute", attr, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_withNoMatchingAttribute_returnsNull() {
        Element element = new ElementMock();
        Attr attr = new AttrMock("otherName", "testValue");
        element.setAttributeNode(attr);

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull("NodePointer should be null", nodePointer);
    }

    @Test
    public void testGetNodePointer_withNamespaceAttribute_returnsCorrectNodePointer() {
        Element element = new ElementMock();
        element.setAttributeNS("http://www.example.com", name.getName(), "testValue");

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("NodePointer should not be null", nodePointer);
        Assert.assertEquals("NodePointer should point to the correct attribute", element.getAttributeNodeNS("http://www.example.com", name.getName()), nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_withInvalidNamespacePrefixAttribute_returnsNull() {
        Element element = new ElementMock();
        element.setAttributeNS("http://www.example.com", "invalid:testName", "testValue");

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull("NodePointer should be null", nodePointer);
    }

    @Test
    public void testGetNodePointer_withNullNamespaceAttribute_returnsCorrectNodePointer() {
        Element element = new ElementMock();
        element.setAttributeNS(null, name.getName(), "testValue");

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull("NodePointer should not be null", nodePointer);
        Assert.assertEquals("NodePointer should point to the correct attribute", element.getAttributeNode(name.getName()), nodePointer.getNode());
    }

    @Test
    public void testGetPosition_withInitialPosition_returnsZero() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        int position = iterator.getPosition();

        Assert.assertEquals("Position should be 0", 0, position);
    }

    @Test
    public void testSetPosition_withValidPosition_returnsTrue() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(1);

        Assert.assertTrue("setPosition should return true", result);
        Assert.assertEquals("Position should be set correctly", 1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_withInvalidPosition_returnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(2);

        Assert.assertFalse("setPosition should return false", result);
        Assert.assertEquals("Position should not be changed", 0, iterator.getPosition());
    }

    @Test
    public void testNodeIterator_withMultipleMatchingAttributes_returnsCorrectOrder() {
        Element element = new ElementMock();
        Attr attr1 = new AttrMock(name.getName(), "testValue1");
        Attr attr2 = new AttrMock(name.getName(), "testValue2");
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer nodePointer1 = iterator.getNodePointer();
        NodePointer nodePointer2 = iterator.getNodePointer();

        Assert.assertNotNull("NodePointer 1 should not be null", nodePointer1);
        Assert.assertNotNull("NodePointer 2 should not be null", nodePointer2);
        Assert.assertEquals("NodePointer 1 should point to the first attribute", attr1, nodePointer1.getNode());
        Assert.assertEquals("NodePointer 2 should point to the second attribute", attr2, nodePointer2.getNode());
    }

    @Test
    public void testNodeIterator_withNoAttributes_returnsNull() {
        Element element = new ElementMock();

        parent.setNode(element);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull("NodePointer should be null", nodePointer);
    }

    class ElementMock implements Element {

        private Map<String, String> attributes;

        ElementMock() {
            attributes = new HashMap<>();
        }

        @Override
        public String getAttribute(String attrName) {
            return attributes.get(attrName);
        }

        @Override
        public void setAttribute(String attrName, String attrValue) {
            attributes.put(attrName, attrValue);
        }

        // Implement other methods if necessary
    }

    class AttrMock implements Attr {

        private String name;
        private String value;

        AttrMock(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getNodeName() {
            return name;
        }

        @Override
        public void setNodeValue(String nodeValue) {
            value = nodeValue;
        }

        @Override
        public String getNodeValue() {
            return value;
        }

        // Implement other methods if necessary
    }
}
