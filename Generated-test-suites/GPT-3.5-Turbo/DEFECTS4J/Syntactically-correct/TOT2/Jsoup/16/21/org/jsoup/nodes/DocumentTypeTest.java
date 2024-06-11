import org.junit.Test;
import org.jsoup.nodes.DocumentType;

import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeName() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("html", documentType.nodeName());
    }

    @Test
    public void testDocumentTypePublicId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr("publicId"));
    }

    @Test
    public void testDocumentTypeSystemId() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("", documentType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "", "http://www.w3.org/TR/html4/strict.dtd");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
}