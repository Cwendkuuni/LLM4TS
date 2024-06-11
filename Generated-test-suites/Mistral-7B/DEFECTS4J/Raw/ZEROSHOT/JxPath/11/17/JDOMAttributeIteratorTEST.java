package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyIterator() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testSingleAttributeIterator() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "target";
        String value = "value";

        QName name = new QName(xmlns, prefix, localName);

        Element element = new Element("root");
        Attribute attribute = new Attribute(localName, value, new Namespace(xmlns, prefix));
        element.setAttribute(attribute);

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(1, iterator.getPosition());
        assertSame(attribute, iterator.next());
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testMultipleAttributesIterator() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix1 = "xml1";
        String prefix2 = "xml2";
        String localName = "target";
        String value1 = "value1";
        String value2 = "value2";

        QName name1 = new QName(xmlns, prefix1, localName);
        QName name2 = new QName(xmlns, prefix2, localName);

        Element element = new Element("root");
        Attribute attribute1 = new Attribute(localName, value1, new Namespace(xmlns, prefix1));
        Attribute attribute2 = new Attribute(localName, value2, new Namespace(xmlns, prefix2));
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(xmlns, "*", localName));

        assertEquals(1, iterator.getPosition());
        assertSame(attribute1, iterator.next());
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, iterator.next());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testEmptyAttributeListIterator() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "target";

        QName name = new QName(xmlns, prefix, localName);

        Element element = new Element("root");

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testNullParentIterator() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "target";

        QName name = new QName(xmlns, prefix, localName);

        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testNullNameIterator() {
        Element parent = new Element("root");

        NodePointer parentPointer = new JDOMPointer(parent);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, null);

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testIteratorPositionReset() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "target";

        QName name = new QName(xmlns, prefix, localName);

        Element element = new Element("root");
        Attribute attribute1 = new Attribute(localName, "value1", new Namespace(xmlns, prefix));
        Attribute attribute2 = new Attribute(localName, "value2", new Namespace(xmlns, prefix));
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        NodePointer parent = new JDOMPointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(1, iterator.getPosition());
        assertSame(attribute1, iterator.next());
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, iterator.next());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());

        iterator.setPosition(1);
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, iterator.next());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());

        iterator.setPosition(0);
        assertEquals(1, iterator.getPosition());
        assertSame(attribute1, iterator.next());
        assertEquals(2, iterator.getPosition());
        assertSame(attribute2, iterator.next());
        assertEquals(3, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }
}