import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("html", dt.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", dt.attr("systemId"));
        assertEquals("http://example.org", dt.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("#doctype", dt.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", dt.outerHtmlHead());
    }

    @Test
    public void testHasMethod() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.org");
        assertTrue(dt.has("name"));
        assertTrue(dt.has("publicId"));
        assertTrue(dt.has("systemId"));
        assertFalse(dt.has("foo"));
    }
}