package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_singleAttribute() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test");
        Attr attribute = newMockAttr("test", "value");
        root.setAttributeNode(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer expected = new DOMAttributePointer(parent, attribute);
        assertSame(expected, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_noMatchingAttribute() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_multipleAttributes() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test");
        Attr attr1 = newMockAttr("test1", "value1");
        Attr attr2 = newMockAttr("test", "value2");
        Attr attr3 = newMockAttr("test3", "value3");
        root.setAttributeNode(attr1);
        root.setAttributeNode(attr2);
        root.setAttributeNode(attr3);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        List<DOMAttributePointer> expected = new ArrayList<>();
        expected.add(new DOMAttributePointer(parent, attr2));
        for (int i = 1; i < iterator.size(); i++) {
            DOMAttributePointer next = iterator.next();
            expected.add(next);
        }
        for (int i = 0; i < iterator.size(); i++) {
            DOMAttributePointer actual = iterator.getNodePointer();
            DOMAttributePointer expectedAttr = expected.get(i);
            assertSame(expectedAttr, actual);
            assertEquals(i + 1, iterator.getPosition());
            iterator.next();
        }
        assertNull(iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_nextAttribute() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test");
        Attr attr1 = newMockAttr("test1", "value1");
        Attr attr2 = newMockAttr("test", "value2");
        Attr attr3 = newMockAttr("test3", "value3");
        root.setAttributeNode(attr1);
        root.setAttributeNode(attr2);
        root.setAttributeNode(attr3);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer expected = new DOMAttributePointer(parent, attr2);
        assertSame(expected, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
        DOMAttributePointer next = iterator.next();
        // The next attribute should be the third one
        DOMAttributePointer expectedNext = new DOMAttributePointer(parent, attr3);
        assertSame(expectedNext, next);
        assertEquals(2, iterator.getPosition());
        next = iterator.next();
        assertNull(next);
        assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_noMoreAttributes() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test");
        Attr attr1 = newMockAttr("test1", "value1");
        Attr attr2 = newMockAttr("test", "value2");
        root.setAttributeNode(attr1);
        root.setAttributeNode(attr2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        assertNull(iterator.getNodePointer());
        assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_wrongPrefix() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("http://test.com/test");
        Attr attr = newMockAttr("test", "value");
        root.setAttributeNode(attr);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_wrongLocalName() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("test", "test1");
        Attr attr = newMockAttr("test", "value");
        root.setAttributeNode(attr);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_namespaceURI() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("http://test.com/test");
        Attr attr = newMockAttr("test", "value");
        root.setNamespaceURI("http://test.com");
        root.setAttributeNode(attr);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer expected = new DOMAttributePointer(parent, attr);
        assertSame(expected, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testDOMAttributeIterator_namespaceURI_noMatch() {
        Element root = newMockElement();
        NodePointer parent = new DOMNodePointer(root);
        QName name = new QName("http://test.com/test");
        Attr attr = newMockAttr("test", "value");
        root.setAttributeNode(attr);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    private Element newMockElement() {
        Element element = new MockElement() {
            @Override
            public Node getNode() {
                return new MockNode() {};
            }
        };
        return element;
    }

    private Attr newMockAttr(String localName, String value) {
        Attr attr = new MockAttr() {
            @Override
            public String getNodeValue() {
                return value;
            }

            @Override
            public String getNamespaceURI() {
                return "";
            }

            @Override
            public String getLocalName() {
                return localName;
            }
        };
        return attr;
    }
}

class MockElement extends Element {
    @Override
    public Node getNode() {
        return null;
    }
}

class MockAttr extends Attr {
    @Override
    public String getNodeValue() {
        return "";
    }

    @Override
    public String getNamespaceURI() {
        return "";
    }

    @Override
    public String getLocalName() {
        return "";
    }
}

class MockNode extends Node {
}