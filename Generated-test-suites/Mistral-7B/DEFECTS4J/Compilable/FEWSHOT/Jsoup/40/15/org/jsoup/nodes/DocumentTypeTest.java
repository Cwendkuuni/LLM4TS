package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeCreationNullName() {
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        new DocumentType(null, publicId, systemId, baseUri);
    }

    @Test
    public void testDocumentTypeCreationEmptyName() {
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType("", publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = null;
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        String name = "html";
        String publicId = null;
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithBothPublicIdAndSystemId() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeEquals() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType1 = new DocumentType(name, publicId, systemId, baseUri);
        DocumentType docType2 = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualDifferentName() {
        String name1 = "html";
        String name2 = "html5";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType1 = new DocumentType(name1, publicId, systemId, baseUri);
        DocumentType docType2 = new DocumentType(name2, publicId, systemId, baseUri);

        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualDifferentPublicId() {
        String name = "html";
        String publicId1 = "HTML_4_01";
        String publicId2 = "HTML5_1.0";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType1 = new DocumentType(name, publicId1, systemId, baseUri);
        DocumentType docType2 = new DocumentType(name, publicId2, systemId, baseUri);

        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualDifferentSystemId() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId1 = "http://www.w3.org/TR/html4/strict.dtd";
        String systemId2 = "http://www.w3.org/TR/html5/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType1 = new DocumentType(name, publicId, systemId1, baseUri);
        DocumentType docType2 = new DocumentType(name, publicId, systemId2, baseUri);

        assertNotEquals(docType1, docType2);
    }

    @Test
    public void testDocumentTypeNotEqualNull() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotEquals(docType, null);
    }

    @Test
    public void testDocumentTypeHashCode() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType1 = new DocumentType(name, publicId, systemId, baseUri);
        DocumentType docType2 = new DocumentType(name, publicId, systemId, baseUri);

        int hashCode1 = docType1.hashCode();
        int hashCode2 = docType2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_4_01\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadEmptyName() {
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        DocumentType docType = new DocumentType("", publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE >", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = null;
        String baseUri = "https://example.com/";

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_4_01\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() {
        String name = "html";
        String publicId = null;
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithBothPublicIdAndSystemId() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML_4_01\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeBaseUri() {
        String name = "html";
        String publicId = "HTML_4_01";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(baseUri, docType.baseUri());
    }
}