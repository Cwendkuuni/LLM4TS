import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" http://www.w3.org/TR/html4/loose.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    @Test
    public void testConstructor() {
        assertEquals("HTML", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", documentType.attr("systemId"));
    }
    
    // Test cases for potential edge cases and exceptions
    @Test
    public void testConstructorWithNullValues() {
        DocumentType documentType = new DocumentType(null, null, null, null);
        
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("HTML", "", "http://www.w3.org/TR/html4/loose.dtd", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/loose.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01 Transitional//EN", "", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("HTML", "", "", "http://www.example.com");

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

}