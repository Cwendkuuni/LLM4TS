package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        String expected = "#doctype";
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals(expected, docType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        String expected = "<!DOCTYPE html PUBLIC \"publicId\">";
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, out);
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        String expected = "<!DOCTYPE html systemId\">";
        DocumentType docType = new DocumentType("name", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, out);
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId\">";
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, out);
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        String expected = "";
        DocumentType docType = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, out);
        assertEquals(expected, accum.toString());
    }
}