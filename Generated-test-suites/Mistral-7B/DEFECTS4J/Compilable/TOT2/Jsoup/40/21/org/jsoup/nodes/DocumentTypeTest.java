package org.jsoup.nodes;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", "baseURI");
        String name = docType.attr("name");
        String publicId = docType.attr("publicId");
        String systemId = docType.attr("systemId");
        String baseUri = docType.baseUri();

        assertEquals("html", name);
        assertEquals("publicID", publicId);
        assertEquals("systemID", systemId);
        assertEquals("baseURI", baseUri);
    }

    // Test for edge cases and exception handling can be added here
}