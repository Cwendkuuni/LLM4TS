
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "baseUri";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();
        Document doc = new Document();
        documentType.outerHtmlHead(accum, 0, doc.outputSettings());
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\";";
        if (!StringUtil.isBlank(publicId)) {
            expected += " PUBLIC \"\"";
        }
        if (!StringUtil.isBlank(systemId)) {
            expected += ' ' + systemId + "\"";
        }
        expected += '>';
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeEdgeCasesAndErrors() {
        // Test empty constructor
        DocumentType documentType = new DocumentType();
        assertEquals("#doctype", documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());

        // Test null values
        DocumentType documentType1 = new DocumentType(null, null, null, null);
        assertEquals("#doctype", documentType1.nodeName());
        assertNull(documentType1.attr("name"));
        assertNull(documentType1.attr("publicId"));
        assertNull(documentType1.attr("systemId"));
        assertNull(documentType1.baseUri());

        // Test empty strings
        DocumentType documentType2 = new DocumentType("", "", "", "");
        assertEquals("#doctype", documentType2.nodeName());
        assertNull(documentType2.attr("name"));
        assertNull(documentType2.attr("publicId"));
        assertNull(documentType2.attr("systemId"));
        assertNull(documentType2.baseUri());

        // Test whitespace strings
        DocumentType documentType3 = new DocumentType(" name ", "publicId", "systemId", " baseUri ");
        assertEquals("#doctype", documentType3.nodeName());
        assertEquals(" name ", documentType3.attr("name"));
        assertEquals("publicId", documentType3.attr("publicId"));
        assertEquals("systemId", documentType3.attr("systemId"));
        assertEquals(" baseUri ", documentType3.baseUri());

        // Test null baseUri
        DocumentType documentType4 = new DocumentType("name", "publicId", "systemId", null);
        assertEquals("#doctype", documentType4.nodeName());
        assertEquals("name", documentType4.attr("name"));
        assertEquals("publicId", documentType4.attr("publicId"));
        assertEquals("systemId", documentType4.attr("systemId"));
        assertNull(documentType4.baseUri());
    }
}

