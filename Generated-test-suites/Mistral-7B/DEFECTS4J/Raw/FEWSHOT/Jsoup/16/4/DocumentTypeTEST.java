package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML_PUBLIC", docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertEquals("", docType.attr("baseUri"));
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("", "", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testDocumentTypeWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML_PUBLIC", docType.attr("publicId"));
        assertEquals("DTD HTML 4.01", docType.attr("systemId"));
        assertEquals("", docType.attr("baseUri"));
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC\" \"DTD HTML 4.01\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithPublicIdOnly() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC", "", "");
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithSystemIdOnly() {
        DocumentType docType = new DocumentType("html", "", "DTD HTML 4.01", "");
        assertEquals("<!DOCTYPE html SYSTEM \"DTD HTML 4.01\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC\" \"DTD HTML 4.01\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyAttributes() {
        DocumentType docType = new DocumentType("", "", "", "");
        assertEquals("<!DOCTYPE>", docType.toString());
    }

    @Test
    public void testDocumentTypeEquals() {
        DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        DocumentType docType2 = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        assertEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualsDifferentNames() {
        DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        DocumentType docType2 = new DocumentType("head", "HTML_PUBLIC", "DTD HTML 4.01", "");
        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualsDifferentPublicIds() {
        DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        DocumentType docType2 = new DocumentType("html", "HTML_INTERNAL", "DTD HTML 4.01", "");
        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualsDifferentSystemIds() {
        DocumentType docType1 = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        DocumentType docType2 = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01_ALTERNATE", "");
        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotNull() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC", "DTD HTML 4.01", "");
        assertNotNull(docType);
    }
}