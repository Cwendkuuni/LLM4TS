package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML_DOCTYPE_PUBLIC", "HTML_DOCTYPE_SYSTEM", doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("HTML_DOCTYPE_PUBLIC", doctype.attr("publicId"));
        assertEquals("HTML_DOCTYPE_SYSTEM", doctype.attr("systemId"));
        assertNull(doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithPublicId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML_DOCTYPE_PUBLIC", null, doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("HTML_DOCTYPE_PUBLIC", doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        String expected = "<!DOCTYPE html PUBLIC \"HTML_DOCTYPE_PUBLIC\" >";
        assertEquals(expected, doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithSystemId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", null, "HTML_DOCTYPE_SYSTEM", doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertEquals("HTML_DOCTYPE_SYSTEM", doctype.attr("systemId"));
        String expected = "<!DOCTYPE html SYSTEM \"HTML_DOCTYPE_SYSTEM\" >";
        assertEquals(expected, doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithBothPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML_DOCTYPE_PUBLIC", "HTML_DOCTYPE_SYSTEM", doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertEquals("HTML_DOCTYPE_PUBLIC", doctype.attr("publicId"));
        assertEquals("HTML_DOCTYPE_SYSTEM", doctype.attr("systemId"));
        String expected = "<!DOCTYPE html PUBLIC \"HTML_DOCTYPE_PUBLIC\" SYSTEM \"HTML_DOCTYPE_SYSTEM\" >";
        assertEquals(expected, doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", null, null, doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        String expected = "<!DOCTYPE html >";
        assertEquals(expected, doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithNullAttributes() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", null, null, doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        String expected = "<!DOCTYPE html >";
        assertEquals(expected, doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithBaseUri() {
        Document doc = new Document();
        String baseUri = "baseUri";
        DocumentType doctype = new DocumentType("html", null, null, baseUri);
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        String expected = "<!DOCTYPE html >";
        assertEquals(expected, doctype.outerHtml());
        assertEquals(baseUri, doctype.baseUri());
    }

    @Test
    public void testDocumentTypeWithEmptyStringAttributes() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "", "", doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        String expected = "<!DOCTYPE html >";
        assertEquals(expected, doctype.outerHtml());
    }

    @Test
    public void testDocumentTypeWithBlankStringAttributes() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", " ", " ", doc.baseUri());
        assertEquals("#doctype", doctype.nodeName());
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        String expected = "<!DOCTYPE html >";
        assertEquals(expected, doctype.outerHtml());
    }
}