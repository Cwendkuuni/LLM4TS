package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.nodes.NamedNode;
import org.junit.Test;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NodePointer parent = new DOMPointer(node);
        QName name = new QName("test", "testLocalName");

        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getNamespaceURI("test")).thenReturn("testNamespaceURI");
        when(element.getAttributeNodeNS("testNamespaceURI", "testLocalName")).thenReturn(mock(Attr.class));
        when(element.getAttributeNode("testLocalName")).thenReturn(mock(Attr.class));

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        List<NodePointer> attributes = attributeIterator.getAttributes();
        assertTrue(attributes.size() > 0);
        NodePointer nodePointer = attributes.get(0);
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = ((DOMAttributePointer) nodePointer).getNode();
        assertEquals(name.getName(), attr.getName());
        assertEquals("testNamespaceURI", attr.getNamespaceURI());
    }

    @Test
    public void testEmptyDOMAttributeIterator() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NodePointer parent = new DOMPointer(node);
        QName name = new QName("test", "testLocalName");

        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(mock(NamedNodeMap.class));

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        List<NodePointer> attributes = attributeIterator.getAttributes();
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testDOMAttributeIteratorWithNameSpace() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NodePointer parent = new DOMPointer(node);
        QName name = new QName("test", "testPrefix:testLocalName");

        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getNamespaceURI("testPrefix")).thenReturn("testNamespaceURI");

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        List<NodePointer> attributes = attributeIterator.getAttributes();
        Attr attr = ((DOMAttributePointer) attributes.get(0)).getNode();
        assertEquals(name.getName(), attr.getName());
        assertEquals("testNamespaceURI", attr.getNamespaceURI());
    }

    @Test
    public void testDOMAttributeIteratorWithNameSpaceAndWildcard() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NodePointer parent = new DOMPointer(node);
        QName name = new QName(null, "*");

        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(node.getNamespaceURI(null)).thenReturn("testNamespaceURI");
        when(element.getAttributeNode("testAttribute")).thenReturn(mock(Attr.class));
        when(element.getAttributeNodeNS("testNamespaceURI", "testAttribute")).thenReturn(mock(Attr.class));
        when(element.getAttributes()).thenReturn(mock(NamedNodeMap.class));
        when(element.getAttributes().getLength()).thenReturn(3);
        Attr attr1 = mock(Attr.class);
        Attr attr2 = mock(Attr.class);
        Attr attr3 = mock(Attr.class);
        when(element.getAttributes().item(0)).thenReturn(attr1);
        when(element.getAttributes().item(1)).thenReturn(attr2);
        when(element.getAttributes().item(2)).thenReturn(attr3);
        when(attr1.getLocalName()).thenReturn("testAttribute1");
        when(attr2.getLocalName()).thenReturn("testAttribute2");
        when(attr3.getLocalName()).thenReturn("testAttribute3");

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        List<NodePointer> attributes = attributeIterator.getAttributes();
        NodePointer nodePointer = attributes.get(0);
        assertTrue(nodePointer instanceof DOMAttributePointer);
        Attr attr = ((DOMAttribePointer) nodePointer).getNode();
        assertEquals(name.getLocalPart(), attr.getLocalName());
        assertEquals("testNamespaceURI", attr.getNamespaceURI());
    }

    @Test
    public void testDOMAttributeIteratorWithNameSpaceAndEmptyElement() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NodePointer parent = new DOMPointer(node);
        QName name = new QName("test", "testLocalName");

        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(mock(NamedNodeMap.class));

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        List<NodePointer> attributes = attributeIterator.getAttributes();
        assertTrue(attributes.isEmpty());
    }

    @Test
    public void testDOMAttributeIteratorWithNameSpaceAndNullParent() {
        Element element = mock(Element.class);
        Node node = mock(Node.class);
        NamedNodeMap nodeMap = mock(NamedNodeMap.class);
        QName name = new QName("test", "testLocalName");

        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(nodeMap);
        when(nodeMap.getLength()).thenReturn(1);
        Attr attr = mock(Attr.class);
        when(nodeMap.item(0)).thenReturn(attr);
        when(attr.getLocalName()).thenReturn("testLocalName");

        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(null, name);
        List<NodePointer> attributes = attributeIterator.getAttributes();
        assertTrue(attributes.isEmpty());
    }
}

class DOMAttributePointer implements NodePointer {
    private NodePointer parent;
    private Attr node;

    public DOMAttributePointer(NodePointer parent, Attr node) {
        this.parent = parent;
        this.node = node;
    }

    public NodePointer getParent() {
        return parent;
    }

    public Node getNode() {
        return node;
    }
}

class DOMAttributeIterator implements NodeIterator {
    private NodePointer parent;
    private QName name;
    private List<NodePointer> attributes;
    private int position = 0;

    public DOMAttributeIterator(NodePointer parent, QName name) {
        this.parent = parent;
        this.name = name;
        attributes = new ArrayList<>();
        Node node = parent.getNode();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NamedNodeMap map = node.getAttributes();
            for (int i = 0; i < map.getLength(); i++) {
                Attr attr = (Attr) map.item(i);
                if (testAttr(attr)) {
                    attributes.add(new DOMAttributePointer(parent, attr));
                }
            }
        }
    }

    private boolean testAttr(Attr attr) {
        String nodePrefix = DOMNodePointer.getPrefix(attr);
        String nodeLocalName = DOMNodePointer.getLocalName(attr);

        if (nodePrefix != null && nodePrefix.equals("xmlns")) {
            return false;
        }

        if (nodePrefix == null && nodeLocalName.equals("xmlns")) {
            return false;
        }

        String testLocalName = name.getName();
        if (testLocalName.equals("*") || testLocalName.equals(nodeLocalName)) {
            String testPrefix = name.getPrefix();

            if (equalStrings(testPrefix, nodePrefix)) {
                return true;
            }
            String testNS = null;
            if (testPrefix != null) {
                testNS = parent.getNamespaceURI(testPrefix);
            }
            String nodeNS = null;
            if (nodePrefix != null) {
                nodeNS = parent.getNamespaceURI(nodePrefix);
            }
            return equalStrings(testNS, nodeNS);
        }
        return false;
    }

    private static boolean equalStrings(String s1, String s2) {
        return s1 == s2 || s1 != null && s1.equals(s2);
    }

    private Attr getAttribute(Element element, QName name) {
        String testPrefix = name.getPrefix();
        String testNS = null;

        if (testPrefix != null) {
            testNS = parent.getNamespaceResolver().getNamespaceURI(testPrefix);
        }

        if (testNS != null) {
            Attr attr = element.getAttributeNodeNS(testNS, name.getName());
            if (attr != null) {
                return attr;
            }

            // This may mean that the parser does not support NS for
            // attributes, example - the version of Crimson bundled
            // with JDK 1.4.0
            NamedNodeMap nnm = element.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                attr = (Attr) nnm.item(i);
                if (testAttr(attr)) {
                    return attr;
                }
            }
            return null;
        }
        return element.getAttributeNode(name.getName());
    }

    public NodePointer getNodePointer() {
        if (position == 0) {
            if (!setPosition(1)) {
                return null;
            }
            position = 0;
        }
        int index = position - 1;
        if (index < 0) {
            index = 0;
        }
        return attributes.get(index);
    }

    public int getPosition() {
        return position;
    }

    public boolean setPosition(int position) {
        this.position = position;
        return position >= 1 && position <= attributes.size();
    }
}