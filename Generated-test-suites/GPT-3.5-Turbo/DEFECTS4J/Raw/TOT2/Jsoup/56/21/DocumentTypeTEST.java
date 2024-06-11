import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.attr("systemId"));
        assertEquals("http://example.com", doctype.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        OutputSettings out = new OutputSettings();
        StringBuilder sb = new StringBuilder();
        try {
            doctype.outerHtmlHead(sb, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        OutputSettings out = new OutputSettings();
        StringBuilder sb = new StringBuilder();
        doctype.outerHtmlTail(sb, 0, out);
        assertEquals("", sb.toString());
    }

    @Test
    public void testHas() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertTrue(doctype.has("name"));
        assertTrue(doctype.has("publicId"));
        assertTrue(doctype.has("systemId"));
        assertFalse(doctype.has("description"));
    }
}