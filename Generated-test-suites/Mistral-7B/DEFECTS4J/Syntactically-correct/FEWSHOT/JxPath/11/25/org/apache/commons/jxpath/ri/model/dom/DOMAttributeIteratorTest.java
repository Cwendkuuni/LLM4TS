package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;

public class DOMAttributeIteratorTest {
    @Test
    public void testDOMAttributeIterator() {
        Element element = new MockElement("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(new MockDocument()), new QName("test"));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(1));
        Attr attr = new MockAttr("test1", "value1");
        element.setAttributeNode(attr);
        iterator.setParent(new DOMParentNodePointer(element));
        assertNotNull(iterator.getNodePointer());
        assertEquals("test1", iterator.getNodePointer().getName().getName());
        assertEquals("value1", iterator.getNodePointer().getValue());
        assertTrue(iterator.setPosition(2));
        Attr attr2 = new MockAttr("test2", "value2");
        element.setAttributeNode(attr2);
        assertEquals("test2", iterator.getNodePointer().getName().getName());
        assertEquals("value2", iterator.getNodePointer().getValue());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_wildcard() {
        Element element = new MockElement("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(new MockDocument()), new QName("*"));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(1));
        Attr attr = new MockAttr("test1", "value1");
        element.setAttributeNode(attr);
        iterator.setParent(new DOMParentNodePointer(element));
        assertNotNull(iterator.getNodePointer());
        assertTrue(iterator.getName().getName().equals(attr.getNodeName()));
        assertTrue(iterator.getValue().equals(attr.getValue()));
        assertTrue(iterator.setPosition(2));
        Attr attr2 = new MockAttr("test2", "value2");
        element.setAttributeNode(attr2);
        assertNotNull(iterator.getNodePointer());
        assertTrue(iterator.getName().getName().equals(attr2.getNodeName()));
        assertTrue(iterator.getValue().equals(attr2.getValue()));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testDOMAttributeIterator_namespace() {
        Element element = new MockElement("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(new MockDocument()), new QName("http://test.com/test", "test"));
        assertNull(iterator.getNodePointer());
        assertTrue(iterator.setPosition(1));
        Attr attr = new MockAttr("test1", "value1", "http://test.com/test");
        element.setAttributeNode(attr);
        iterator.setParent(new DOMParentNodePointer(element));
        assertNotNull(iterator.getNodePointer());
        assertTrue(iterator.getName().getName().equals(attr.getNodeName()));
        assertTrue(iterator.getValue().equals(attr.getValue()));
        assertTrue(iterator.getName().getNamespaceURI().equals(attr.getNamespaceURI()));
        assertNull(iterator.getNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_outOfBounds() {
        Element element = new MockElement("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentNodePointer(new MockDocument()), new QName("test"));
        iterator.setParent(new DOMParentNodePointer(element));
        assertTrue(iterator.setPosition(1));
        iterator.setPosition(2);
        assertNull(iterator.getNodePointer());
    }
}

class MockDocument implements Node {
    @Override
    public NodeType getNodeType() {
        return NodeType.DOCUMENT_NODE;
    }

    @Override
    public Node getDocumentElement() {
        return new MockElement("root");
    }

    @Override
    public NamedNodeMap getAttributes() {
        return new MockNamedNodeMap();
    }

    @Override
    public String getNodeValue() {
        return null;
    }

    @Override
    public Node getChildNodes() {
        return null;
    }

    @Override
    public Node insertBefore(Node newChild, Node refChild) {
        return null;
    }

    @Override
    public Node removeChild(Node oldChild) {
        return null;
    }

    @Override
    public Node replaceChild(Node newChild, Node oldChild) {
        return null;
    }

    @Override
    public Node appendChild(Node newChild) {
        return null;
    }
}

class MockElement implements Element {
    private String name;

    public MockElement(String name) {
        this.name = name;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ELEMENT_NODE;
    }

    @Override
    public String getTagName() {
        return name;
    }

    @Override
    public NamedNodeMap getAttributes() {
        return new MockNamedNodeMap();
    }

    @Override
    public String getAttribute(String name) {
        return null;
    }

    @Override
    public Attr setAttribute(String name, String value) {
        return new MockAttr(name, value);
    }

    @Override
    public Attr getAttributeNode(String name) {
        return new MockAttr(name, null);
    }

    @Override
    public void removeAttribute(String name) {
        // empty
    }

    @Override
    public void removeAttributeNode(Attr oldAttr) {
        // empty
    }

    @Override
    public void removeAttributeNS(String namespaceURI, String localName) {
        // empty
    }

    @Override
    public void removeAttributeNode(Node oldChild) {
        // empty
    }

    @Override
    public void replaceChildren(Node newChild) {
        // empty
    }

    @Override
    public void replaceChildren(Node newChild
            , int howMany) {
        // empty
    }

    @Override
    public NodeList getElementsByTagName(String tagName) {
        return null;
    }

    @Override
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return null;
    }

    @Override
    public Node getChildNodes() {
        return null;
    }

    @Override
    public Node getFirstChild() {
        return null;
    }

    @Override
    public Node getLastChild() {
        return null;
    }

    @Override
    public Node nextSibling() {
        return null;
    }

    @Override
    public Node previousSibling() {
        return null;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public Node cloneNode(boolean deep) {
        return null;
    }
}

class MockAttr implements Attr {
    private String name;
    private String value;
    private String namespaceURI;

    public MockAttr(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public MockAttr(String name, String value, String namespaceURI) {
        this.name = name;
        this.value = value;
        this.namespaceURI = namespaceURI;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ATTRIBUTE_NODE;
    }

    @Override
    public String getNodeName() {
        return name;
    }

    @Override
    public String getNodeValue() {
        return value;
    }

    @Override
    public String getSpecifiedValue() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getNamespaceURI() {
        return namespaceURI;
    }

    @Override
    public String getLocalName() {
        return name;
    }

    @Override
    public void setValue(String value) {
        // empty
    }

    @Override
    public void setSpecifiedValue(String value) {
        // empty
    }

    @Override
    public void setValue(String value, String namespaceURI) {
        // empty
    }

    @Override
    public void setSpecifiedValue(String value, String namespaceURI) {
        // empty
    }

    @Override
    public void setNodeValue(Node node) {
        // empty
    }

    @Override
    public Node getOwnerElement() {
        return null;
    }

    @Override
    public Node getNamespaceOwner() {
        return null;
    }

    @Override
    public void setNodeValue(String nodeValue) {
        // empty
    }

    @Override
    public void setNodeValue(Node nodeValue) {
        // empty
    }

    @Override
    public void setSpecifiedNodeValue(String nodeValue) {
        // empty
    }

    @Override
    public void setSpecifiedNodeValue(Node nodeValue) {
        // empty
    }

    @Override
    public String getTextContent() {
        return null;
    }

    @Override
    public void setTextContent(String textContent) {
        // empty
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public void setPrefix(String prefix) {
        // empty
    }

    @Override
    public String getLocalName() {
        return name;
    }

    @Override
    public Node getParentNode() {
        return null;
    }

    @Override
    public Node getNextSibling() {
        return null;
    }

    @Override
    public Node getPreviousSibling() {
        return null;
    }

    @Override
    public Node cloneNode(boolean deep) {
        return null;
    }

    @Override
    public String getBaseURI() {
        return null;
    }
}

class MockNamedNodeMap implements NamedNodeMap {
    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public Node item(int index) {
        return null;
    }

    @Override
    public Node getNamedItem(String name) {
        return null;
    }

    @Override
    public Node getNamedItemNS(String namespaceURI, String localName) {
        return null;
    }

    @Override
    public Attr removeNamedItem(String name) {
        return null;
    }

    @Override
    public Attr removeNamedItemNS(String namespaceURI, String localName) {
        return null;
    }

    @Override
    public void setNamedItem(Node newItem) {
        // empty
    }

    @Override
    public void setNamedItemNS(Node newItem) {
        // empty
    }

    @Override
    public void removeNamedItem(Node oldItem) {
        // empty
    }

    @Override
    public void removeNamedItemNS(Node oldItem) {
        // empty
    }
}

enum NodeType {
    DOCUMENT_NODE, ELEMENT_NODE, ATTRIBUTE_NODE
}