package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "public id", "system id", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("public id", docType.attr("publicId"));
        assertEquals("system id", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "public id", "system id", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    // Test outerHtmlHead and outerHtmlTail methods here
}