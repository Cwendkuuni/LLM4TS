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
        documentType = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/TR/html4/");
    }

    @Test
    public void testNodeName() {
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadHtmlSyntax() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        
        assertEquals("<!doctype", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadXmlSyntax() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        
        assertEquals("<!DOCTYPE", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoPublicId() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        documentType.attr(DocumentType.PUBLIC_ID, "");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        
        assertEquals("<!doctype html", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNoSystemId() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        documentType.attr(DocumentType.SYSTEM_ID, "");
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlHead(stringBuilder, 0, outputSettings);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        documentType.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testHasWithAttribute() {
        documentType.attr(DocumentType.PUBLIC_ID, "-//W3C//DTD HTML 4.01//EN");
        
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testHasWithEmptyAttribute() {
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
    }
}