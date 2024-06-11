import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        Element html = document.elementSibling(0);
        assertEquals("html", html.tagName());
        Element head = html.elementSibling(0);
        assertEquals("head", head.tagName());
        Element body = head.elementSibling(0);
        assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testGetHeadElement() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
        Element head = document.head();
        assertEquals("head", head.tagName());
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test", title.text());
    }

    @org.junit.Test
    public void testGetBodyElement() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
        Element body = document.body();
        assertEquals("body", body.tagName());
        assertTrue(body.children().isEmpty());
    }

    @org.junit.Test
    public void testGetTitle() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>", "http://example.com");
        String title = document.title();
        assertEquals("Test", title);
    }

    @org.junit.Test
    public void testSetTitle() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        document.title("Test");
        String title = document.title();
        assertEquals("Test", title);
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html><body></body></html>", "http://example.com");
        Element div = document.createElement("div");
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri());
    }

    @org.junit.Test
    public void testNormaliseDocument() {
        Document document = Jsoup.parse("<html><head></head><body></body><p>Text</p><head><title>Title</title></head></html>", "http://example.com");
        document.normalise();
        List<Element> elements = document.getElementsByTag("body");
        assertEquals(1, elements.size());
        Element body = elements.get(0);
        List<Node> textNodes = body.childNodes();
        assertEquals(2, textNodes.size());
        TextNode firstTextNode = (TextNode) textNodes.get(0);
        assertEquals("Text", firstTextNode.text());
        TextNode secondTextNode = (TextNode) textNodes.get(1);
        assertTrue(secondTextNode.text().isEmpty());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Text</p></body></html>", "http://example.com");
        Element head = document.findFirstElementByTagName("head", document);
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test", title.text());
    }

    @org.junit.Test
    public void testOuterHtml() {
        Document document = Jsoup.parse("<html><head></head><body><p>Text</p></body></html>", "http://example.com");
        String outerHtml = document.outerHtml();
        assertEquals("<html><head></head><body><p>Text</p></body></html>", outerHtml);
    }

    @org.junit.Test
    public void testText() {
        Document document = Jsoup.parse("<html><head></head><body></body></html>", "http://example.com");
        document.body().text("Text");
        String text = document.body().text();
        assertEquals("Text", text);
    }

}