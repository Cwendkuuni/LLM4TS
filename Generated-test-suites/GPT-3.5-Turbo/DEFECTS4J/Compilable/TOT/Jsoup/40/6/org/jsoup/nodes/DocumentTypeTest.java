package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyAttributes() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithOnlyNameAttribute() {
        documentType.attr("name", "HTML");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE HTML>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAttribute() {
        documentType.attr("name", "HTML");
        documentType.attr("publicId", "-//W3C//DTD HTML 4.0//EN");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdAttribute() {
        documentType.attr("name", "HTML");
        documentType.attr("systemId", "http://www.w3.org/TR/REC-html40/strict.dtd");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE HTML \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithAllAttributes() {
        documentType.attr("name", "HTML");
        documentType.attr("publicId", "-//W3C//DTD HTML 4.0//EN");
        documentType.attr("systemId", "http://www.w3.org/TR/REC-html40/strict.dtd");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}