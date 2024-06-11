import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "https://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        try {
            StringBuilder sb = new StringBuilder();
            documentType.outerHtmlHead(sb, 0, new Document.OutputSettings().syntax(Syntax.html));
            assertEquals(expected, sb.toString());
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void hasMethodShouldReturnTrueWhenAttributeExists() {
        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void hasMethodShouldReturnFalseWhenAttributeDoesNotExist() {
        assertFalse(documentType.has("nonexistentAttribute"));
    }
}