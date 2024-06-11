package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "WT-W3C-XHTML1-20010111";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyValues() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "WT-W3C-XHTML1-20010111";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" WT-W3C-XHTML1-20010111>", accum.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "WT-W3C-XHTML1-20010111";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html >", accum.toString());
    }

    @Test
    public void testDocumentTypeToStringWithEmptySystemId() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" >", accum.toString());
    }

    @Test
    public void testDocumentTypeToStringWithNullBaseUri() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "WT-W3C-XHTML1-20010111";
        String baseUri = null;

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertSame("<!DOCTYPE html", accum.toString());
    }
}