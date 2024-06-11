package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

/**
 * Tests for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "W3C DTD XHTML 1.0 Strict", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML PUBLIC", docType.attr("publicId"));
        assertEquals("W3C DTD XHTML 1.0 Strict", docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithoutPublicId() {
        DocumentType docType = new DocumentType("html", "", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertEquals("", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithoutSystemId() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("HTML PUBLIC", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithoutPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "W3C DTD XHTML 1.0 Strict", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doc.appendChild(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML PUBLIC\" W3C DTD XHTML 1.0 Strict \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doc.appendChild(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML PUBLIC\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "W3C DTD XHTML 1.0 Strict", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doc.appendChild(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM \"W3C DTD XHTML 1.0 Strict http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", "");
        doc.appendChild(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "W3C DTD XHTML 1.0 Strict", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doc.appendChild(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testParseDocumentType() throws Exception {
        String htmlString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        Document doc = Parser.parseBodyFragment(htmlString);

        Node docTypeNode = doc.child(0);
        assertEquals(DocumentType.class, docTypeNode.getClass());
        DocumentType docType = (DocumentType) docTypeNode;

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.baseUri());

        Element html = doc.child(1);
        assertEquals("html", html.tagName());
        assertNull(html.attr("name"));
    }

    @Test
    public void testParseDocumentTypeWithoutPublicId() throws Exception {
        String htmlString = "<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        Document doc = Parser.parseBodyFragment(htmlString);

        Node docTypeNode = doc.child(0);
        assertEquals(DocumentType.class, docTypeNode.getClass());
        DocumentType docType = (DocumentType) docTypeNode;

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.baseUri());

        Element html = doc.child(1);
        assertEquals("html", html.tagName());
        assertNull(html.attr("name"));
    }

    @Test
    public void testParseDocumentTypeWithoutSystemId() throws Exception {
        String htmlString = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"";
        Document doc = Parser.parseBodyFragment(htmlString);

        Node docTypeNode = doc.child(0);
        assertEquals(DocumentType.class, docTypeNode.getClass());
        DocumentType docType = (DocumentType) docTypeNode;

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        Element html = doc.child(1);
        assertEquals("html", html.tagName());
        assertNull(html.attr("name"));
    }

    @Test
    public void testParseDocumentTypeEmpty() throws Exception {
        String htmlString = "<!DOCTYPE html>";
        Document doc = Parser.parseBodyFragment(htmlString);

        Node docTypeNode = doc.child(0);
        assertEquals(DocumentType.class, docTypeNode.getClass());
        DocumentType docType = (DocumentType) docTypeNode;

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        Element html = doc.child(1);
        assertEquals("html", html.tagName());
        assertNull(html.attr("name"));
    }
}