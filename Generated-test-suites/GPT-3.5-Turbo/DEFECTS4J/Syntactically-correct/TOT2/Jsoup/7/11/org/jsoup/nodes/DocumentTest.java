import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://www.example.com");
    }

    @Test
    public void testDocumentCreation() {
        assertNotNull(document);
        assertEquals(document.baseUri(), "http://www.example.com");
        assertEquals(document.nodeName(), "#root");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://www.example.com");
        assertNotNull(shell);
        Element html = shell.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals(title, "");

        document.title("Document Title");
        assertEquals(document.title(), "Document Title");
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals(element.tagName(), "div");
        assertNull(element.parent());
    }

    @Test
    public void testNormalise() {
        Element element = document.createElement("span");
        element.text("Text content");
        document.appendChild(element);

        document.normalise();

        Element html = document.getElementsByTag("html").first();
        Element body = html.getElementsByTag("body").first();
        List<Element> textNodes = body.textNodes();
        assertEquals(textNodes.size(), 2);
        assertEquals(textNodes.get(0).text(), "Text content");
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings settings = document.outputSettings();
        assertNotNull(settings);
        assertEquals(settings.charset(), Charset.forName("UTF-8"));

        settings.charset("ISO-8859-1");
        assertEquals(settings.charset(), Charset.forName("ISO-8859-1"));

        assertEquals(settings.prettyPrint(), true);
        settings.prettyPrint(false);
        assertEquals(settings.prettyPrint(), false);
    }
}