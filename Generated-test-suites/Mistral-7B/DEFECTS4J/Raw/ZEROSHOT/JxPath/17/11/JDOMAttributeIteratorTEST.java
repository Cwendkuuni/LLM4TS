package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * JDOMAttributeIteratorTest class to test the JDOMAttributeIterator class.
 */
public class JDOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @org.junit.Test
    public void testSingleAttributeIterator() {
        String nsUri = "http://example.com";
        String nsPrefix = "ex";
        String attrName = "test";

        Element element = new Element("root", new QName("root", nsUri, nsPrefix));
        Attribute attribute = new Attribute("test", "value", new QName("test", nsUri, nsPrefix));
        element.addAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(attrName, nsPrefix));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next.getNode(), attribute);
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());

        iterator.setPosition(0);
        next = iterator.next();
        assertSame(next.getNode(), attribute);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @org.junit.Test
    public void testMultipleAttributesIterator() {
        String nsUri = "http://example.com";
        String nsPrefix = "ex";

        Element element = new Element("root", new QName("root", nsUri, nsPrefix));
        Attribute attr1 = new Attribute("test1", "value1", new QName("test1", nsUri, nsPrefix));
        Attribute attr2 = new Attribute("test2", "value2", new QName("test2", nsUri, nsPrefix));
        Attribute attr3 = new Attribute("test3", "value3", new QName("test3", nsUri, nsPrefix));
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next.getNode(), attr1);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next.getNode(), attr2);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next.getNode(), attr3);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());

        iterator.setPosition(2);
        next = iterator.next();
        assertSame(next.getNode(), attr3);
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(1);
        next = iterator.next();
        assertSame(next.getNode(), attr2);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(0);
        next = iterator.next();
        assertSame(next.getNode(), attr1);
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(3);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @org.junit.Test
    public void testIteratorWithWildcardNamespacePrefix() {
        String nsUri = "http://example.com";
        String nsPrefix = "ex";

        Element element = new Element("root", new QName("root"));
        Attribute attr1 = new Attribute("test1", "value1", new QName("test1"));
        Attribute attr2 = new Attribute("test2", "value2", new QName("test2", nsUri, nsPrefix));
        Attribute attr3 = new Attribute("test3", "value3", new QName("test3", nsUri, null));
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next.getNode(), attr1);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next.getNode(), attr2);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next.getNode(), attr3);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @org.junit.Test
    public void testIteratorWithSpecificNamespacePrefix() {
        String nsUri = "http://example.com";
        String nsPrefix = "ex";

        Element element = new Element("root", new QName("root"));
        Attribute attr1 = new Attribute("test1", "value1", new QName("test1"));
        Attribute attr2 = new Attribute("test2", "value2", new QName("test2", nsUri, nsPrefix));
        Attribute attr3 = new Attribute("test3", "value3", new QName("test3", nsUri, nsPrefix));
        Attribute attr4 = new Attribute("test4", "value4", new QName("test4", "http://example2.com", nsPrefix));
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);
        element.addAttribute(attr4);

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("test", nsPrefix));

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());

        iterator = new JDOMAttributeIterator(parent, new QName("test", null));

        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next.getNode(), attr1);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next.getNode(), attr2);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next.getNode(), attr3);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());

        iterator.setPosition(2);
        next = iterator.next();
        assertSame(next.getNode(), attr3);
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(1);
        next = iterator.next();
        assertSame(next.getNode(), attr2);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(3);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }

    @org.junit.Test
    public void testIteratorWithEmptyList() {
        String nsUri = "http://example.com";
        String nsPrefix = "ex";

        Element element = new Element("root", new QName("root"));
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("test", nsPrefix));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.previous());
    }
}