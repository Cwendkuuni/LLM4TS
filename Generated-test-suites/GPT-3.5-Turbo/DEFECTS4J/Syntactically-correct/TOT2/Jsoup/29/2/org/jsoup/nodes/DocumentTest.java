import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDocument = Document.createShell("https://www.example.com");
        assertNotNull(shellDocument);
        Element html = shellDocument.head();
        assertNotNull(html);
        Element head = html.head();
        assertNotNull(head);
        Element body = html.body();
        assertNotNull(body);
    }

    @Test
    public void testAccessHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testAccessBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testGetTitle() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
    }

    @Test
    public void testText() {
        Element body = document.body();
        document.text("Test text");
        assertEquals("Test text", body.text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

    @Test
    public void testClone() {
        Document clonedDocument = document.clone();
        assertNotNull(clonedDocument);
        assertNotSame(document, clonedDocument);
    }

    @Test
    public void testOutputSettings() {
        OutputSettings outputSettings = new OutputSettings();
        
        outputSettings.charset("UTF-16");
        assertEquals("UTF-16", outputSettings.charset().name());
        
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
        
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}