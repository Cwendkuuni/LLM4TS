package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML_PUBLIC_ID", docType.attr("publicId"));
        assertEquals("HTML_SYSTEM_ID", docType.attr("systemId"));
        assertNull(docType.attr("id"));
    }

    @Test
    public void testDocumentTypeCreationWithNullValues() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", null);
        StringBuilder expected = new StringBuilder("<!DOCTYPE html");
        if (!StringUtil.isBlank(docType.attr("publicId"))) {
            expected.append(" PUBLIC ");
            expected.append('"').append(docType.attr("publicId")).append('"');
        }
        if (!StringUtil.isBlank(docType.attr("systemId"))) {
            expected.append(' ').append(docType.attr("systemId")).append('"');
        }
        expected.append('>');

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }
}