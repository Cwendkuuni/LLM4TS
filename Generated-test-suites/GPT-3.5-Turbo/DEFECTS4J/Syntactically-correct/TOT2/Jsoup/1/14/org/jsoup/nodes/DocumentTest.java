import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document testDocument;

    @Before
    public void setup() {
        testDocument = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("<html><head></head><body></body></html>", shell.outerHtml());
    }

    @Test
    public void testHead() {
        Element head = testDocument.head();
        assertNull(head);
        Element testHead = testDocument.createElement("head");
        testDocument.appendChild(testHead);
        assertEquals(testHead, testDocument.head());
    }

    @Test
    public void testBody() {
        Element body = testDocument.body();
        assertNull(body);
        Element testBody = testDocument.createElement("body");
        testDocument.appendChild(testBody);
        assertEquals(testBody, testDocument.body());
    }

    @Test
    public void testTitleAndGetTitle() {
        String title = testDocument.title();
        assertEquals("", title);

        testDocument.title("Test Title");
        assertEquals("Test Title", testDocument.title());
    }

    // Test for normalise method can be added here

    // Test for createElement method can be added here

    // Test for text method can be added here

    // Test for nodeName method can be added here

    // Additional edge cases and exception handling tests can be added here

}