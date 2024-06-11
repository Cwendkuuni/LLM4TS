package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNotNull(docType.attr("publicId"));
        assertEquals("HTML TDG SGML", docType.attr("publicId"));
        assertNotNull(docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNotNull(docType.attr("publicId"));
        assertEquals("HTML TDG SGML", docType.attr("publicId"));
        assertNotNull(docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNotNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithBothNullPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyName() {
        DocumentType docType = new DocumentType("", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNotNull(docType.attr("publicId"));
        assertEquals("HTML TDG SGML", docType.attr("publicId"));
        assertNotNull(docType.attr("systemId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithNullName() {
        new DocumentType(null, "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"HTML TDG SGML\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"HTML TDG SGML\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        String expected = "";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeAttributes() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Attributes attrs = docType.attributes();
        assertEquals(3, attrs.size());
        assertEquals("name", attrs.get("name"));
        assertEquals("publicId", attrs.get("publicId"));
        assertEquals("systemId", attrs.get("systemId"));
    }

    @Test
    public void testDocumentTypeAttributesWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Attributes attrs = docType.attributes();
        assertEquals(2, attrs.size());
        assertEquals("name", attrs.get("name"));
        assertEquals("systemId", attrs.get("systemId"));
        assertNull(attrs.get("publicId"));
    }

    @Test
    public void testDocumentTypeAttributesWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "", null);
        Attributes attrs = docType.attributes();
        assertEquals(2, attrs.size());
        assertEquals("name", attrs.get("name"));
        assertEquals("publicId", attrs.get("publicId"));
        assertNull(attrs.get("systemId"));
    }

    @Test
    public void testDocumentTypeAttributesWithBothNullPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", null, null, null);
        Attributes attrs = docType.attributes();
        assertEquals(1, attrs.size());
        assertEquals("name", attrs.get("name"));
    }

    @Test
    public void testDocumentTypeAttributesWithEmptyName() {
        DocumentType docType = new DocumentType("", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        Attributes attrs = docType.attributes();
        assertEquals(2, attrs.size());
        assertEquals("publicId", attrs.get("publicId"));
        assertEquals("systemId", attrs.get("systemId"));
        assertNull(attrs.get("name"));
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        String expected = "<!DOCTYPE html PUBLIC \"HTML TDG SGML\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">";
        assertEquals(expected, docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML TDG SGML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        String expected = "<!DOCTYPE html PUBLIC \"HTML TDG SGML\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">";
        assertEquals(expected, docType.toString());
    }
}