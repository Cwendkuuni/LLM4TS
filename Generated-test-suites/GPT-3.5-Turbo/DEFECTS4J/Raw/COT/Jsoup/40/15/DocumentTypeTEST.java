
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com/");

        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
        assertEquals("https://www.example.com/", docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com/");

        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com/");
        StringBuilder accum = new StringBuilder();
        int depth = 0;

        docType.outerHtmlHead(accum, depth, new Document.OutputSettings());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "https://www.example.com/");
        StringBuilder accum = new StringBuilder();
        int depth = 0;

        docType.outerHtmlTail(accum, depth, new Document.OutputSettings());

        assertEquals("", accum.toString());
    }
}
