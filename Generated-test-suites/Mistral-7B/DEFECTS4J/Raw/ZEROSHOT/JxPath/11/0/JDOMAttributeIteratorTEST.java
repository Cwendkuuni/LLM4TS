package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    private Element rootElement;
    private NodePointer rootNodePointer;

    @Before
    public void setUp() {
        rootElement = new Element("root");
        rootElement.addAttribute("attr1", new QName("ns1", "attr1"));
        rootElement.addAttribute("attr2", new QName("ns2", "attr2"));
        rootElement.addNamespace("ns1", "http://ns1.com");
        rootElement.addNamespace("ns2", "http://ns2.com");
        rootNodePointer = new NodePointer(rootElement);
    }

    @Test
    public void testEmptyIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(null), null);
        assertTrue(iterator.isEmpty());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("ns1", "attr1"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(rootNodePointer, next.getParent());
        Attribute attribute = (Attribute) next.getNode();
        assertEquals("attr1", attribute.getName());
        assertEquals("ns1", attribute.getNamespace().getPrefix());
        assertEquals("value1", attribute.getValue());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.isEmpty());
    }

    @Test
    public void testMultipleAttributeIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("*", "*"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(rootNodePointer, next.getParent());
        Attribute attribute1 = (Attribute) next.getNode();
        assertEquals("attr1", attribute1.getName());
        assertEquals("ns1", attribute1.getNamespace().getPrefix());
        assertEquals("value1", attribute1.getValue());
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(rootNodePointer, next.getParent());
        Attribute attribute2 = (Attribute) next.getNode();
        assertEquals("attr2", attribute2.getName());
        assertEquals("ns2", attribute2.getNamespace().getPrefix());
        assertEquals("value2", attribute2.getValue());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.isEmpty());
    }

    @Test
    public void testIteratorPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("*", "*"));
        List<Integer> positions = new ArrayList<>();
        // Iterate through all attributes
        while (iterator.hasNext()) {
            iterator.next();
            positions.add(iterator.getPosition());
        }
        assertEquals(2, positions.size());
        assertEquals(1, positions.get(0));
        assertEquals(2, positions.get(1));
        // Try to iterate through all attributes again
        iterator = new JDOMAttributeIterator(rootNodePointer, new QName("*", "*"));
        int position = 0;
        while (iterator.hasNext()) {
            NodePointer next = iterator.next();
            assertEquals(position + 1, iterator.getPosition());
            position++;
        }
    }

    @Test
    public void testIteratorSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("*", "*"));
        List<Integer> positions = new ArrayList<>();
        // Iterate through all attributes
        while (iterator.hasNext()) {
            iterator.next();
            positions.add(iterator.getPosition());
        }
        // Set position to first attribute
        assertEquals(1, iterator.getPosition());
        iterator.setPosition(1);
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(rootNodePointer, next.getParent());
        Attribute attribute = (Attribute) next.getNode();
        assertEquals("attr1", attribute.getName());
        assertEquals("ns1", attribute.getNamespace().getPrefix());
        assertEquals("value1", attribute.getValue());
        // Set position to last attribute
        iterator.setPosition(2);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(rootNodePointer, next.getParent());
        attribute = (Attribute) next.getNode();
        assertEquals("attr2", attribute.getName());
        assertEquals("ns2", attribute.getNamespace().getPrefix());
        assertEquals("value2", attribute.getValue());
        // Set position to invalid index
        assertFalse(iterator.setPosition(-1));
        assertEquals(2, iterator.getPosition());
        // Try to set position after last attribute
        assertFalse(iterator.setPosition(3));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testEmptyQuery() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(rootNodePointer, new QName("ns1", "nonExistentAttr"));
        assertTrue(iterator.isEmpty());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNullParent() {
        assertThrows(NullPointerException.class, () -> new JDOMAttributeIterator(null, new QName("*", "*")));
    }

    @Test
    public void testNullName() {
        assertThrows(NullPointerException.class, () -> new JDOMAttributeIterator(rootNodePointer, null));
    }
}