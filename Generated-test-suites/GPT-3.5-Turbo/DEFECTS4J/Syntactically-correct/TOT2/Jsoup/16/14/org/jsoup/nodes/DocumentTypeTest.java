import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "http://www.example.com");

        assertEquals("html", documentType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());

        assertEquals("", accum.toString());
    }
}