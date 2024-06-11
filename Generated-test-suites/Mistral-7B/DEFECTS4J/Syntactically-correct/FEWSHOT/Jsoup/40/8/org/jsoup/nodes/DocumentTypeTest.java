package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "DTD HTML 5";
        String systemId = "http://www.w3.org/TR/html5/DTD/html5.dtd";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        String name = "";
        String publicId = "DTD HTML 5";
        String systemId = "http://www.w3.org/TR/html5/DTD/html5.dtd";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"DTD HTML 5\" \"http://www.w3.org/TR/html5/DTD/html5.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBaseUri() {
        DocumentType documentType = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", "http://example.com");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"DTD HTML 5\" \"http://www.w3.org/TR/html5/DTD/html5.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "DTD HTML 5", null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNameOnly() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertNull(accum.toString());
    }

    @Test
    public void testEquals() {
        DocumentType documentType1 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
        DocumentType documentType2 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);

        assertSame(documentType1, documentType2);
    }

    @Test
    public void testEqualsWithDifferentBaseUri() {
        DocumentType documentType1 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
        DocumentType documentType2 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", "http://example2.com");

        assertNotSame(documentType1, documentType2);
    }

    @Test
    public void testEqualsWithDifferentName() {
        DocumentType documentType1 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
        DocumentType documentType2 = new DocumentType("head", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);

        assertNotSame(documentType1, documentType2);
    }

    @Test
    public void testEqualsWithDifferentPublicId() {
        DocumentType documentType1 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
        DocumentType documentType2 = new DocumentType("html", "DTD HTML 4.01", "http://www.w3.org/TR/html4/DTD/html1.dtd", null);

        assertNotSame(documentType1, documentType2);
    }

    @Test
    public void testEqualsWithDifferentSystemId() {
        DocumentType documentType1 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
        DocumentType documentType2 = new DocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html4/DTD/html1.dtd", null);

        assertNotSame(documentType1, documentType2);
    }
}