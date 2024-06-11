package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attrs().get("name"));
        assertEquals("publicId", documentType.attrs().get("publicId"));
        assertEquals("systemId", documentType.attrs().get("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyName() {
        new DocumentType(null, "", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        new DocumentType(null, "", "", "");
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoPublicId() {
        DocumentType documentType = new DocumentType("html", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoSystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadNoPublicIdOrSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testNullAttrs() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertNull(documentType.attrs());
    }

    @Test
    public void testEmptyAttrs() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals(Node.EMPTY_MAP, documentType.attrs());
    }

    @Test
    public void testSetAndGetAttrs() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        documentType.attr("foo", "bar");
        documentType.attr("baz", "qux");

        Map<String, String> attrs = documentType.attrs();

        assertEquals("bar", attrs.get("foo"));
        assertEquals("qux", attrs.get("baz"));
    }

    @Test
    public void testSetAndRemoveAttr() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.attr("foo", "bar");
        documentType.removeAttr("foo");

        Map<String, String> attrs = documentType.attrs();

        assertNull(attrs.get("foo"));
    }

    @Test
    public void testSetAndRemoveAttrWithEmptyValue() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.attr("foo", "");
        documentType.removeAttr("foo");

        Map<String, String> attrs = documentType.attrs();

        assertNull(attrs.get("foo"));
    }

    @Test
    public void testRemoveAttrNullKey() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        documentType.removeAttr(null);

        Map<String, String> attrs = documentType.attrs();

        assertEquals(Node.EMPTY_MAP, attrs);
    }

    @Test
    public void testRemoveAttrNonExistentKey() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");

        Map<String, String> attrs = documentType.attrs();

        documentType.removeAttr("foo");

        assertNull(attrs.get("foo"));
    }
}