package org.apache.commons.jxpath.ri.model.dom;

import org.junit.Test;
import org.junit.Assert.*;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;

public class DOMAttributeIteratorTest {

    private Document createDocument() {
        // Document creation logic
    }

    private Attr createAttribute(Element element, String prefix, String localName) {
        // Attribute creation logic
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // Tests for DOMAttributeIterator constructor

    // ... (Expert 1's test cases)

    // Tests for getNodePointer() method

    // ... (Expert 1's test cases)

    // Tests for setPosition(int position) method

    // ... (Expert 1's test cases)

    // Tests for testAttr(Attr attr, QName testName) method

    // ... (Expert 1's test cases)

    // Tests for equalStrings(String s1, String s2) method

    // ... (Expert 1's test cases)

    // Tests for getAttribute(Element element, QName name) method

    // ... (Expert 1's test cases)

    @Test
    public void testDOMAttributeIteratorEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(null), new QName("", ""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorIteratorHasNext() {
        Document doc = createDocument();
        Element root = doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(root), new QName("", ""));
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorIteratorNoMoreElements() {
        Document doc = createDocument();
        Element root = doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(root), new QName("", ""));
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testDOMAttributeIteratorNext() throws Exception {
        Document doc = createDocument();
        Element root = doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(root), new QName("", ""));
        Attr expectedAttr = (Attr) root.getAttributes().getNamedItem("testAttr");
        Attr attr = (Attr) iterator.next().getNode();
        assertEquals(expectedAttr, attr);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDOMAttributeIteratorNextNoSuchElement() throws Exception {
        Document doc = createDocument();
        Element root = doc.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(root), new QName("", ""));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();

        iterator.next();
    }
}