import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstruction() {
        // typical use case
        DocumentType dt1 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("html", dt1.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", dt1.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", dt1.attr("systemId"));
        assertEquals("http://www.example.com", dt1.baseUri());

        // empty name
        try {
            DocumentType dt2 = new DocumentType("", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("name must not be empty", e.getMessage());
        }
    }

    @Test
    public void testNodeName() {
        DocumentType dt = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("#doctype", dt.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        // no publicId and systemId
        DocumentType dt1 = new DocumentType("html", "", "", "http://www.example.com");
        assertEquals("<!DOCTYPE html>", dt1.outerHtmlHead());

        // with publicId and systemId
        DocumentType dt2 = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", dt2.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlTail() {
        // no implementation required as the method does nothing
    }

}