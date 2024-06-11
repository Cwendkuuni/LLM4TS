package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testCreateDOMAttributeIterator() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode()), new QName("test"));
        assertNull(iterator.getNodePointer());
        assertNotNull(iterator.getPosition());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testCreateDOMAttributeIteratorWithPrefix() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode(), "http://test"), new QName("test", "prefix"));
        assertNull(iterator.getNodePointer());
        assertNotNull(iterator.getPosition());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testCreateDOMAttributeIteratorWithName() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode()), new QName("test"));
        assertNull(iterator.getNodePointer());
        assertNotNull(iterator.getPosition());
        assertEquals(0, iterator.getPosition());

        Attr attr = new MockAttr();
        element.setAttributeNode(attr);
        iterator.next();
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertSame(attr, nodePointer.getNode());
        assertEquals("test", nodePointer.getName().getLocalPart());
        assertEquals("prefix", nodePointer.getName().getPrefix());
    }

    @Test
    public void testCreateDOMAttributeIteratorWithWildcardName() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode()), new QName("*"));
        assertNull(iterator.getNodePointer());
        assertNotNull(iterator.getPosition());
        assertEquals(0, iterator.getPosition());

        Attr attr1 = new MockAttr("test1");
        attr1.setPrefix("prefix1");
        element.setAttributeNode(attr1);

        Attr attr2 = new MockAttr("test2");
        attr2.setPrefix("prefix2");
        attr2.setNamespaceURI("http://test2");
        element.setAttributeNodeNS(attr2, "test2");

        iterator.next();
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertSame(attr1, nodePointer.getNode());

        iterator.next();
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertSame(attr2, nodePointer.getNode());
    }

    @Test
    public void testSetPosition() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode()), new QName("test"));
        iterator.next();
        iterator.next();

        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertSame(element.getAttributeNode("test2"), nodePointer.getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionOutOfBounds() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode()), new QName("test"));
        iterator.next();

        iterator.setPosition(2);
    }

    @Test
    public void testIterateAllAttributes() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMParentPointer(new MockNode()), new QName("test"));

        Attr attr1 = new MockAttr("test1");
        attr1.setPrefix("prefix1");
        element.setAttributeNode(attr1);

        Attr attr2 = new MockAttr("test2");
        attr2.setPrefix("prefix2");
        attr2.setNamespaceURI("http://test2");
        element.setAttributeNodeNS(attr2, "test2");

        iterator.next();
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertSame(attr1, nodePointer.getNode());

        iterator.next();
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertSame(attr2, nodePointer.getNode());

        iterator.next();
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testIterateEmptyElement() {
        Element element = new MockElement();
        DOMAttributeIterator iterator = new DOMAttribeIterator(new DOMParentPointer(new MockNode()), new QName("test"));
        assertNull(iterator.getNodePointer());
        assertNotNull(iterator.getPosition());
        assertEquals(0, iterator.getPosition());
    }

    private static class MockAttr extends Attr {
        private MockAttr(String name) {
            super();
            setName(name);
        }
    }

    private static class MockElement extends Element {
        @Override
        public Attr getAttributeNode(String name) {
            return null;
        }

        @Override
        public Attr getAttributeNodeNS(String namespaceURI, String localName) {
            return null;
        }

        @Override
        public int getAttributeNodeCount() {
            return 0;
        }
    }

    private static class DOMParentPointer implements NodePointer {
        private Node node;
        private String prefix;

        public DOMParentPointer(Node node) {
            this.node = node;
        }

        public DOMParentPointer(Node node, String prefix) {
            this.node = node;
            this.prefix = prefix;
        }

        @Override
        public Node getNode() {
            return node;
        }

        public String getNamespaceURI(String prefix) {
            return null;
        }
    }

}