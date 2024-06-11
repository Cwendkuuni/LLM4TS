package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n", accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullName() {
        DocumentType documentType = new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullPublicId() {
        DocumentType documentType = new DocumentType("html", null, "systemId", "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullSystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", null, "baseUri");
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeConstructionWithNullBaseUri() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptyName() {
        DocumentType documentType = new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructionWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
    }

    @Test
    public void testDocumentTypeConstructionWithLongStrings() {
        String longPublicId = "verylongpublicid";
        String longSystemId = "verylongsystemid";
        String longBaseUri = "verylongbaseuri";

        DocumentType documentType = new DocumentType("html", longPublicId, longSystemId, longBaseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals(longPublicId, documentType.attr("publicId"));
        assertEquals(longSystemId, documentType.attr("systemId"));
        assertEquals(longBaseUri, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}