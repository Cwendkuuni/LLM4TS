import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor1() {
        DocumentType documentType = new DocumentType("html5", "-//W3C//DTD HTML 5.0//EN", "http://www.w3.org/TR/html5/strict.dtd", "http://example.com");

        assertEquals("html5", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 5.0//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html5/strict.dtd", documentType.attr("systemId"));
        assertEquals("http://example.com", documentType.baseUri());
    }

    @Test
    public void testConstructor2() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        assertEquals("html", documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testConstructor3() {
        DocumentType documentType = new DocumentType("", "-//W3C//DTD XML 1.0//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "");

        assertEquals("", documentType.attr("name"));
        assertEquals("-//W3C//DTD XML 1.0//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.attr("systemId"));
        assertEquals("", documentType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");

        String expectedHtmlHead = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";

        assertEquals(expectedHtmlHead, documentType.outerHtmlHead());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");

        assertFalse(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
    }
}