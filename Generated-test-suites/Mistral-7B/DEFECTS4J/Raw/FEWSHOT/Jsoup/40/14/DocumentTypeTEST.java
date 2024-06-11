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
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML_VERSION", docType.attr("publicId"));
        assertEquals("html.dtd", docType.attr("systemId"));
        assertEquals("/baseUrl", docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameInDocumentTypeCreation() {
        new DocumentType(null, "HTML_VERSION", "html.dtd", "/baseUrl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicIdInDocumentTypeCreation() {
        new DocumentType("html", null, "html.dtd", "/baseUrl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemIdInDocumentTypeCreation() {
        new DocumentType("html", "HTML_VERSION", null, "/baseUrl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriInDocumentTypeCreation() {
        new DocumentType("html", "HTML_VERSION", "html.dtd", null);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyName() {
        DocumentType docType = new DocumentType(null, "HTML_VERSION", "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() {
        DocumentType docType = new DocumentType("html", null, "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html SYSTEM \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", null, "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtml(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlWithAllAttributes() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtml(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeEquals() {
        DocumentType docType1 = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        DocumentType docType2 = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        DocumentType docType3 = new DocumentType("html", "HTML_VERSION", null, "/baseUrl");
        DocumentType docType4 = new DocumentType("html", null, "html.dtd", "/baseUrl");
        DocumentType docType5 = new DocumentType("html", "HTML_VERSION", "html.dtd", null);

        assertSame(docType1, docType1);
        assertSame(docType1, docType2);

        assertNotSame(docType1, docType3);
        assertNotSame(docType1, docType4);
        assertNotSame(docType1, docType5);
    }

    @Test
    public void testDocumentTypeHashCode() {
        DocumentType docType1 = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        DocumentType docType2 = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        DocumentType docType3 = new DocumentType("html", "HTML_VERSION", null, "/baseUrl");
        DocumentType docType4 = new DocumentType("html", null, "html.dtd", "/baseUrl");
        DocumentType docType5 = new DocumentType("html", "HTML_VERSION", "html.dtd", null);

        assertEquals(docType1.hashCode(), docType2.hashCode());
        assertNotEquals(docType1.hashCode(), docType3.hashCode());
        assertNotEquals(docType1.hashCode(), docType4.hashCode());
        assertNotEquals(docType1.hashCode(), docType5.hashCode());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "html.dtd", "/baseUrl");
        assertEquals("<html xmlns:jsoup=\"http://jsoup.org\"><!DOCTYPE html PUBLIC \"HTML_VERSION\" \"html.dtd\">", docType.toString());
    }

    @Test
    public void testDocumentTypeWithNullAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html SYSTEM \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyNameAndPublicId() {
        DocumentType docType = new DocumentType(null, null, "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html SYSTEM \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyNameAndSystemId() {
        DocumentType docType = new DocumentType(null, "HTML_VERSION", null, "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "html.dtd", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "html.dtd", null);
        assertEquals(null, docType.baseUri());
    }

    @Test
    public void testDocumentTypeWithBlankPublicId() {
        DocumentType docType = new DocumentType("html", "\"\", HTML_VERSION", "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"\" HTML_VERSION \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithQuotesInPublicId() {
        DocumentType docType = new DocumentType("html", "\"HTML Public ID\" HTML_VERSION", "html.dtd", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML Public ID\" HTML_VERSION \"html.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeWithQuotesInSystemId() {
        DocumentType docType = new DocumentType("html", "HTML_VERSION", "\"html.dtd with quotes\"", "/baseUrl");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML_VERSION\" \"html.dtd with quotes\">", accum.toString());
    }
}