package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Test class for DOMAttributeIterator.
 */
public class DOMAttributeIteratorTest {

    private NodePointer parentNode;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("root");
        parentNode = new NodePointer(element);

        Attr attr1 = element.setAttributeNode(new Attr("data1", "value1"));
        Attr attr2 = element.setAttributeNode(new Attr("data2", "value2"));
        Attr attr3 = element.setAttributeNode(new Attr("data3", "value3"));
        Attr attr4 = element.setAttributeNode(new Attr("xmlns:x", "http://example.com"));
        Attr attr5 = element.setAttributeNode(new Attr("xmlns:y", "http://example.com"));
        Attr attr6 = element.setAttributeNode(new Attr("data4", "value4"));

        iterator = new DOMAttributeIterator(parentNode, null);
        iterator.addAll(new ArrayList<Attr>() {{
            add(attr1);
            add(attr2);
            add(attr3);
            add(attr4);
            add(attr5);
            add(attr6);
        }});
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentNode, null);
        assertTrue(iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertEquals(3, iterator.getPosition());
        iterator.next();
        assertEquals(4, iterator.getPosition());
        iterator.next();
        assertEquals(5, iterator.getPosition());
        iterator.next();
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        iterator.next();
        assertTrue(iterator.setPosition(1));
        assertEquals(2, iterator.getPosition());
        assertTrue(iterator.setPosition(5));
        assertEquals(5, iterator.getPosition());
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(6));
    }

    @Test
    public void testIteratorBehavior() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals("value1", nodePointer.getNodeValue());
        nodePointer = iterator.next();
        assertEquals("value2", nodePointer.getNodeValue());
        nodePointer = iterator.next();
        assertEquals("value3", nodePointer.getNodeValue());
        nodePointer = iterator.next();
        assertEquals("http://example.com", nodePointer.getNodeValue().getNodeValue());
        nodePointer = iterator.next();
        assertEquals("http://example.com", nodePointer.getNodeValue().getNodeValue());
        nodePointer = iterator.next();
        assertNull(nodePointer.getNodeValue());
    }

    @Test
    public void testTestAttr() {
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("*, data1")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("*", null, "data1")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", null)));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "root")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", "*")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "*")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "*", "http://example.com")));
        assertTrue(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "*", "http://example.com", "*")));

        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("data2")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("xmlns:x")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("xmlns:y")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "xmlns")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "xmlns:z")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "*", "http://example.com", "xmlns")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("data1", "", "xmlns")));
        assertFalse(DOMAttributeIterator.testAttr(attr1, new QName("data1", null, "*", "", "xmlns")));
    }

    @Test
    public void testGetAttribute() {
        QName name = new QName("data1");
        Attr attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr1, attr);

        name = new QName("xmlns:x");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertNull(attr);

        name = new QName("data2");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr2, attr);

        name = new QName("*", null, "xmlns");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr4, attr);

        name = new QName("*", null, "xmlns:z");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertNull(attr);

        name = new QName("data3");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr3, attr);

        name = new QName("data4");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", null);
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", null, "");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", null, "root");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", "*");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", null, "*");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", null, "*", "http://example.com");
        attr = DOMAttribeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data4", null, "*", "http://example.com", "*");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertSame(attr6, attr);

        name = new QName("data5");
        attr = DOMAttributeIterator.getAttribute(element, name);
        assertNull(attr);
    }
}