package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    // Test the constructor
    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("doctypeName", "publicId", "systemId", "baseUri");

        assertEquals("doctypeName", dt.attr("name"));
        assertEquals("publicId", dt.attr("publicId"));
        assertEquals("systemId", dt.attr("systemId"));
        assertEquals("baseUri", dt.baseUri());
    }

    // Test the nodeName() method
    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("doctypeName", "publicId", "systemId", "baseUri");

        assertEquals("#doctype", dt.nodeName());
    }

    // Test the outerHtmlHead() method when publicId is not blank
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType dt = new DocumentType("doctypeName", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        dt.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }

    // Test the outerHtmlHead() method when publicId is blank
    @Test
    public void testOuterHtmlHeadWithoutPublicId() {
        DocumentType dt = new DocumentType("doctypeName", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        dt.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html >", accum.toString());
    }

    // Test the outerHtmlHead() method when systemId is not blank
    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType dt = new DocumentType("doctypeName", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        dt.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }

    // Test the outerHtmlHead() method when systemId is blank
    @Test
    public void testOuterHtmlHeadWithoutSystemId() {
        DocumentType dt = new DocumentType("doctypeName", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        dt.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >", accum.toString());
    }

    // Test the outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("doctypeName", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        dt.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }
}