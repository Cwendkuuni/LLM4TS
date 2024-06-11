import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("html", docType.nodeName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testHasMethodWithBlankAttribute() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testOuterHtmlHeadMethodForHtmlDoctype() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        String expectedOutput = "<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        assertEquals(expectedOutput, docType.outerHtmlHead());
    }

    @Test
    public void testOuterHtmlHeadMethodForNonHtmlDoctype() {
        DocumentType docType = new DocumentType("foo", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        String expectedOutput = "<!DOCTYPE foo PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        assertEquals(expectedOutput, docType.outerHtmlHead());
    }

}