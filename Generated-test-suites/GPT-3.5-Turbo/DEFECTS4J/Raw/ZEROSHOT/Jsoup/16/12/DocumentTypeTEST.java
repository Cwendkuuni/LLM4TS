package org.jsoup.nodes;

import org.jsoup.helper.StringUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType1 = new DocumentType("", "", "", "");
        StringBuilder accum1 = new StringBuilder();
        documentType1.outerHtmlHead(accum1, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html>", accum1.toString());

        DocumentType documentType2 = new DocumentType("", "publicId", "", "");
        StringBuilder accum2 = new StringBuilder();
        documentType2.outerHtmlHead(accum2, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum2.toString());

        DocumentType documentType3 = new DocumentType("", "", "systemId", "");
        StringBuilder accum3 = new StringBuilder();
        documentType3.outerHtmlHead(accum3, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html systemId\">", accum3.toString());

        DocumentType documentType4 = new DocumentType("", "publicId", "systemId", "");
        StringBuilder accum4 = new StringBuilder();
        documentType4.outerHtmlHead(accum4, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId\">", accum4.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}