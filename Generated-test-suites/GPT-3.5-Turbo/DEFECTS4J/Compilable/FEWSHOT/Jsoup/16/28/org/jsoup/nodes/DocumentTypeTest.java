package org.jsoup.nodes;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private static DocumentType docType;

    @BeforeClass
    public static void setUp() {
        docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
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
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}