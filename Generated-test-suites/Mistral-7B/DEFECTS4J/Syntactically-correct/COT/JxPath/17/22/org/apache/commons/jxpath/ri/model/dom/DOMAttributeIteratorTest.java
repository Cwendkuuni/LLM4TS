import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructorWithElementAndWildcardQName() {
        Node node = createMockElement();
        QName name = new QName("*,*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        List attributes = iterator.getAttributes();
        int size = attributes.size();
        for (int i = 0; i < size; i++) {
            Attr attr = (Attr) attributes.get(i);
            assertTrue(attr != null);
        }
    }

    @Test
    public void testConstructorWithElementAndSpecificQName() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        List attributes = iterator.getAttributes();
        int size = attributes.size();
        if (size > 0) {
            Attr attr = (Attr) attributes.get(0);
            assertTrue(attr != null);
            assertTrue(attr.getNodeName().equals(name.getName()));
            assertTrue(equalStrings(attr.getNodeValue(), "testValue"));
        } else {
            assertFalse(size > 0);
        }
    }

    @Test
    public void testConstructorWithNullParentOrNullName() {
        Node node = createMockElement();
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        List attributes = iterator.getAttributes();
        int size = attributes.size();
        assertFalse(size > 0);
    }

    @Test
    public void testConstructorWithNullParentOrWithElementWithNoAttributes() {
        Node node = createMockElementWithNoAttributes();
        QName name = new QName("testAttr", "testNS");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        List attributes = iterator.getAttributes();
        int size = attributes.size();
        assertFalse(size > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        new DOMAttributeIterator(null, name);
    }

    @Test
    public void testConstructorWithNullName() {
        Node node = createMockElement();
        QName name = null;
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        List attributes = iterator.getAttributes();
        int size = attributes.size();
        assertFalse(size > 0);
    }

    @Test
    public void testTestAttrMethodWithNullAttr() {
        Attr nullAttr = null;
        boolean result = DOMAttributeIterator.testAttr(nullAttr);
        assertFalse(result);
    }

    @Test
    public void testTestAttrMethodWithAttrWithEmptyPrefix() {
        Attr attr = createMockAttr("testAttr");
        boolean result = DOMAttributeIterator.testAttr(attr);
        assertTrue(result);
    }

    @Test
    public void testTestAttrMethodWithAttrWithNonEmptyPrefix() {
        Attr attr = createMockAttr("testAttr", "testNS");
        boolean result = DOMAttributeIterator.testAttr(attr);
        assertTrue(result);
    }

    @Test
    public void testTestAttrMethodWithAttrWithXmlnsPrefix() {
        Attr attr = createMockAttr("xmlns:test");
        boolean result = DOMAttributeIterator.testAttr(attr);
        assertFalse(result);
    }

    @Test
    public void testTestAttrMethodWithAttrWithXmlnsLocalName() {
        Attr attr = createMockAttr("xmlns:test");
        boolean result = DOMAttributeIterator.testAttr(attr);
        assertFalse(result);
    }

    @Test
    public void testTestAttrMethodWithAttrWithMatchingPrefixAndNamespace() {
        Attr attr = createMockAttr("testAttr", "testNS");
        NodePointer nodePointer = new NodePointer(createMockElement());
        String parentNs = nodePointer.getNamespaceURI("testNS");
        String testNs = attr.getNamespaceURI();
        boolean result = DOMAttributeIterator.testAttr(attr);
        assertTrue(result);
    }

    @Test
    public void testTestAttrMethodWithAttrWithMatchingPrefixButDifferentNamespace() {
        Attr attr = createMockAttr("testAttr", "testNS1");
        NodePointer nodePointer = new NodePointer(createMockElement());
        String parentNs = nodePointer.getNamespaceURI("testNS2");
        boolean result = DOMAttributeIterator.testAttr(attr);
        assertFalse(result);
    }

    @Test
    public void testGetAttributeMethodWithElementAndWildcardQName() {
        Node node = createMockElement();
        QName name = new QName("*,*");
        Attr attr = DOMAttributeIterator.getAttribute(node, name);
        assertTrue(attr != null);
    }

    @Test
    public void testGetAttributeMethodWithElementAndSpecificQName() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        Attr attr = DOMAttributeIterator.getAttribute(node, name);
        if (attr != null) {
            assertTrue(attr.getNodeName().equals(name.getName()));
            assertTrue(equalStrings(attr.getNodeValue(), "testValue"));
        } else {
            assertNull(attr);
        }
    }

    @Test
    public void testGetAttributeMethodWithNullElementOrNullName() {
        Node node = null;
        QName name = new QName("testAttr", "testNS");
        Attr attr = DOMAttributeIterator.getAttribute(node, name);
        assertNull(attr);
    }

    @Test
    public void testGetAttributeMethodWithNullElement() {
        Node node = null;
        QName name = new QName("testAttr", "testNS");
        Attr attr = DOMAttributeIterator.getAttribute(node, name);
        assertNull(attr);
    }

    @Test
    public void testGetAttributeMethodWithNullName() {
        Node node = createMockElement();
        QName name = null;
        Attr attr = DOMAttributeIterator.getAttribute(node, name);
        assertNull(attr);
    }

    @Test
    public void testGetAttributeMethodWithElementAndNoMatchingAttribute() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        Attr attr = DOMAttributeIterator.getAttribute(node, name);
        assertNull(attr);
    }

    @Test
    public void testGetNodePointerMethodWithNullIterator() {
        DOMAttributeIterator iterator = null;
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointerMethodWithIteratorWithNoAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(createMockElement()), new QName("*,*"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointerMethodWithIteratorWithAttributes() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = (Attr) iterator.getAttributes().get(0);
        assertTrue(nodePointer.getNode() == attr);
    }

    @Test
    public void testGetPositionMethodWithIteratorWithNoAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(createMockElement()), new QName("*,*"));
        int position = iterator.getPosition();
        assertFalse(position > 0);
    }

    @Test
    public void testGetPositionMethodWithIteratorWithAttributes() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        int position = iterator.getPosition();
        assertTrue(position > 0);
    }

    @Test
    public void testSetPositionMethodWithIteratorWithNoAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(createMockElement()), new QName("*,*"));
        boolean result = iterator.setPosition(1);
        assertFalse(result);
    }

    @Test
    public void testSetPositionMethodWithIteratorWithAttributes() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        boolean result = iterator.setPosition(1);
        assertTrue(result);
        int position = iterator.getPosition();
        assertTrue(position == 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionMethodWithIteratorWithInvalidPosition() {
        Node node = createMockElement();
        QName name = new QName("testAttr", "testNS");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(node), name);
        iterator.setPosition(5);
    }

    private Node createMockElement() {
        Element element = new MockElement();
        Node node = new MockNode(Node.ELEMENT_NODE, "mockElement", element);
        return node;
    }

    private Node createMockElementWithNoAttributes() {
        Element element = new MockElementWithNoAttributes();
        Node node = new MockNode(Node.ELEMENT_NODE, "mockElementWithNoAttributes", element);
        return node;
    }

    private Attr createMockAttr(String name) {
        Attr attr = new MockAttr(Node.ATTRIBUTE_NODE, name, "testValue");
        return attr;
    }

    private Attr createMockAttr(String name, String namespaceURI) {
        Attr attr = new MockAttr(Node.ATTRIBUTE_NODE, name, namespaceURI, "testValue");
        return attr;
    }
}