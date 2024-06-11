package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        assertNotNull(docType);
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML Public ID", docType.attr("publicId"));
        assertEquals("HTML System ID", docType.attr("systemId"));
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeCreationWithNullPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertNotNull(docType);
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertNotNull(docType);
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithBaseUri() {
        String baseUri = "https://example.com";
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", baseUri);
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\" \"HTML System ID\">", accum.toString());

        docType = new DocumentType("html", null, null, null);
        accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        assertSame("", accum.toString());
    }

    @Test
    public void testDocumentTypeIsEmpty() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertTrue(docType.isEmpty());
    }

    @Test
    public void testDocumentTypeIsBlockLevel() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertFalse(docType.isBlockElement());
    }

    @Test
    public void testDocumentTypeIsInlineLevel() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertFalse(docType.isInlineElement());
    }

    @Test
    public void testDocumentTypeIsEmptyElement() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertFalse(docType.isEmptyElement());
    }

    @Test
    public void testDocumentTypeIsSelfClosing() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertFalse(docType.isSelfClosing());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\" \"HTML System ID\">", docType.toString());

        docType = new DocumentType("html", null, null, null);
        assertEquals("<!DOCTYPE html>", docType.toString());
    }

    @Test
    public void testDocumentTypeEquals() {
        DocumentType docType1 = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        DocumentType docType2 = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        DocumentType docType3 = new DocumentType("html", null, null, null);

        assertEquals(docType1, docType1);
        assertEquals(docType1, docType2);
        assertNotEquals(docType1, docType3);
    }

    @Test
    public void testDocumentTypeHashCode() {
        DocumentType docType1 = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        DocumentType docType2 = new DocumentType("html", "HTML Public ID", "HTML System ID", null);
        DocumentType docType3 = new DocumentType("html", null, null, null);

        int hashCode1 = docType1.hashCode();
        int hashCode2 = docType2.hashCode();
        int hashCode3 = docType3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}