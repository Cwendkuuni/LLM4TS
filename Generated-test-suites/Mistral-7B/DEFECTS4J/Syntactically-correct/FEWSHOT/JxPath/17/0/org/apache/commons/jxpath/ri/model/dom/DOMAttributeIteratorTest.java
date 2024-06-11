package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testCreateIteratorWithEmptyNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), null);
        assertNull(iterator.getNodePointer());
        assertNull(iterator.getPosition());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithElementNodeAndSingleAttribute() {
        Element element = new ElementImpl("root", "root");
        Attr attribute = new AttrImpl("attr", "value", null);
        element.setAttributeNode(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("attr"));
        assertNotNull(iterator.getNodePointer());
        assertSame(attribute, iterator.getNodePointer().getNode());
        assertSame(1, iterator.getPosition());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithElementNodeAndMultipleAttributes() {
        Element element = new ElementImpl("root", "root");
        Attr attr1 = new AttrImpl("attr1", "value1", null);
        Attr attr2 = new AttrImpl("attr2", "value2", null);
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("*"));
        assertNotNull(iterator.getNodePointer());
        assertSame(attr1, iterator.next().getNode());
        assertSame(attr2, iterator.next().getNode());
        assertNull(iterator.next());
    }

    @Test
    public void testCreateIteratorWithElementNodeAndEmptyNamespace() {
        Element element = new ElementImpl("root", "root");
        Attr attr1 = new AttrImpl("xmlns:x", "http://example.com/ns");
        Attr attr2 = new AttrImpl("attr", "value", null);
        element.setAttributeNodeNS(attr1, "http://example.com/ns", "xmlns:x");
        element.setAttributeNode(attr2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        assertSame(attr2, iterator.next().getNode());
    }

    @Test
    public void testCreateIteratorWithElementNodeAndNonEmptyNamespace() {
        Element element = new ElementImpl("root", "root");
        Attr attr1 = new AttrImpl("xmlns:x", "http://example.com/ns");
        Attr attr2 = new AttrImpl("xmlns:y", "http://example.com/otherns");
        Attr attr3 = new AttrImpl("attr", "value", null);
        element.setAttributeNodeNS(attr1, "http://example.com/ns", "xmlns:x");
        element.setAttributeNodeNS(attr2, "http://example.com/otherns", "xmlns:y");
        element.setAttributeNode(attr3);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:x", "x"));
        assertNull(iterator.next());
        iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:y", "y"));
        assertNull(iterator.next());
        assertSame(attr3, iterator.next().getNode());
    }

    @Test
    public void testCreateIteratorWithElementNodeAndMixedNamespaces() {
        Element element = new ElementImpl("root", "root");
        Attr attr1 = new AttrImpl("xmlns:x", "http://example.com/ns1");
        Attr attr2 = new AttrImpl("xmlns:y", "http://example.com/ns2");
        Attr attr3 = new AttrImpl("attr", "value", null);
        element.setAttributeNodeNS(attr1, "http://example.com/ns1", "xmlns:x");
        element.setAttributeNodeNS(attr2, "http://example.com/ns2", "xmlns:y");
        element.setAttributeNode(attr3);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:x", "x"));
        assertNull(iterator.next());
        iterator = new DOMAttributeIterator(new DOMPointer(element), new QName("xmlns:y", "y"));
        assertNull(iterator.next());
        assertSame(attr3, iterator.next().getNode());
    }

    @Test
    public void testCreateIteratorWithElementNodeAndEmptyName() {
        Element element = new ElementImpl("root", "root");
        Attr attr1 = new AttrImpl("xmlns:", null);
        Attr attr2 = new AttrImpl("attr", "value", null);
        element.setAttributeNodeNS(attr1, null, null);
        element.setAttributeNode(attr2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(element), null);
        assertSame(attr2, iterator.next().getNode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorWithNullParent() {
        new DOMAttributeIterator(null, new QName("attr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateIteratorWithNullName() {
        new DOMAttributeIterator(new DOMPointer(new ElementImpl("root", "root")), null);
    }
}