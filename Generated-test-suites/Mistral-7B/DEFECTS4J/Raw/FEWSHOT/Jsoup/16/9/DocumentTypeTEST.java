package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTMLtdPUBLIC";
        String systemId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
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
    public void testDocumentTypeWithNullBaseUri() {
        String name = "html";
        String publicId = "HTMLtdPUBLIC";
        String systemId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String baseUri = null;

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "HTMLtdPUBLIC";
        String systemId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTMLtdPUBLIC\" \"" + systemId + "\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        String name = "html";
        String publicId = "HTMLtdPUBLIC";
        String systemId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String baseUri = "http://www.example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }
}