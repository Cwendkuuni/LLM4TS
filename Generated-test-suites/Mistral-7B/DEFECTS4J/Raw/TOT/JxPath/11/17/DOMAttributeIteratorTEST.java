package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeIterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    private Document doc;
    private Element rootElement;

    @Before
    public void setUp() {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        rootElement = doc.createElement("root");
        doc.appendChild(rootElement);
    }

    @After
    public void tearDown() {
        doc = null;
        rootElement = null;
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc), new QName("*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorWithSingleAttribute() {
        rootElement.setAttribute("test", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("*"));
        assertTrue(iterator.hasNext());
        Attr expectedAttr = (Attr) iterator.next();
        assertEquals("test", expectedAttr.getName().getNodeValue());
        assertEquals("testValue", expectedAttr.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithMultipleAttributes() {
        rootElement.setAttribute("test1", "testValue1");
        rootElement.setAttribute("test2", "testValue2");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("*"));
        assertTrue(iterator.hasNext());
        Attr expectedAttr1 = (Attr) iterator.next();
        assertEquals("test1", expectedAttr1.getName().getNodeValue());
        assertEquals("testValue1", expectedAttr1.getValue());

        assertTrue(iterator.hasNext());
        Attr expectedAttr2 = (Attr) iterator.next();
        assertEquals("test2", expectedAttr2.getName().getNodeValue());
        assertEquals("testValue2", expectedAttr2.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithPrefixName() {
        doc.setXmlNamespaceAware(true);
        rootElement.setAttributeNS("http://example.com", "test", "testValue");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(rootElement), new QName("http://example.com:test"));
        assertTrue(iterator.hasNext());
        Attr expectedAttr = (Attr) iterator.next();
        assertEquals("test", expectedAttr.getName().getNodeValue());
        assertEquals("testValue", expectedAttr.getValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithInvalidQNamePrefix() {
        new DOMAttributeIterator(new DOMPointer(rootElement), new QName(":test"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullParent() {
        new DOMAttributeIterator(null, new QName("*"));
    }
}