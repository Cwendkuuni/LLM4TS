package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }
}