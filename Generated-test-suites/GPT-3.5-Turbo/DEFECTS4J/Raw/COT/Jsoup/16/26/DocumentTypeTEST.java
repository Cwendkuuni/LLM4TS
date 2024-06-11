import org.junit.*;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("", "", "", "");
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("", "", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicAndSystemId() {
        DocumentType documentType = new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("", "", "", "");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }

}