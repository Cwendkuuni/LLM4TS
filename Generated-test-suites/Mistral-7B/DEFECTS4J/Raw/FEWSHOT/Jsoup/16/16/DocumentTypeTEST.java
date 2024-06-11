package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML_TLS_4.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertNotNull(docType);
        assertEquals("html", docType.nodeName());
        assertEquals("HTML_TLS_4.0", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
        assertTrue(docType.children().isEmpty());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_TLS_4.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_TLS_4.0\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_TLS_4.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeWithPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_TLS_4.0_Public", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://mywebsite.com/");
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_TLS_4.0_Public\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", null);
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeWithNullPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, null, null);
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeWithAttributes() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_TLS_4.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Attributes attrs = new Attributes();
        attrs.put("xml:lang", "en");
        attrs.put("xml:version", "1.0");
        docType.setAttributes(attrs);
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_TLS_4.0\" \"http://www.w3.org/TR/REC-html40/strict.dtd\" xml:lang=\"en\" xml:version=\"1.0\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML_TLS_4.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Attributes attrs = new Attributes();
        docType.setAttributes(attrs);
        Element docTypeElement = doc.parseBodyElement("!DOCTYPE");
        docTypeElement.children().add(docType);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }
}