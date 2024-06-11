import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor_withValidValues() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertEquals("http://www.example.com", docType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructor_withEmptyName() {
        new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructor_withNullName() {
        new DocumentType(null, "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_withPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", docType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHead_withOnlyName() {
        DocumentType docType = new DocumentType("html", "", "", "http://www.example.com");
        assertEquals("<!DOCTYPE html>", docType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHead_withNoAttributes() {
        DocumentType docType = new DocumentType("", "", "", "http://www.example.com");
        assertEquals("<!DOCTYPE>", docType.outerHtmlHead());
    }

}