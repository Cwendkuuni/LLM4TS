package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testCreateDocumentType() {
        String name = "html";
        String publicId = "--//W3C//DTD XHTML 1.0 Strict//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Validate.assertEquals(name, documentType.attr("name"));
        Validate.assertEquals(publicId, documentType.attr("publicId"));
        Validate.assertEquals(systemId, documentType.attr("systemId"));
        Validate.assertEquals(baseUri, documentType.baseUri());
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentTypeEmptyName() {
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        new DocumentType(null, publicId, systemId, baseUri);
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "publicId", null, null);
        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, "systemId", null);
        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM \"systemId\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();

        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\"", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        documentType.outerHtmlTail(null, 0, null);

        // no output
    }

    @Test
    public void testOuterHtmlWithBaseUri() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, null, "http://example.com");
        Element html = new Element("html");

        html.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
        document.outerHtml(accum);

        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<!DOCTYPE html\n<!DOCTYPE html PUBLIC \"\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"\n>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", null, null);
        Element html = new Element("html");

        html.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
        document.outerHtml(accum);

        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<!DOCTYPE html PUBLIC \"publicId\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"\n>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, "systemId", null);
        Element html = new Element("html");

        html.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
        document.outerHtml(accum);

        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<!DOCTYPE html SYSTEM \"systemId\"\n>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        Element html = new Element("html");

        html.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        document.outputSettings().syntax(Document.OutputSettings.Syntax.XML);
        document.outerHtml(accum);

        assertEquals("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\"\n>", accum.toString());
    }

    @Test
    public void testAttrs() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertNull(documentType.attr("name"));

        documentType.attr("name", "html5");

        assertEquals("html5", documentType.attr("name"));
    }
}