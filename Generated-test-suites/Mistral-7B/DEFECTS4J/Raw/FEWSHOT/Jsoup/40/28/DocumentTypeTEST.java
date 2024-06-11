package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "-//W3C//DTD HTML 4.01//EN";
        String baseUri = "http://example.com/";

        DocumentType html = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, html.name());
        assertEquals(publicId, html.attr("publicId"));
        assertEquals(systemId, html.attr("systemId"));
        assertEquals(baseUri, html.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("html", docType.name());
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"HTML_4_01\" \"-//W3C//DTD HTML 4.01//EN\" >";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expectedOutput = "<!DOCTYPE html >";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithAttributes() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        Attributes attributes = new Attributes();
        attributes.put("xml:lang", "en");
        docType.attributes(attributes);

        StringBuilder accum = new StringBuilder();
        docType.outerHtml(accum, 0, null);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"HTML_4_01\" \"-//W3C//DTD HTML 4.01//EN\" >";
        assertEquals(expectedOutput, accum.toString());

        accum.setLength(0);
        docType.outerHtml(accum, 0, null, true);

        String expectedOutputWithAttributes = "<!DOCTYPE html PUBLIC \"HTML_4_01\" \"-//W3C//DTD HTML 4.01//EN\" >\n<![ATTLIST html xml:lang CDATA #IMPLIED]>";
        assertEquals(expectedOutputWithAttributes, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithEmptyPublicIdAndSystemIdAndAttributes() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        Attributes attributes = new Attributes();
        attributes.put("xml:lang", "en");
        docType.attributes(attributes);

        StringBuilder accum = new StringBuilder();
        docType.outerHtml(accum, 0, null, true);

        String expectedOutput = "<!DOCTYPE html >\n<![ATTLIST html xml:lang CDATA #IMPLIED]>";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testDocumentTypeEquals() {
        Document doc1 = new Document();
        DocumentType doc1Html = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        DocumentType doc1Html2 = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        DocumentType doc2Html = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        DocumentType doc3XHtml = new DocumentType("html", null, "-//W3C//DTD XHTML 1.0//EN", "http://example.com/");

        assertEquals(doc1Html, doc1Html);
        assertEquals(doc1Html, doc1Html2);
        assertNotEquals(doc1Html, doc2Html);
        assertNotEquals(doc1Html, doc3XHtml);
    }

    @Test
    public void testDocumentTypeHashCode() {
        Document doc1 = new Document();
        DocumentType doc1Html = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        DocumentType doc1Html2 = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        DocumentType doc2Html = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");
        DocumentType doc3XHtml = new DocumentType("html", null, "-//W3C//DTD XHTML 1.0//EN", "http://example.com/");

        int hashCode1 = doc1Html.hashCode();
        int hashCode2 = doc1Html2.hashCode();
        int hashCode3 = doc1Html.hashCode();
        int hashCode4 = doc2Html.hashCode();
        int hashCode5 = doc3XHtml.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
    }

    @Test
    public void testDocumentTypeToString() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_4_01", "-//W3C//DTD HTML 4.01//EN", "http://example.com/");

        String expectedOutput = "<!DOCTYPE html PUBLIC \"HTML_4_01\" \"-//W3C//DTD HTML 4.01//EN\" >";
        assertEquals(expectedOutput, docType.toString());
    }
}