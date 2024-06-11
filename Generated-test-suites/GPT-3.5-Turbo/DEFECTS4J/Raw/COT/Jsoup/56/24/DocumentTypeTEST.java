import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("DOCTYPE", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE DOCTYPE PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testHasWithBlankAttribute() {
        documentType.attr(DocumentType.NAME, " ");
        assertFalse(documentType.has(DocumentType.NAME));
    }

    @Test
    public void testHasWithoutAttribute() {
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
    }

}