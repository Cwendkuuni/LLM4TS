import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("http://example.com", shell.baseUri());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().parent().nodeName());
        assertEquals("head", shell.head().nodeName());
        assertEquals("html", shell.body().parent().nodeName());
        assertEquals("body", shell.body().nodeName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
        
        Element newHead = document.appendElement("head");
        head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        assertEquals(document, head.parent());
        assertEquals(document.head(), head);

        Element anotherHead = document.createElement("head");
        assertNull(anotherHead.parent());
        assertNull(anotherHead.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
        
        Element newBody = document.appendElement("body");
        body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals(document, body.parent());
        assertEquals(document.body(), body);

        Element anotherBody = document.createElement("body");
        assertNull(anotherBody.parent());
        assertNull(anotherBody.nodeName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
        
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("title", titleElement.nodeName());
        assertTrue(titleElement.text().isEmpty());

        document.title("New Title");
        assertEquals("New Title", document.title());

        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        html.appendElement("div").text("Hello");
        head.appendElement("title").text("Title");
        body.appendElement("p").text("Paragraph");

        doc.normalise();

        Element newHtml = doc.getElementsByTag("html").first();
        Element newHead = doc.getElementsByTag("head").first();
        Element newBody = doc.getElementsByTag("body").first();

        assertNotNull(newHtml);
        assertEquals("html", newHtml.nodeName());
        assertEquals(doc, newHtml.parent());
        assertEquals(newHtml, doc.html());
        
        assertNotNull(newHead);
        assertEquals("head", newHead.nodeName());
        assertEquals(newHtml, newHead.parent());
        assertEquals(newHead, doc.head());

        assertNotNull(newBody);
        assertEquals("body", newBody.nodeName());
        assertEquals(newHtml, newBody.parent());
        assertEquals(newBody, doc.body());

        assertEquals(0, newHtml.childNodes.size());
        assertTrue(newHtml.text().isEmpty());
        
        assertEquals(0, newHead.childNodes.size());
        assertTrue(newHead.text().isEmpty());
        
        assertEquals(2, newBody.childNodes.size());
        assertNull(newBody.textNodes().first().previousSibling());
        assertEquals("Hello", newBody.textNodes().first().text());
        assertEquals(" ", newBody.textNodes().get(1).text());
        assertEquals(" ", newBody.text());
        assertFalse(newBody.text().isEmpty());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.isEmpty());

        document.appendElement("div").text("Hello");
        outerHtml = document.outerHtml();
        assertEquals("<div>Hello</div>", outerHtml);
    }

    @Test
    public void testText() {
        Element body = document.appendElement("body");
        Element div = body.appendElement("div");
        Element p = div.appendElement("p");

        document.text("New Text");

        assertEquals("New Text", body.text());
        assertTrue(div.text().isEmpty());
        assertTrue(p.text().isEmpty());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        outputSettings.charset(Charset.forName("ISO-8859-1"));
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);

        assertEquals(Entities.EscapeMode.extended, outputSettings.escapeMode());
        assertEquals(Charset.forName("ISO-8859-1"), outputSettings.charset());
        assertFalse(outputSettings.prettyPrint());
        assertEquals(2, outputSettings.indentAmount());
    }

}