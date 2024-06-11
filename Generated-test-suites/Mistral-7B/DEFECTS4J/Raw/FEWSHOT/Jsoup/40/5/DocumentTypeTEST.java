package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", null);
        assertEquals("#doctype", docType.nodeName());
        assertTrue(docType.attributes().size() == 3);
        assertEquals("html", docType.attributes().get("name").getValue());
        assertEquals("HTML", docType.attributes().get("publicId").getValue());
        assertEquals("HTML5", docType.attributes().get("systemId").getValue());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertTrue(docType.attributes().size() == 0);
    }

    @Test
    public void testDocumentTypeWithOnlyPublicId() {
        DocumentType docType = new DocumentType("html", "PUBLIC", "HTML PUBLIC", null);
        assertEquals("#doctype", docType.nodeName());
        assertTrue(docType.attributes().size() == 1);
        assertEquals("PUBLIC", docType.attributes().get("").getValue());
    }

    @Test
    public void testDocumentTypeWithOnlySystemId() {
        DocumentType docType = new DocumentType("html", "", "", "http://example.com/");
        assertEquals("#doctype", docType.nodeName());
        assertTrue(docType.attributes().size() == 1);
        assertEquals("http://example.com/", docType.attributes().get("systemId").getValue());
    }

    @Test
    public void testDocumentTypeWithAllAttributes() {
        DocumentType docType = new DocumentType("html", "PUBLIC", "HTML PUBLIC", "http://example.com/", null);
        assertEquals("#doctype", docType.nodeName());
        assertTrue(docType.attributes().size() == 4);
        assertEquals("PUBLIC", docType.attributes().get("").getValue());
        assertEquals("HTML PUBLIC", docType.attributes().get("publicId").getValue());
        assertEquals("http://example.com/", docType.attributes().get("systemId").getValue());
        assertNull(docType.attributes().get("name").getValue());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = new Document();
        Element html = new Element("html");
        DocumentType doctype = new DocumentType("html", "PUBLIC", "HTML PUBLIC", null);
        html.append(doctype);

        StringBuilder accum = new StringBuilder();
        doc.outerHtml(accum);

        String expected = "<html><!DOCTYPE html PUBLIC \"HTML PUBLIC\"><!--\n";
        assertEquals(expected.substring(0, expected.length() - 2), accum.toString());
    }
}