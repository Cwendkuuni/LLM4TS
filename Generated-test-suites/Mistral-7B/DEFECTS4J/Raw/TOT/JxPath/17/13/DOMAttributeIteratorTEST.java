package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

    private Document doc;
    private Element rootElement;

    @Before
    public void setUp() {
        doc = DOMConfiguration.getDocumentBuilder().newDocument();
        rootElement = doc.createElement("root");
        doc.appendChild(rootElement);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc), new QName("", "*"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttributeIterator() {
        Attr attribute = doc.createAttribute("test:attribute");
        attribute.setNodeValue("testValue");
        rootElement.setAttributeNode(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("test", "attribute"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertInstanceOf(Attr.class, iterator.getNodePointer().getNode());
        assertEquals("testValue", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesIterator() {
        Attr attribute1 = doc.createAttribute("test:attribute1");
        attribute1.setNodeValue("testValue1");
        rootElement.setAttributeNode(attribute1);

        Attr attribute2 = doc.createAttribute("test:attribute2");
        attribute2.setNodeValue("testValue2");
        rootElement.setAttributeNode(attribute2);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("test", "*"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertInstanceOf(Attr.class, iterator.getNodePointer().getNode());
        assertEquals("testValue1", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertInstanceOf(Attr.class, iterator.getNodePointer().getNode());
        assertEquals("testValue2", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyElementIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("", "*"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithPrefix() {
        Attr attribute = doc.createAttribute("xmlns:test:attribute");
        attribute.setNodeValue("testValue");
        rootElement.setAttributeNode(attribute);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("xmlns:test", "attribute"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertInstanceOf(Attr.class, iterator.getNodePointer().getNode());
        assertEquals("testValue", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithNamespaceURI() {
        String nsUri = "http://test.com";
        Attr attribute = doc.createAttributeNS(nsUri, "test:attribute");
        attribute.setNodeValue("testValue");
        rootElement.setAttributeNodeNS(attribute, nsUri);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName(null, "test:attribute"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertInstanceOf(Attr.class, iterator.getNodePointer().getNode());
        assertEquals("testValue", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
        assertFalse(

            iterator.hasNext());
    }

    @Test
    public void testAttributeIteratorWithEmptyNamespaceURI() {
        String nsUri = "";
        Attr attribute = doc.createAttributeNS(nsUri, "test:attribute");
        attribute.setNodeValue("testValue");
        rootElement.setAttributeNodeNS(attribute, nsUri);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("", "test:attribute"));
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.getNodePointer().getNode());
        assertInstanceOf(Attr.class, iterator.getNodePointer().getNode());
        assertEquals("testValue", ((Attr) iterator.getNodePointer().getNode()).getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorWithNonExistingAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("test", "nonexistent:attribute"));
        iterator.next();
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorWithNullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), new QName("test", "attribute"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullQName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), null);
    }
}