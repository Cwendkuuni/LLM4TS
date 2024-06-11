
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html5", "", "", "");
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html5", "", "", "");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Syntax.html);
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException thrown");
        }
        assertEquals("<!doctype", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Syntax.html);
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException thrown");
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
                     "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html5", "", "", "");
        Appendable accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Syntax.html);
        docType.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("html5", "", "", "");
        assertFalse(docType.has("name"));
        assertFalse(docType.has("publicId"));
        assertFalse(docType.has("systemId"));

        docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                                    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "");
        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));
    }
}

