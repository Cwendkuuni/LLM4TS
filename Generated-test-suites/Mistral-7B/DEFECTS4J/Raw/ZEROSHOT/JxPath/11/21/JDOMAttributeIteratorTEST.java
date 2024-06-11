package org.apache.commons.jxpath.ri.model.jdom.test;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link JDOMAttributeIterator}.
 */
public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element root = new Element("root");
        Element child = new Element("child");
        child.setAttribute("attr1", new QName("ns1", "attr1"));
        child.setAttribute("attr2", new QName("ns1", "attr2"));
        child.setAttribute("xml:id", new QName("xml", "id"));
        root.addContent(child);
        parentNode = new NodePointer(root);
    }

    @Test
    public void testEmptyAttributeIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testSingleAttributeIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("ns1", "attr1"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals(parentNode, next.getParent());
        assertEquals("attr1", next.getNodeName().getName());
        assertEquals("ns1", next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAllAttributesIterator() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("", "*"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals("attr1", next.getNodeName().getName());
        assertEquals("ns1", next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());

        next = iterator.next();
        assertNotNull(next);
        assertEquals("attr2", next.getNodeName().getName());
        assertEquals("ns1", next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());

        next = iterator.next();
        assertNotNull(next);
        assertEquals("xml:id", next.getNodeName().getName());
        assertNull(next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithPrefix() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("ns1", "attr1"));
        assertFalse(iterator.hasNext());

        iterator = new JDOMAttributeIterator(parentNode, new QName("ns1", "*"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals("attr1", next.getNodeName().getName());
        assertEquals("ns1", next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());

        next = iterator.next();
        assertNotNull(next);
        assertEquals("attr2", next.getNodeName().getName());
        assertEquals("ns1", next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());

        next = iterator.next();
        assertNotNull(next);
        assertEquals("xml:id", next.getNodeName().getName());
        assertNull(next.getNodeName().getNamespaceURI());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeIteratorWithNullParentNode() {
        new JDOMAttributeIterator(null, new QName("", "*"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeIteratorWithNullQName() {
        new JDOMAttributeIterator(parentNode, null);
    }
}