import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://www.example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        Element headElement = document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals(headElement, head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        Element bodyElement = document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals(bodyElement, body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        document.title("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testTitleNull() {
        boolean exceptionThrown = false;
        try {
            document.title(null);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testTitleElementNotPresent() {
        document.getElementsByTag("title").remove();
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testText() {
        document.appendElement("body").appendElement("h1").text("Test Text");
        String text = document.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testTextNull() {
        boolean exceptionThrown = false;
        try {
            document.text(null);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testTextExistingNodes() {
        document.appendElement("body").appendText("Existing Text");
        document.text("Test Text");
        String text = document.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("body").appendElement("h1").text("Test Text");
        String outerHtml = document.outerHtml();
        assertEquals("<body><h1>Test Text</h1></body>", outerHtml);
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertEquals(document.baseUri(), clone.baseUri());
        assertEquals(document.head(), clone.head());
        assertEquals(document.body(), clone.body());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        doc.title("Test Title");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element body = document.appendElement("body");
        body.appendChild(new TextNode("Test Text 1", ""));
        body.appendChild(new TextNode("Test Text 2", ""));
        body.appendChild(new TextNode("Test Text 3", ""));
        document.normaliseTextNodes(body);
        assertEquals("Test Text 1 Test Text 2 Test Text 3", body.text());
    }

    @Test
    public void testNormaliseStructure() {
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element secondHead = html.appendElement("head");
        Element secondBody = html.appendElement("body");

        document.normaliseStructure("head", html);
        assertEquals(1, html.getElementsByTag("head").size());
        assertEquals(head, html.getElementsByTag("head").first());
        assertEquals(head.childNodes, secondHead.childNodes);
        assertEquals(0, html.getElementsByTag("body").size());

        document.normaliseStructure("body", html);
        assertEquals(1, html.getElementsByTag("body").size());
        assertEquals(body, html.getElementsByTag("body").first());
        assertEquals(body.childNodes, secondBody.childNodes);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        Element foundElement = document.findFirstElementByTagName("head", html);
        assertEquals(head, foundElement);

        foundElement = document.findFirstElementByTagName("body", html);
        assertEquals(body, foundElement);

        foundElement = document.findFirstElementByTagName("h1", html);
        assertNull(foundElement);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertTrue(outputSettings.prettyPrint());

        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        outputSettings = document.outputSettings();
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());

        document.quirksMode(Document.QuirksMode.noQuirks);
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
    }
}