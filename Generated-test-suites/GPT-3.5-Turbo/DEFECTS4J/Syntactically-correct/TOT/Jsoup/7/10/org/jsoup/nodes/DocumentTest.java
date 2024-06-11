import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("www.example.com");
    }

    // Test Case for constructor
    @Test
    public void testDocumentConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("www.example.com", document.baseUri());
    }

    // Test Case for createShell method
    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("www.example.com");
        assertEquals("html", shellDoc.head().parent().nodeName());
        assertNotNull(shellDoc.head());
        assertNotNull(shellDoc.body());
    }

    // Test Case for head method
    @Test
    public void testHead() {
        assertNull(document.head());
        Element head = document.appendElement("head");
        assertEquals(head, document.head());
    }

    // Test Case for body method
    @Test
    public void testBody() {
        assertNull(document.body());
        Element body = document.appendElement("body");
        assertEquals(body, document.body());
    }

    // Test Case for title method (typical use case)
    @Test
    public void testTitle_typical() {
        document.clear();
        document.appendElement("head").appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    // Test Case for title method (edge case - empty title)
    @Test
    public void testTitle_emptyTitle() {
        document.clear();
        document.appendElement("head").appendElement("title");
        assertEquals("", document.title());
    }
    
    // Test Case for title method (error scenario - no title element)
    @Test
    public void testTitle_noTitleElement() {
        document.clear();
        assertEquals("", document.title());
    }

    // Test Case for title method (error scenario - null title)
    @Test(expected = NullPointerException.class)
    public void testTitle_nullTitle() {
        document.title(null);
    }

    // Test Case for createElement method
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    // Test Case for normalise method
    @Test
    public void testNormalise() {
        document.clear();
        document.appendElement("head");
        document.appendElement("body");
        document.normalise();
        assertEquals("html", document.firstChild().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());
    }

    // Test Case for outerHtml method
    @Test
    public void testOuterHtml() {
        document.clear();
        document.appendElement("div").text("test");
        assertEquals("<div>test</div>", document.outerHtml());
    }

    // Test Case for text method
    @Test
    public void testText() {
        document.clear();
        document.appendElement("body");
        document.text("test");
        assertEquals("test", document.body().text());
    }

    // Test Case for nodeName method
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    // Test Case for outputSettings method
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals("UTF-8", outputSettings.charset().name());
        assertTrue(outputSettings.prettyPrint());
    }
}