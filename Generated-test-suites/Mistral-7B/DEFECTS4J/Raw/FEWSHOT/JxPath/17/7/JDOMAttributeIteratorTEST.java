package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("foo", "bar", "http://example.com"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://example.com", "foo"));
        assertTrue(iterator.hasNext());
        Attribute next = iterator.next();
        assertEquals("bar", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("foo1", "bar1", "http://example.com"));
        element.addAttribute(new Attribute("foo2", "bar2", "http://example.com"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://example.com", "*"));

        List<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            assertTrue(iterator.hasNext());
            Attribute next = iterator.next();
            attributes.add(next);
        }

        assertEquals(2, attributes.size());
        assertEquals("bar1", attributes.get(0).getValue());
        assertEquals("bar2", attributes.get(1).getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testEmptyAttributeNameIterator() {
        Element element = new Element("root");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testUnknownNamespaceAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("foo", "bar", "http://example.com"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("http://unknown", "foo"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testXmlNamespaceAttributeIterator() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("xml:lang", "en"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "lang"));
        assertTrue(iterator.hasNext());
        Attribute next = iterator.next();
        assertEquals("en", next.getValue());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}