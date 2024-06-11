package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType doctype;

    @Before
    public void setup() {
        doctype = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org/1999/xhtml");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            doctype.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlTail(sb, 0, outputSettings);
        assertTrue(sb.toString().isEmpty());
    }

    @Test
    public void testHas() {
        assertTrue(doctype.has(DocumentType.PUBLIC_ID));
        assertTrue(doctype.has(DocumentType.SYSTEM_ID));
        assertFalse(doctype.has(DocumentType.NAME));
    }

}