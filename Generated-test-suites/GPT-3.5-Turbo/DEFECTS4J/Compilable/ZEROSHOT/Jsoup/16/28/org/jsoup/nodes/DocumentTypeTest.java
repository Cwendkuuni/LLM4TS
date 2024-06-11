package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType docType;

    @Before
    public void setUp() {
        docType = new DocumentType("doctypeName", "publicId", "systemId", "baseUri");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withPublicIdOnly() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.attr("systemId", "");
        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withSystemIdOnly() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.attr("publicId", "");
        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_withNoPublicIdAndSystemId() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.attr("publicId", "");
        docType.attr("systemId", "");
        docType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

}