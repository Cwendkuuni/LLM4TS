package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DocumentTypeTest {

    private DocumentType docType;

    @Before
    public void setup() {
        docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.w3.org");
    }

    @Test
    public void testConstructor() {
        assertNotNull(docType);
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlHead(accum, depth, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        docType.outerHtmlTail(accum, depth, out);

        assertEquals("", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyName() {
        DocumentType invalidDocType = new DocumentType("", null, null, null);
    }

    @Test
    public void testConstructorWithEmptyPublicId() {
        DocumentType validDocType = new DocumentType("html", null, null, null);
        assertNull(validDocType.attr("publicId"));
    }

    @Test
    public void testConstructorWithEmptySystemId() {
        DocumentType validDocType = new DocumentType("html", null, null, null);
        assertNull(validDocType.attr("systemId"));
    }
}