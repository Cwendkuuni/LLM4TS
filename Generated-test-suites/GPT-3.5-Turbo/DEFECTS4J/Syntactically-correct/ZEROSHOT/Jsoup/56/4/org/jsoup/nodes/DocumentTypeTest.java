import org.jsoup.nodes.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.OutputSettings.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    private DocumentType documentType;

    @Before
    public void setUp() {
        documentType = new DocumentType("doctype", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testHas_PublicId() {
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testHas_SystemId() {
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testHas_Name() {
        assertTrue(documentType.has(DocumentType.NAME));
    }

    @Test
    public void testOuterHtmlHead_SyntaxHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.html);
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }

        assertEquals("<!doctype", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_SyntaxXhtml() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.xhtml);
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }

        assertEquals("<!DOCTYPE", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_NoPublicId() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.xml);
        documentType.attr(DocumentType.PUBLIC_ID, "");
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }

        assertEquals("<!DOCTYPE doctype \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_NoSystemId() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.xml);
        documentType.attr(DocumentType.SYSTEM_ID, "");
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }

        assertEquals("<!DOCTYPE doctype PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHead_NoPublicId_NoSystemId() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.xml);
        documentType.attr(DocumentType.PUBLIC_ID, "");
        documentType.attr(DocumentType.SYSTEM_ID, "");
        try {
            documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }

        assertEquals("<!DOCTYPE doctype>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder stringBuilder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings(Syntax.html);
        documentType.outerHtmlTail(stringBuilder, 0, outputSettings);

        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testConstructor() {
        assertEquals("doctype", documentType.attr(DocumentType.NAME));
        assertEquals("-//W3C//DTD HTML 4.01//EN", documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.attr(DocumentType.SYSTEM_ID));
    }

    // Additional test methods can be added here

}