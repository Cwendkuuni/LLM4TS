package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML", docType.attr("publicId"));
        assertEquals("HTML5", docType.attr("systemId"));
        assertNull(docType.attr("quirkMode"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationWithEmptyName() {
        new DocumentType(null, "", "", null);
    }

    @Test
    public void testDocumentTypeCreationWithNullPublicId() {
        DocumentType docType = new DocumentType("html", null, "HTML5", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertEquals("HTML5", docType.attr("systemId"));
        assertNull(docType.attr("quirkMode"));
    }

    @Test
    public void testDocumentTypeCreationWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("quirkMode"));
    }

    @Test
    public void testDocumentTypeCreationWithValidAttributes() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 5", "http://www.w3.org/TR/html5/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML PUBLIC", docType.attr("publicId"));
        assertEquals("DTD HTML 5", docType.attr("systemId"));
        assertNull(docType.attr("quirkMode"));
        assertTrue(docType.hasAttr("publicId"));
        assertTrue(docType.hasAttr("systemId"));
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", null);
        assertEquals("<!DOCTYPE html>", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithPublicId() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 5", null);
        assertEquals("<!DOCTYPE html PUBLIC \"DTD HTML 5\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithSystemId() {
        DocumentType docType = new DocumentType("html", "HTML", "DTD HTML 5", "http://www.w3.org/TR/html5/");
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html5/\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithAllAttributes() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 5", "http://www.w3.org/TR/html5/");
        assertEquals("<!DOCTYPE html PUBLIC \"DTD HTML 5\" SYSTEM \"http://www.w3.org/TR/html5/\">", docType.toString());
    }

    @Test
    public void testDocumentTypeToStringWithQuirkMode() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "DTD HTML 5", "http://www.w3.org/TR/html5/", "QUIRKS");
        assertEquals("<!DOCTYPE html PUBLIC \"DTD HTML 5\" SYSTEM \"http://www.w3.org/TR/html5/\">\n<![ATTLIST html quirks CDATA 'quirks']>", docType.toString());
    }
}