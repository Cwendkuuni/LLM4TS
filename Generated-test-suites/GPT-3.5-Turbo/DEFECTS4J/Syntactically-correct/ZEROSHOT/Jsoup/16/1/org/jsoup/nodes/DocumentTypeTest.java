package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit test class for DocumentType.
 */
public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("name", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("name", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        String expected = "<!DOCTYPE html PUBLIC 'publicId' 'systemId'>";
        assertEquals(expected, accum.toString());
    }
    
    // Add additional test methods here to cover all methods in DocumentType
    
}