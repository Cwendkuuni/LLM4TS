package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testCreateDocumentType() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        assertNotNull(docType);
        assertEquals("html", docType.nodeName());
        assertEquals("HTML Public ID", docType.attr("publicId"));
        assertEquals("HTML System ID", docType.attr("systemId"));
        assertNull(docType.children());
    }

    @Test
    public void testEmptyDocumentType() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertNotNull(docType);
        assertEquals("html", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.children());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\" HTML System ID", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\" HTML System ID", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType docType = new DocumentType("html", null, null, null);
        docType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testCreateDocumentTypeWithChildren() {
        Element root = new Element("html");
        TextNode textNode = new TextNode("Hello World!");
        root.appendChild(textNode);
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        docType.appendChild(root);
        assertNotNull(docType.children());
        assertEquals(1, docType.childNodes().size());
        assertEquals("html", docType.nodeName());
    }
}