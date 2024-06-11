
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTypeTest {
    @Test
    public void testDocumentType() {
        DocumentType documentType1 = new DocumentType("name1", "publicId1", "systemId1", "baseUri1");
        assertEquals("name1", documentType1.attr("name"));
        assertEquals("publicId1", documentType1.attr("publicId"));
        assertEquals("systemId1", documentType1.attr("systemId"));

        DocumentType documentType2 = new DocumentType("name2", "publicId2", "systemId2", "baseUri2");
        assertEquals("name2", documentType2.attr("name"));
        assertEquals("publicId2", documentType2.attr("publicId"));
        assertEquals("systemId2", documentType2.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType1 = new DocumentType("name1", "publicId1", "systemId1", "baseUri1");
        StringBuilder accum1 = new StringBuilder();
        documentType1.outerHtmlHead(accum1, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId1\" systemId1\">", accum1.toString());

        DocumentType documentType2 = new DocumentType("name2", "", "systemId2", "baseUri2");
        StringBuilder accum2 = new StringBuilder();
        documentType2.outerHtmlHead(accum2, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html systemId2\">", accum2.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}
