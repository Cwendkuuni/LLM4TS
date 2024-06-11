package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", null);
        assertEquals("#doctype", docType.nodeName());
    }

    // Test other methods here...

}