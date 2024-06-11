package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

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
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        String name = null;
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        new DocumentType(name, publicId, systemId, baseUri);
    }

    @Test
    public void testOuterHtmlHead() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" system\"systemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" system\"\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNullBaseUri() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = null;

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" system\"systemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankPublicIdAndSystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoAttributes() {
        String name = "html";
        String publicId = null;
        String systemId = null;
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testAttributes() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertSame(baseUri, documentType.baseUri());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
    }

    @Test
    public void testEmptyPublicIdAttribute() {
        String name = "html";
        String publicId = "";
        String systemId = "systemID";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("", documentType.attr("publicId"));
    }

    @Test
    public void testEmptySystemIdAttribute() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "";
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testNullAttributes() {
        String name = "html";
        String publicId = null;
        String systemId = null;
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testAttributesWithNullValues() {
        String name = "html";
        String publicId = "publicID";
        String systemId = null;
        String baseUri = "baseUri";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(publicId, documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }
}