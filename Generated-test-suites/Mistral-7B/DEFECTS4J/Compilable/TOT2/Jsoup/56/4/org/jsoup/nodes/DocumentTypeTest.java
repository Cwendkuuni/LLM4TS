package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://www.example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://www.example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    // Testing outerHtmlHead and outerHtmlTail methods will be covered in edge cases
}