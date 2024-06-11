import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals(Tag.valueOf("#root"), document.tagName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        
        assertEquals(Tag.valueOf("html"), shell.tagName());
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("http://example.com", shell.baseUri());
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

        document.appendElement("title").text("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        
        assertEquals(Tag.valueOf("div"), element.tagName());
        assertEquals("http://example.com", element.baseUri());
        assertEquals("", element.text());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        Element div = doc.createElement("div").text("Test Div");
        doc.appendChild(div);

        doc.normalise();

        assertNull(doc.getElementsByTag("head").first());
        assertNotNull(doc.head());
        assertNotNull(doc.body());

        assertEquals("Test Div", doc.body().text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");

        Element div = doc.createElement("div");
        div.appendChild(doc.createTextNode(" Text Node 1 "));
        div.appendChild(doc.createTextNode(" "));
        div.appendChild(doc.createTextNode("Text Node 2"));
        
        doc.appendChild(div);

        doc.normaliseTextNodes(doc);

        assertEquals("Text Node 2 Text Node 1", doc.body().text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("http://example.com");

        Element head1 = doc.createElement("head");
        head1.appendChild(doc.createTextNode(" Head 1 "));
        head1.appendChild(doc.createElement("title").text("Title 1"));
        doc.appendChild(head1);

        Element head2 = doc.createElement("head");
        head2.appendChild(doc.createTextNode(" Head 2 "));
        head2.appendChild(doc.createElement("title").text("Title 2"));
        doc.appendChild(head2);

        doc.normaliseStructure("head", doc);

        assertNotNull(doc.head());
        assertEquals("Title 2", doc.title());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");

        Element body = doc.createElement("body");
        body.appendChild(doc.createElement("div").text("Test Div"));
        doc.appendChild(body);

        Element div = doc.findFirstElementByTagName("div", doc);

        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("Test Div", div.text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");

        html.appendChild(head);
        html.appendChild(body);
        doc.appendChild(html);

        assertEquals("<head></head>\n<body></body>", doc.outerHtml());
    }

    @Test
    public void testText() {
        assertNull(document.body());

        document.text("Test Text");

        assertNotNull(document.body());
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();

        assertNotSame(document, clone);
        assertEquals(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        document.outputSettings(outputSettings);

        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());

        document.quirksMode(Document.QuirksMode.quirks);

        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }
}