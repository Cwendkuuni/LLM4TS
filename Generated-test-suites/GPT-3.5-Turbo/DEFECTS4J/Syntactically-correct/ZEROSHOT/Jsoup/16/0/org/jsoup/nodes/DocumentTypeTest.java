package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType docType = new DocumentType("name", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        DocumentType docType = new DocumentType("name", "", "http://www.w3.org/TR/REC-html40/strict.dtd", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, out);
        assertTrue(accum.toString().isEmpty());
    }
}