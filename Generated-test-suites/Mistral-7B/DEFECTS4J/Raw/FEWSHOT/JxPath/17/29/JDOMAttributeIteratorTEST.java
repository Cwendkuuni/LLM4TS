package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyElement() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value", new Namespace("prefix", "uri"));
        element.addContent(attribute);
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("prefix", "name"));
        assertTrue(iterator.hasNext());
        Attribute next = iterator.next();
        assertEquals("value", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testMultipleAttributes() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1", new Namespace("prefix1", "uri1"));
        Attribute attr2 = new Attribute("attr2", "value2", new Namespace("prefix2", "uri2"));
        Attribute attr3 = new Attribute("attr3", "value3", new Namespace("prefix3", "uri3"));
        element.addContent(attr1);
        element.addContent(attr2);
        element.addContent(attr3);
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertTrue(iterator.hasNext());
        Attribute next = iterator.next();
        assertNull(next.getNamespace());
        assertEquals("attr1", next.getName().getName());
        assertEquals("value1", next.getValue());
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNull(next.getNamespace());
        assertEquals("attr2", next.getName().getName());
        assertEquals("value2", next.getValue());
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNull(next.getNamespace());
        assertEquals("attr3", next.getName().getName());
        assertEquals("value3", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new JDOMAttributeIterator(parent, new QName("prefix1", "attr1"));
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals("value1", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new JDOMAttributeIterator(parent, new QName("prefix2", "attr2"));
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals("value2", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new JDOMAttributeIterator(parent, new QName("prefix3", "attr3"));
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals("value3", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testEmptyNamespace() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        element.addContent(attr1);
        element.addContent(attr2);
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertTrue(iterator.hasNext());
        Attribute next = iterator.next();
        assertNull(next.getNamespace());
        assertEquals("attr1", next.getName().getName());
        assertEquals("value1", next.getValue());
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNull(next.getNamespace());
        assertEquals("attr2", next.getName().getName());
        assertEquals("value2", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testNullParent() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testNullName() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testInvalidNamespace() {
        Element element = new Element("root");
        Attribute attr = new Attribute("name", "value", new Namespace("prefix", "uri"));
        element.addContent(attr);
        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("invalid", "name"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}