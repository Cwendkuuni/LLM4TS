package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "HTML TDG", "HTML 4.01 Frameset", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML TDG", documentType.attr("publicId"));
        assertEquals("HTML 4.01 Frameset", documentType.attr("systemId"));
        assertNull(documentType.attributes().getNamedItem("xml:version"));
    }

    @Test
    public void testDocumentTypeCreationWithBaseUri() {
        String baseUri = "http://example.com/";
        DocumentType documentType = new DocumentType("html", "HTML TDG", "HTML 4.01 Frameset", baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML TDG", documentType.attr("publicId"));
        assertEquals("HTML 4.01 Frameset", documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document document = new Document("");
        DocumentType documentType = new DocumentType("html", "HTML TDG", "HTML 4.01 Frameset", null);
        document.appendChild(documentType);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML TDG\" \"HTML 4.01 Frameset\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        new DocumentType(null, "HTML TDG", "HTML 4.01 Frameset", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPublicId() {
        new DocumentType("html", "", "HTML 4.01 Frameset", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySystemId() {
        new DocumentType("html", "HTML TDG", "", null);
    }
}