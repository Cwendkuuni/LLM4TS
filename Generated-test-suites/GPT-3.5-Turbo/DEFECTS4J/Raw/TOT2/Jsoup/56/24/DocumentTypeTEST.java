import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertNotNull(dt);
        assertEquals("html", dt.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", dt.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", dt.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertEquals("#doctype", dt.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, dt.outerHtmlHead());
    }

    @Test
    public void testHas() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
        assertTrue(dt.has("name"));
        assertTrue(dt.has("publicId"));
        assertTrue(dt.has("systemId"));
    }
}