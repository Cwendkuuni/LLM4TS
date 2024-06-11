import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("https://www.example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("https://www.example.com");
        assertNotNull(shell);
        Element html = shell.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
        document.appendElement("head");
        head = document.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
        document.appendElement("body");
        body = document.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
        document.appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");
        String title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertTrue(normalizedDoc.select("html").size() > 0);
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertTrue(outerHtml.contains("<!DOCTYPE html>"));
        assertFalse(outerHtml.contains("<html"));
    }

    @Test
    public void testText() {
        document.appendElement("body");
        document.text("Test Text");
        Element body = document.body();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}