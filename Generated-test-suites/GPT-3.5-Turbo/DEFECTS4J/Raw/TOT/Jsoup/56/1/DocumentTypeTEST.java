import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private static DocumentType documentType;

    @BeforeClass
    public static void setUp() {
        documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead_html5() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        String expected = "<!doctype";
        String actual = getOuterHtmlHead(outputSettings);
        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testOuterHtmlHead_nonHtml5() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        String expected = "<!DOCTYPE";
        String actual = getOuterHtmlHead(outputSettings);
        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testOuterHtmlHead_withName() {
        documentType.attr(DocumentType.NAME, "html");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        String expected = "<!DOCTYPE html";
        String actual = getOuterHtmlHead(outputSettings);
        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testOuterHtmlHead_withPublicId() {
        documentType.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD XHTML 1.0 Transitional//EN");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"";
        String actual = getOuterHtmlHead(outputSettings);
        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testOuterHtmlHead_withSystemId() {
        documentType.attr(DocumentType.SYSTEM_ID, "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        String expected = "<!DOCTYPE html \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"";
        String actual = getOuterHtmlHead(outputSettings);
        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testOuterHtmlTail() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, outputSettings);
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testHas_withBlankAttribute() {
        assertFalse(documentType.has(DocumentType.NAME));
    }

    @Test
    public void testHas_withNonBlankAttribute() {
        documentType.attr(DocumentType.NAME, "html");
        assertTrue(documentType.has(DocumentType.NAME));
    }

    private String getOuterHtmlHead(Document.OutputSettings outputSettings) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception occurred while getOuterHtmlHead");
        }
        return stringBuilder.toString();
    }
}