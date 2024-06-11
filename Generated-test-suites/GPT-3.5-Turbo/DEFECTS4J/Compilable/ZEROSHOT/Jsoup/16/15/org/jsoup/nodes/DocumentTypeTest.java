package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("", "", "", "");
        assertEquals("#doctype", dt.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithBlankPublicId() {
        DocumentType dt = new DocumentType("", "", "systemId", "");
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankSystemId() {
        DocumentType dt = new DocumentType("", "publicId", "", "");
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
        DocumentType dt = new DocumentType("", "publicId", "systemId", "");
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("", "", "", "");
        StringBuilder accum = new StringBuilder();
        dt.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}