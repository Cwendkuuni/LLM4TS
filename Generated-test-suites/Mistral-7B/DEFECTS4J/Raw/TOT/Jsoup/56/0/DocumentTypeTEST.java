package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeWithNameAndPublicId() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String baseUri = "http://www.example.com/";

        DocumentType documentType = new DocumentType(name, publicId, null, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(NAME));
        assertEquals(publicId, documentType.attr(PUBLIC_ID));
        assertEquals(baseUri, documentType.baseUri());

        Appendable accumulator = new StringBuffer();
        try {
            documentType.outerHtmlHead(accumulator, 0, null);
        } catch (IOException e) {
            fail("outerHtmlHead should not throw IOException: " + e.getMessage());
        }

        String expected = "<!DOCTYPE " + name + " PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testDocumentTypeWithNameAndSystemId() {
        String name = "html";
        String systemId = "about:html";
        String baseUri = "http://www.example.com/";

        DocumentType documentType = new DocumentType(name, null, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(NAME));
        assertEquals(systemId, documentType.attr(SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());

        Document doc = Jsoup.parse("<!DOCTYPE " + name + " SYSTEM \"" + systemId + "\">", baseUri);
        Node doctypeNode = doc.getFirstChild();

        assertEquals(documentType, doctypeNode);
    }

    @Test
    public void testDocumentTypeWithEmptyNameAndId() {
        String name = "";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://www.example.com/";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException as name and id cannot be empty");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        DocumentType documentType = new DocumentType("html", "public id", "system id", baseUri);

        Appendable accumulator = new StringBuffer();
        try {
            documentType.outerHtmlHead(accumulator, 0, null);
            documentType.outerHtmlTail(accumulator, 0, null);
        } catch (IOException e) {
            fail("outerHtmlTail should not throw IOException: " + e.getMessage());
        }

        String expectedHtml = "<!DOCTYPE html PUBLIC \"public id\"\n>";
        assertEquals(expectedHtml, accumulator.toString());
    }
}