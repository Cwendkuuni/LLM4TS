package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", dt.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType dtHtml = new DocumentType("name", "publicId", "systemId", "baseUri");
        DocumentType dtNoPublicId = new DocumentType("name", null, "systemId", "baseUri");
        DocumentType dtNoSystemId = new DocumentType("name", "publicId", null, "baseUri");
        DocumentType dtNoPublicIdAndNoSystemId = new DocumentType("name", null, null, "baseUri");

        assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\">", dtHtml.outerHtmlHead());
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", dtNoPublicId.outerHtmlHead());
        assertEquals("<!DOCTYPE name \"systemId\">", dtNoSystemId.outerHtmlHead());
        assertEquals("<!doctype name>", dtNoPublicIdAndNoSystemId.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("", dt.outerHtmlTail());
    }

    @Test
    public void testHas() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");

        assertTrue(dt.has("name"));
        assertTrue(dt.has("publicId"));
        assertTrue(dt.has("systemId"));
        assertFalse(dt.has("baseUri"));
    }

}