import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("https://example.com");
    }
  
    @Test
    public void testDocumentConstructor() {
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("https://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://example.com");
        assertNotNull(shell);
        assertEquals("https://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        Element head = document.createElement("head");
        document.appendChild(head);
        assertEquals(head, document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        Element body = document.createElement("body");
        document.appendChild(body);
        assertEquals(body, document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        document.head().appendElement("title").text("Existing Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testTitleWithExistingTitleElementInIncorrectLocation() {
        document.body().appendElement("title").text("Existing Title");
        document.title("New Title");
        assertEquals("New Title", document.title());
        assertNull(document.head().getElementsByTag("title").first());
        assertNotNull(document.body().getElementsByTag("title").first());
    }

    @Test
    public void testNormalise() {
        Element head = document.createElement("head");
        Element body = document.createElement("body");
        document.appendChild(head);
        document.appendChild(body);

        Element div = document.createElement("div");
        body.appendChild(div);
        div.appendChild(new TextNode("Text 1", ""));
        div.appendChild(new TextNode("Text 2", ""));

        document.normalise();

        Element newBody = document.body();
        assertNotNull(newBody);
        Element newHead = document.head();
        assertNotNull(newHead);

        Element newDiv = newBody.getElementsByTag("div").first();
        assertNotNull(newDiv);
        assertTrue(newDiv.childNodes().get(0) instanceof TextNode);
        assertTrue(newDiv.childNodes().get(1) instanceof TextNode);
    }

    @Test
    public void testNormaliseWithoutBody() {
        Element head = document.createElement("head");
        document.appendChild(head);

        document.normalise();

        Element newBody = document.body();
        assertNotNull(newBody);
        Element newHead = document.head();
        assertNotNull(newHead);
    }

    @Test
    public void testOutlineHtml() {
        document.body().append("<p>Test Paragraph</p>");
        String html = document.html();
        assertEquals("<p>Test Paragraph</p>", html);
    }

    @Test
    public void testText() {
        document.body().text("Test Text");
        String text = document.text();
        assertEquals("Test Text", text);
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
    }

    @Test
    public void testCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
    }

    @Test
    public void testPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertTrue(outputSettings.prettyPrint());
        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }

}