package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        assertEquals("html", documentType.nodeName());
        assertEquals("publicID", documentType.attr("publicId"));
        assertEquals("systemID", documentType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        new DocumentType(null, "", "", null);
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "systemID", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM systemID>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithAllAttributes() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" systemID>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testAttributePublicId() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        assertEquals("publicID", documentType.attr("publicId"));
        assertEquals("publicID", documentType.attr("public-id"));
        assertEquals("publicID", documentType.attr(" PublicID "));
    }

    @Test
    public void testAttributeSystemId() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        assertEquals("systemID", documentType.attr("systemId"));
        assertEquals("systemID", documentType.attr("system-id"));
        assertEquals("systemID", documentType.attr(" SystemID "));
    }

    @Test
    public void testAttributeBaseUri() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");

        assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testNullBaseUri() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

        assertNull(documentType.baseUri());
    }

    @Test
    public void testCloneNode() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
        DocumentType clonedDocumentType = (DocumentType) documentType.cloneNode(true);

        assertEquals(documentType.nodeName(), clonedDocumentType.nodeName());
        assertEquals(documentType.attr("publicId"), clonedDocumentType.attr("publicId"));
        assertEquals(documentType.attr("systemId"), clonedDocumentType.attr("systemId"));
        assertEquals(documentType.baseUri(), clonedDocumentType.baseUri());
    }

    @Test
    public void testToString() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");

        assertEquals("<html xmlns=http://www.w3.org/1999/xhtml>\n<!DOCTYPE html PUBLIC \"publicID\" systemID>\n</html>", documentType.toString());
    }
}