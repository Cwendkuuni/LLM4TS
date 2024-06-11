package org.jsoup.nodes;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        // Test the document type constructor with valid inputs
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "https://www.example.com/");

        // Verify that the properties are set correctly
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", documentType.attr("systemId"));
        assertEquals("https://www.example.com/", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        // Create a document type with a name
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        // Create a document type with a public ID and system ID
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", null);

        // Generate the expected HTML head
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"")
                .append(" http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");

        // Test the outerHtmlHead method
        assertEquals(expected.toString(), documentType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlTail() {
        // Create a document type without any properties
        DocumentType documentType = new DocumentType(null, null, null, null);

        // Test the outerHtmlTail method
        assertEquals("", documentType.outerHtmlTail());
    }
}