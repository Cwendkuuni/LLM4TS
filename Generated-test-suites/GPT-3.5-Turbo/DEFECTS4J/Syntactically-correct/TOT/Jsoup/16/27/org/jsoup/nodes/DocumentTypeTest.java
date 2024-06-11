package org.jsoup.nodes;

import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType docType;

    @Before
    public void setUp() {
        docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlHead(accum, 0, out);
        
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testOuterHtmlHead_BlankPublicId() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html \"http://www.w3.org/TR/REC-html40/strict.dtd\">");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.attr("publicId", "");

        docType.outerHtmlHead(accum, 0, out);
        
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testOuterHtmlHead_BlankSystemId() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.attr("systemId", "");

        docType.outerHtmlHead(accum, 0, out);
        
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testOuterHtmlHead_BlankPublicAndSystemId() {
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html>");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        docType.attr("publicId", "");
        docType.attr("systemId", "");

        docType.outerHtmlHead(accum, 0, out);
        
        assertEquals(expected.toString(), accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder expected = new StringBuilder();
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        docType.outerHtmlTail(accum, 0, out);
        
        assertEquals(expected.toString(), accum.toString());
    }

    // Add more test cases for the remaining methods

}