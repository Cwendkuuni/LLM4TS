package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class DOMAttributeIteratorTest {

    private Element element;
    private QName name;
    private NodePointer parent;
    private DOMAttributeIterator iterator;
    private List<Attr> attributes;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = new QName("test", "test:attribute");
        element = mock(Element.class);
        attributes = new ArrayList<>();

        when(element.getAttributes()).thenReturn(createMockNamedNodeMap(attributes.toArray(new Attr[0])));
        when(parent.getNode()).thenReturn(element);
    }

    @Test
    public void testCreateDOMAttributeIteratorWithEmptyElement() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
        assertNull(iterator.getPosition());
        assertNull(iterator.setPosition(1));
    }

    @Test
    public void testCreateDOMAttributeIteratorWithEmptyAttrList() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertNull(iterator.getPosition());
        assertNull(iterator.setPosition(1));
    }

    @Test
    public void testCreateDOMAttributeIteratorWithSingleAttr() {
        Attr attr = createAttr("test:attribute1", "test:value1");
        attributes.add(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(name, nodePointer.getNodeName());
        assertEquals(attr, nodePointer.getNode());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertSame(nodePointer, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testCreateDOMAttributeIteratorWithMultiAttr() {
        Attr attr1 = createAttr("test:attribute1", "test:value1");
        Attr attr2 = createAttr("test:attribute2", "test:value2");
        attributes.add(attr1);
        attributes.add(attr2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals(attr1, nodePointer1.getNode());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertSame(nodePointer1, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals(attr2, nodePointer2.getNode());
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertSame(nodePointer2, iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());

        NodePointer nodePointer3 = iterator.getNodePointer();
        assertNull(nodePointer3);
        assertEquals(-1, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testCreateDOMAttributeIteratorWithAttrNamePrefix() {
        Attr attr = createAttr("test:prefix:attribute", "test:value");
        attributes.add(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "test:prefix"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(attr, nodePointer.getNode());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertSame(nodePointer, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testCreateDOMAttributeIteratorWithAttrNameNS() {
        String testNS = "testNS";
        Attr attr = createAttr("test:attribute", "test:value");
        attributes.add(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(null, "test:attribute"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(attr, nodePointer.getNode());
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(1));
        assertSame(nodePointer, iterator.getNodePointer());
        assertEquals(1, iterator.getPosition());

        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent, new QName(testNS, "test:attribute"));
        NodePointer nodePointer2 = iterator2.getNodePointer();
        assertEquals(attr, nodePointer2.getNode());
        assertEquals(1, iterator2.getPosition());
        assertTrue(iterator2.setPosition(1));
        assertSame(nodePointer2, iterator2.getNodePointer());
        assertEquals(1, iterator2.getPosition());

        DOMAttributeIterator iterator3 = new DOMAttributeIterator(parent, new QName(null, "test:attribute1"));
        assertNull(iterator3.getNodePointer());
        assertEquals(-1, iterator3.getPosition());
        assertFalse(iterator3.setPosition(1));
    }

    private Attr createAttr(String localName, String value) {
        Attr attr = mock(Attr.class);
        when(attr.getName()).thenReturn(createQName(localName, null).getElementName());
        when(attr.getValue()).thenReturn(value);
        return attr;
    }

    private NodeList createMockNamedNodeMap(Attr... attrs) {
        NamedNodeMap map = mock(NamedNodeMap.class);
        when(map.getLength()).thenReturn(attrs.length);
        for (Attr attr : attrs) {
            when(map.item(ArgumentMatchers.eq(attrs.length - 1 - indexOf(attrs, attr)))).thenReturn(attr);
        }
        return (NodeList) map;
    }

    private QName createQName(String localName, String prefix) {
        return new QName("test", prefix, localName);
    }

    private int indexOf(Attr[] attrs, Attr attr) {
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i].equals(attr)) {
                return i;
            }
        }
        return -1;
    }
}