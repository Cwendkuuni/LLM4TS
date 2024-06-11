package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML TedNG", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML TedNG", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr("systemId"));
        assertNull(docType.attr("id"));
    }

    @Test
    public void testDocumentTypeCreationWithNullValues() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertNotNull(docType);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML TedNG", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", null);
        String toString = docType.toString();
        assertTrue(toString.startsWith("<!DOCTYPE html"));
        assertTrue(toString.contains(" PUBLIC \"".concat("HTML TedNG".concat("\""))));
        assertTrue(toString.contains("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\""));
        assertTrue(toString.endsWith(">"));
    }
}