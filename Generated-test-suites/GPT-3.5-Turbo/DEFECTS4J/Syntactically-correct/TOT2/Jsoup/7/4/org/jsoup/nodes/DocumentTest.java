import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }
    
    @After
    public void tearDown() {
        document = null;
    }
    
    @Test
    public void testCreateShell() {
        assertNotNull(document);
        assertEquals("http://example.com", document.baseUri());
        Element html = document.getElementsByTag("html").first();
        Element head = document.head();
        Element body = document.body();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testNormalise() {
        Element body = document.body();
        body.prependChild(document.createElement("div"));
        document.normalise();
        assertFalse(body.childNodes().contains(document.createElement("div")));
    }
    
    @Test
    public void testText() {
        document.text("Testing");
        assertEquals("Testing", document.body().text());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }
    
    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
    }

}