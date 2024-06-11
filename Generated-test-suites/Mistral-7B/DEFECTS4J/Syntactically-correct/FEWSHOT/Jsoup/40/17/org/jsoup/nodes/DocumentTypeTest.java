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
        DocumentType docType1 = new DocumentType("html", "HTML", "HTML 4.01 Transitional", null);
        DocumentType docType2 = new DocumentType("html", null, null, null);

        assertEquals("#doctype", docType1.nodeName());
        assertEquals("#doctype", docType2.nodeName());

        assertEquals("html", docType1.attr("name"));
        assertEquals("html", docType2.attr("name"));

        assertEquals("HTML", docType1.attr("publicId"));
        assertNull(docType2.attr("publicId"));

        assertEquals("HTML 4.01 Transitional", docType1.attr("systemId"));
        assertNull(docType2.attr("systemId"));

        assertEquals(null, docType1.attr("baseUri"));
        assertNull(docType2.attr("baseUri"));
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("", "", "", null);

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML 4.01 Transitional", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" \"HTML 4.01 Transitional\" >", accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", null, null, null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html>", accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML", null, null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"HTML\" >", accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyValues() {
        DocumentType docType = new DocumentType("", null, null, null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, null);

        assertEquals("<!DOCTYPE>", accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML 4.01 Transitional", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlTail(accumulator, 0, null);

        assertEquals("", accumulator.toString());
    }

    @Test
    public void testSetAttrs() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "HTML");
        docType.attr("systemId", "HTML 4.01 Transitional");
        docType.attr("baseUri", "https://example.com/");

        assertEquals("html", docType.attr("name"));
        assertEquals("HTML", docType.attr("publicId"));
        assertEquals("HTML 4.01 Transitional", docType.attr("systemId"));
        assertEquals("https://example.com/", docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithEmptyValues() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "");
        docType.attr("publicId", "");
        docType.attr("systemId", "");
        docType.attr("baseUri", "");

        assertEquals("", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithNullValues() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", null);
        docType.attr("publicId", null);
        docType.attr("systemId", null);
        docType.attr("baseUri", null);

        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithEmptyStringAndNull() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "");
        docType.attr("publicId", "HTML");
        docType.attr("systemId", null);
        docType.attr("baseUri", "");

        assertEquals("", docType.attr("name"));
        assertEquals("HTML", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithQuotesInValues() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "\"html\"");
        docType.attr("publicId", "\"HTML PUBLIC\"");
        docType.attr("systemId", "\"-//W3C//DTD HTML 4.01 Transitional//EN\"");
        docType.attr("baseUri", "\"https://example.com/\"");

        assertEquals("html", docType.attr("name"));
        assertEquals("HTML PUBLIC", docType.attr("publicId"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("systemId"));
        assertEquals("https://example.com/", docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithEmptyStringsAndQuotes() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "\"\"");
        docType.attr("publicId", "\"\"");
        docType.attr("systemId", "\"\"");
        docType.attr("baseUri", "\"\"");

        assertEquals("", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithNullAndQuotes() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", null);
        docType.attr("publicId", "\"HTML PUBLIC\"");
        docType.attr("systemId", null);
        docType.attr("baseUri", "\"https://example.com/\"");

        assertNull(docType.attr("name"));
        assertEquals("HTML PUBLIC", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals("https://example.com/", docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithQuotesInNames() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "\"html\"");
        docType.attr("publicId", "");
        docType.attr("systemId", "");
        docType.attr("baseUri", "");

        assertEquals("html", docType.attr("name"));
    }

    @Test
    public void testSetAttrsWithEmptyNameAndQuotes() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "\"\"");
        docType.attr("publicId", "");
        docType.attr("systemId", "");
        docType.attr("baseUri", "");

        assertEquals("", docType.attr("name"));
    }

    @Test
    public void testSetAttrsWithQuotesInPublicId() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "\"HTML PUBLIC\"");
        docType.attr("systemId", "");
        docType.attr("baseUri", "");

        assertEquals("html", docType.attr("name"));
        assertEquals("HTML PUBLIC", docType.attr("publicId"));
    }

    @Test
    public void testSetAttrsWithEmptyPublicIdAndQuotes() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "\"\"");
        docType.attr("systemId", "");
        docType.attr("baseUri", "");

        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
    }

    @Test
    public void testSetAttrsWithQuotesInSystemId() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "");
        docType.attr("systemId", "\"-//W3C//DTD HTML 4.01 Transitional//EN\"");
        docType.attr("baseUri", "");

        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("systemId"));
    }

    @Test
    public void testSetAttrsWithEmptySystemIdAndQuotes() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "");
        docType.attr("systemId", "\"\"");
        docType.attr("baseUri", "");

        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testSetAttrsWithQuotesInBaseUri() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "");
        docType.attr("systemId", "");
        docType.attr("baseUri", "\"https://example.com/\"");

        assertEquals("html", docType.attr("name"));
        assertEquals("https://example.com/", docType.attr("baseUri"));
    }

    @Test
    public void testSetAttrsWithEmptyBaseUriAndQuotes() {
        DocumentType docType = new DocumentType("", "", "", null);

        docType.attr("name", "html");
        docType.attr("publicId", "");
        docType.attr("systemId", "");
        docType.attr("baseUri", "\"\"");

        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("baseUri"));
    }

    @Test
    public void testToString() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML 4.01 Transitional", null);

        assertEquals("<html xmlns:xml=\"http://www.w3.org/1999/xhtml\"><!DOCTYPE html PUBLIC \"HTML\" \"HTML 4.01 Transitional\" ></html>", docType.toString());
    }

    @Test
    public void testToStringWithEmptyValues() {
        DocumentType docType = new DocumentType("", "", "", null);

        assertEquals("<!DOCTYPE>", docType.toString());
    }
}