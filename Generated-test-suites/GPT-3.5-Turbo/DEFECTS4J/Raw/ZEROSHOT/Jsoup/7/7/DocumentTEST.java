import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    private Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testHead() {
        assertNull(document.head());
    }
    
    @Test
    public void testBody() {
        assertNull(document.body());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testNormalise() {
        Document normalisedDocument = document.normalise();
        // Check if the document has "html" element
        assertNotNull(normalisedDocument.getElementsByTag("html"));
        // Check if the document has "head" element
        assertNotNull(normalisedDocument.head());
        // Check if the document has "body" element
        assertNotNull(normalisedDocument.body());
    }
    
    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
        document.appendElement("div");
        assertEquals("<div></div>", document.outerHtml());
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}