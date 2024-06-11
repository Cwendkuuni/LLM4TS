package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMNodePointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSingleAttributeIterator() {
        String prefix = "xml";
        String localName = "attr";
        String value = "value";
        Element element = new Element("root");
        Attribute attribute = new Attribute("attr", value, Namespace.XML_NAMESPACE);
        element.addContent(attribute);
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        NodePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals(1, iterator.getPosition());
        assertSame(attribute, attributePointer.getNode());
        assertTrue(iterator.setPosition(1));
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNull(attributePointer2);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testMultipleAttributesIteratorWithPrefix() {
        String prefix = "prefix";
        String localName = "attr";
        String uri = "uri";
        String value1 = "value1";
        String value2 = "value2";
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("prefix:attr1", value1, new Namespace(prefix, uri));
        Attribute attribute2 = new Attribute("prefix:attr2", value2, new Namespace(prefix, uri));
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, localName));
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertEquals(1, iterator.getPosition());
        assertSame(attribute1, attributePointer1.getNode());
        assertTrue(iterator.setPosition(2));
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer2);
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, attributePointer2.getNode());
        assertFalse(iterator.setPosition(3));
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testMultipleAttributesIteratorWithoutPrefix() {
        String localName = "attr";
        String uri1 = "uri1";
        String uri2 = "uri2";
        String value1 = "value1";
        String value2 = "value2";
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("attr1", value1, new Namespace(null, uri1));
        Attribute attribute2 = new Attribute("attr2", value2, new Namespace(null, uri2));
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(null, localName));
        NodePointer attributePointer1 = iterator.getNodePointer();
        assertNotNull(attributePointer1);
        assertEquals(1, iterator.getPosition());
        assertSame(attribute1, attributePointer1.getNode());
        assertTrue(iterator.setPosition(2));
        NodePointer attributePointer2 = iterator.getNodePointer();
        assertNotNull(attributePointer2);
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, attributePointer2.getNode());
        assertFalse(iterator.setPosition(3));
        assertNull(iterator.getNodePointer());
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testMultipleAttributesIteratorWithWildcard() {
        String prefix = "prefix";
        String localName = "attr";
        String uri1 = "uri1";
        String uri2 = "uri2";
        String value1 = "value1";
        String value2 = "value2";
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("prefix:attr1", value1, new Namespace(prefix, uri1));
        Attribute attribute2 = new Attribute("prefix:attr2", value2, new Namespace(prefix, uri2));
        element.addContent(attribute1);
        element.addContent(attribute2);
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "*"));
        List<NodePointer> attributePointers = new ArrayList<>();
        while (iterator.hasNext()) {
            NodePointer attributePointer = iterator.getNodePointer();
            attributePointers.add(attributePointer);
            iterator.next();
        }
        assertEquals(2, attributePointers.size());
        NodePointer attributePointer1 = attributePointers.get(0);
        assertNotNull(attributePointer1);
        assertEquals(1, iterator.getPosition());
        assertSame(attribute1, attributePointer1.getNode());
        NodePointer attributePointer2 = attributePointers.get(1);
        assertNotNull(attributePointer2);
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, attributePointer2.getNode());
        assertFalse(iterator.hasNext());
    }
}