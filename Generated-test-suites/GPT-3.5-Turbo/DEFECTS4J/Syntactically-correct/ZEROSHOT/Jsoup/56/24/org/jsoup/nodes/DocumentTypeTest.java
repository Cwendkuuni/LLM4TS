import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        String expected = "<!DOCTYPE doctype PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        OutputSettings outputSettings = new OutputSettings();
        documentType.outerHtmlTail(accum, 0, outputSettings);
        assertEquals("", accum.toString());
    }

    @Test
    public void testHas() {
        assertFalse(documentType.has("attribute"));
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.NAME));
    }
}