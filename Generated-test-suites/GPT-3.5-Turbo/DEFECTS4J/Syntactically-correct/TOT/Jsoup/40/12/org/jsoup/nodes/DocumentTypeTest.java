import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertNotNull(doctype);
    }

    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoPublicId() {
        DocumentType doctype = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);

        String expected = "<!DOCTYPE html \"http://www.w3.org/TR/html4/strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_NoSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", null, "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlHead(accum, 0, outputSettings);

        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        doctype.outerHtmlTail(accum, 0, outputSettings);

        assertTrue(accum.toString().isEmpty());
    }
}