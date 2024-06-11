import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        assertEquals("html5", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 5.0//EN", doctype.attr("publicId"));
        assertEquals(null, doctype.attr("systemId"));
        assertEquals("http://www.example.com", doctype.baseUri());
    }

    @Test
    public void testConstructorWithSystemId() {
        DocumentType doctype = new DocumentType("xhtml", null, "http://www.example.com/xhtml.dtd", "http://www.example.com");
        assertEquals("xhtml", doctype.attr("name"));
        assertEquals(null, doctype.attr("publicId"));
        assertEquals("http://www.example.com/xhtml.dtd", doctype.attr("systemId"));
        assertEquals("http://www.example.com", doctype.baseUri());
    }

    @Test
    public void testConstructorWithEmptyValues() {
        DocumentType doctype = new DocumentType("", "", "", "");
        assertEquals("", doctype.attr("name"));
        assertEquals("", doctype.attr("publicId"));
        assertEquals("", doctype.attr("systemId"));
        assertEquals("", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        assertEquals("<!doctype html5>", doctype.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        doctype.attr("publicId", "-//W3C//DTD XHTML 1.0 Strict//EN");
        assertEquals("<!DOCTYPE html5 PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\">", doctype.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        doctype.attr("systemId", "http://www.example.com/xhtml.dtd");
        assertEquals("<!DOCTYPE html5 \"http://www.example.com/xhtml.dtd\">", doctype.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadWithPublicAndSystemId() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        doctype.attr("publicId", "-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.attr("systemId", "http://www.example.com/xhtml.dtd");
        assertEquals("<!DOCTYPE html5 PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.example.com/xhtml.dtd\">", doctype.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadForNonHtmlSyntax() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setSyntax(Syntax.xhtml);
        assertEquals("<!DOCTYPE html5>", doctype.outerHtmlHead(outputSettings));
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType doctype = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", null, "http://www.example.com");
        assertTrue(doctype.has("name"));
        assertTrue(doctype.has("publicId"));
        assertFalse(doctype.has("systemId"));
    }
}