package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    private Element rootElement;

    @Before
    public void setUp() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("root");
        rootElement = root;
        root.setAttribute("attr1", "value1");
        root.setAttribute("attr2", "value2");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:x", "http://www.w3.org/2000/xmlns/xhtml");
        root.setAttributeNS("http://www.w3.org/2000/xmlns/xhtml", "xml:lang", "en");
        doc.appendChild(root);
        DocumentType doctype = doc.createDocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doc.insertBefore(doctype, root);
        this.rootElement = root;
    }

    @Test
    public void testDOMAttributeIterator_SingleAttribute() {
        QName attrName = new QName("", "attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(rootElement), attrName);
        Attr attr = iterator.getNodePointer().getNode();
        assertEquals("value1", attr.getValue());
    }

    @Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        QName attrName = new QName("", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(rootElement), attrName);
        Attr attr1 = iterator.next().getNode();
        assertEquals("value1", attr1.getValue());
        Attr attr2 = iterator.next().getNode();
        assertEquals("value2", attr2.getValue());
        assertNull(iterator.next().getNode());
    }

    @Test
    public void testDOMAttributeIterator_NamespaceAware() {
        QName attrName = new QName("http://www.w3.org/2000/xmlns/xhtml", "xml:lang");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(rootElement), attrName);
        Attr attr = iterator.next().getNode();
        assertEquals("en", attr.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDOMAttributeIterator_EmptyIterator() {
        QName attrName = new QName("", "");
        new DOMAttributeIterator(new DOMPoint(rootElement), attrName);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDOMAttributeIterator_OutOfBounds() {
        QName attrName = new QName("", "attr1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(rootElement), attrName);
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }
}