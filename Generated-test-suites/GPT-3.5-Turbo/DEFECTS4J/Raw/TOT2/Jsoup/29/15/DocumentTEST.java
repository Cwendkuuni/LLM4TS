import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("http://example.com");
        assertNotNull(shellDoc);
        assertEquals("html", shellDoc.nodeName());
        assertNotNull(shellDoc.head());
        assertNotNull(shellDoc.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        Element newHead = new Element(Tag.valueOf("head"), document.baseUri());
        document.appendChild(newHead);
        assertEquals(newHead, document.head());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        Element newBody = new Element(Tag.valueOf("body"), document.baseUri());
        document.appendChild(newBody);
        assertEquals(newBody, document.body());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element newElement = document.createElement("div");
        assertNotNull(newElement);
        assertEquals("div", newElement.nodeName());
        assertEquals(document.baseUri(), newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Element head = document.appendElement("head");
        Element htmlEl = document.appendElement("html");
        htmlEl.prependElement("head");
        Element body = document.appendElement("body");
        
        // test normalise text nodes
        Node textNode = new Element(Tag.valueOf("p"), document.baseUri()).text("test text");
        document.appendChild(textNode);
        document.normalise();
        assertEquals(1, body.childNodes.size());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals(document.html(), outerHtml);
    }

    @Test
    public void testText() {
        String text = "test text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testClone() {
        Document clonedDoc = document.clone();
        assertNotNull(clonedDoc);
        assertEquals(document.outputSettings().charset(), clonedDoc.outputSettings().charset());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertEquals(0, outputSettings.indentAmount());
        outputSettings.indentAmount(4);
        assertEquals(4, outputSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}