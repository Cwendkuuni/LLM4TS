package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.attributes.Attribute;
import org.jsoup.nodes.attributes.Attributes;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", "html", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(null, documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.children());
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, "html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(null, documentType.attr(DocumentType.NAME));
        assertEquals("html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", documentType.attr(DocumentType.SYSTEM_ID) + " " + documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.attr(DocumentType.SYSTEM_ID));
        assertNull(documentType.children());
    }

    @Test
    public void testHasAttribute() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, null, null);
        documentType.attr(DocumentType.NAME, "html");
        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/html4/strict.dtd");

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
        assertFalse(documentType.has("non-existent-attribute"));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", "html", "http://www.w3.org/TR/html4/strict.dtd");
        Appendable appendable = new StringBuilder();

        documentType.outerHtmlHead(appendable, 0, null);

        assertEquals("<!DOCTYPE html>\n", appendable.toString());
    }

    @Test
    public void testOuterHtmlHeadWithHtml5() throws IOException {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", "html", null);
        Appendable appendable = new StringBuilder();

        documentType.outerHtmlHead(appendable, 0, null);

        assertEquals("<!doctype html>\n", appendable.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() throws IOException {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, "html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        Appendable appendable = new StringBuilder();

        documentType.outerHtmlHead(appendable, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n", appendable.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, null, null);

        Appendable appendable = new StringBuilder();
        documentType.outerHtml(appendable, 0, null);

        assertEquals("", appendable.toString());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testAttribute() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, null, null);

        documentType.attr(DocumentType.NAME, "html");
        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/html4/strict.dtd");

        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testAttributes() {
        Document doc = new Document();
        DocumentType documentType = new DocumentType(doc, "#doctype", null, "html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");

        Attributes attributes = documentType.attributes();

        assertEquals(2, attributes.size());
        Attribute publicIdAttribute = attributes.get("xml:public");
        assertEquals("PUBLIC", publicIdAttribute.getValue());
        Attribute systemIdAttribute = attributes.get("xml:system");
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", systemIdAttribute.getValue());
    }
}