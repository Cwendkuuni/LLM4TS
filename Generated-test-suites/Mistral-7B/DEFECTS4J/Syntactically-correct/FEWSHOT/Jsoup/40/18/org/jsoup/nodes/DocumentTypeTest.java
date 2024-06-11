package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNewDocumentTypeWithValidArgs() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testNewDocumentTypeWithEmptyName() {
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";

        Exception exception = AssertionError.class; // Change this if error is different

        try {
            new DocumentType("", publicId, systemId, baseUri);
        } catch (Exception e) {
            exception = e;
        }

        assertTrue(exception instanceof IllegalArgumentException);
        assertTrue(exception.getMessage().contains("name can't be empty"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNameAttribute() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");
        docType.attr("name", "xhtml");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">\n<!DOCTYPE xhtml [", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAttribute() {
        DocumentType docType = new DocumentType("html", "", "systemId", "");
        docType.attr("publicId", "publicId");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdAttribute() {
        DocumentType docType = new DocumentType("html", "publicId", "", "");
        docType.attr("systemId", "systemId");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemIdAttributes() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testBaseUri() {
        String baseUri = "baseUri";
        DocumentType docType = new DocumentType("html", "", "", baseUri);

        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testAttrEquals() {
        DocumentType docType1 = new DocumentType("html", "publicId1", "systemId1", "");
        DocumentType docType2 = new DocumentType("html", "publicId2", "systemId2", "");

        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testAttrEqualsWithSameNameAndValues() {
        DocumentType docType1 = new DocumentType("html", "publicId", "systemId", "");
        DocumentType docType2 = new DocumentType("html", "publicId", "systemId", "");

        assertEquals(docType1, docType2);
    }

    @Test
    public void testAttrEqualsWithSameNameButDifferentValues() {
        DocumentType docType1 = new DocumentType("html", "publicId1", "systemId1", "");
        DocumentType docType2 = new DocumentType("html", "publicId2", "systemId1", "");

        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testAttrEqualsWithDifferentNamesAndValues() {
        DocumentType docType1 = new DocumentType("html", "publicId1", "systemId1", "");
        DocumentType docType2 = new DocumentType("html", "publicId1", "systemId2", "");

        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testAttrEqualsWithNull() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");

        assertNull(docType.attr(null));
    }

    @Test
    public void testAttrEqualsWithEmptyString() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");

        assertEquals("", docType.attr(""));
    }
}