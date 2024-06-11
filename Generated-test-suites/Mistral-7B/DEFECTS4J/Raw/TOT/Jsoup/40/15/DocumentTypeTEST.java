
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        // Test empty constructor
        DocumentType docType1 = new DocumentType();
        assertNull(docType1.attr("name"));
        assertNull(docType1.attr("publicId"));
        assertNull(docType1.attr("systemId"));

        // Test constructor with valid arguments
        Document doc = new Document();
        DocumentType docType2 = new DocumentType("html", "publicId1", "systemId1", doc.baseUri());
        assertEquals("#doctype", docType2.nodeName());
        assertEquals("html", docType2.attr("name"));
        assertEquals("publicId1", docType2.attr("publicId"));
        assertEquals("systemId1", docType2.attr("systemId"));

        // Test constructor with null baseUri
        DocumentType docType3 = new DocumentType("html", "publicId2", "systemId2", null);
        assertEquals("#doctype", docType3.nodeName());
        assertNull(docType3.attr("baseUri"));

        // Test constructor with empty strings
        DocumentType docType4 = new DocumentType("", "", "", doc.baseUri());
        assertEquals("#doctype", docType4.nodeName());
        assertNull(docType4.attr("name"));
        assertNull(docType4.attr("publicId"));
        assertNull(docType4.attr("systemId"));

        // Test constructor with null name
        DocumentType docType5 = new DocumentType(null, "publicId2", "systemId2", doc.baseUri());
        assertEquals("#doctype", docType5.nodeName());
        assertNull(docType5.attr("name"));
        assertEquals("publicId2", docType5.attr("publicId"));
        assertEquals("systemId2", docType5.attr("systemId"));

        // Test constructor with null publicId and systemId
        DocumentType docType6 = new DocumentType("html", null, null, doc.baseUri());
        assertEquals("#doctype", docType6.nodeName());
        assertEquals("html", docType6.attr("name"));
        assertNull(docType6.attr("publicId"));
        assertNull(docType6.attr("systemId"));

        // Test outerHtmlHead and outerHtmlTail methods
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();

        DocumentType docType7 = new DocumentType("html", "publicId3", "systemId3", doc.baseUri());
        docType7.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId3\" \"systemId3\">", accum.toString());

        accum.setLength(0);
        docType7.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());

        accum.setLength(0);
        DocumentType docType8 = new DocumentType("html", "publicId4", "systemId4", doc.baseUri());
        docType8.outerHtmlHead(accum, 0, out);
        docType8.outerHtmlTail(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId4\" \"systemId4\">", accum.toString());

        // Test outerHtml methods with accumulator content
        accum.append("<html></html>");
        docType7.outerHtmlHead(accum, 0, out);
        docType7.outerHtmlTail(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId3\" \"systemId3\"><html></html>", accum.toString());

        accum.setLength(0);
        docType8.outerHtmlHead(accum, 0, out);
        docType8.outerHtmlTail(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId4\" \"systemId4\"><html></html>", accum.toString());
    }

    @Test
    public void testInvalidArguments() {
        Document doc = new Document();

        try {
            new DocumentType(null, null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("", null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("html", null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("html", "publicId", null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("html", "publicId", "systemId", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("html", "publicId", "systemId", new Element("html"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("html", "publicId", "systemId", new TextNode(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}