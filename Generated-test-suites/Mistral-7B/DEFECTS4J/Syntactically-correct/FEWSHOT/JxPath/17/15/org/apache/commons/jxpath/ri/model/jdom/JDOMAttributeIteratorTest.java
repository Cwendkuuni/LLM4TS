package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_emptyList() {
        NodePointer parent = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testJDOMAttributeIterator_singleAttribute() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parent = new JDOMPointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new JDOMAttributePointer(parent, new Attribute("attr1", "value1")));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_multipleAttributes() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));
        element.addAttribute(new Attribute("attr2", "value2"));
        NodePointer parent = new JDOMPointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        assertTrue(iterator.hasNext());
        NodePointer next1 = iterator.next();
        assertSame(next1, new JDOMAttributePointer(parent, new Attribute("attr1", "value1")));
        assertTrue(iterator.hasNext());
        NodePointer next2 = iterator.next();
        assertSame(next2, new JDOMAttributePointer(parent, new Attribute("attr2", "value2")));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_mixedNamespaces() {
        Element element = new Element("root");
        element.addNamespaceDeclaration("ns1", "http://test.com/ns1");
        element.addNamespaceDeclaration("ns2", "http://test.com/ns2");
        element.addAttribute(new Attribute("ns1:attr1", "value1"));
        element.addAttribute(new Attribute("ns2:attr2", "value2"));

        NodePointer parent = new JDOMPointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns1", "attr1"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new JDOMAttributePointer(parent, new Attribute("ns1:attr1", "value1")));
        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parent, new QName("ns2", "attr2"));
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertSame(next, new JDOMAttributePointer(parent, new Attribute("ns2:attr2", "value2")));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_mixedNamespaces_emptyNamespace() {
        Element element = new Element("root");
        element.addNamespaceDeclaration("ns1", "http://test.com/ns1");
        element.addNamespaceDeclaration("", "http://test.com/ns2");
        element.addAttribute(new Attribute("ns1:attr1", "value1"));
        element.addAttribute(new Attribute("", "attr2", "http://test.com/ns2"));

        NodePointer parent = new JDOMPointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr2"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertSame(next, new JDOMAttributePointer(parent, new Attribute("", "attr2", "http://test.com/ns2")));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIterator_returnNullOnInvalidPosition() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("attr1", "value1"));
        NodePointer parent = new JDOMPointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));
        iterator.setPosition(10);
        assertNull(iterator.next());
    }
}