import org.junit.Before;
import org.junit.Test;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        documentType.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testHasReturnsTrueForExistingAttribute() {
        assertTrue(documentType.has("name"));
    }

    @Test
    public void testHasReturnsFalseForNonExistingAttribute() {
        assertFalse(documentType.has("nonExistingAttribute"));
    }
}