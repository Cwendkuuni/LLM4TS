package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        assertNotNull(html);
        assertEquals("head", html.nodeName());
        Element head = html.head();
        assertNotNull(head);
        assertEquals("body", head.nodeName());
        Element body = head.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
        assertEquals("", doc.title());
    }

    @Test
    public void testGetTitle() {
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTitleNull() {
        document.title(null);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        document.title("Test Title");
        document.appendText("Test Text");
        Element div = document.createElement("div");
        div.appendText("Div Text");
        document.body().appendChild(div);

        document.normalise();

        Element head = document.head();
        Element title = head.getElementsByTag("title").first();
        assertEquals("Test Title", title.text());

        Element body = document.body();
        assertEquals("Test Text", body.text());
        assertEquals(1, body.childNodes().size());
        assertEquals("div", body.childNodes().get(0).nodeName());
        assertEquals("Div Text", body.childNodes().get(0).text());
    }

    @Test
    public void testSetOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        outputSettings.prettyPrint(false);
        assertEquals(false, doc.outputSettings().prettyPrint());

        Charset utf8 = Charset.forName("UTF-8");
        outputSettings.charset(utf8);
        assertEquals(utf8, doc.outputSettings().charset());
    }

    @Test
    public void testParseWithOutputSettings() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><div>Test Text</div></body></html>", "UTF-8");
        assertEquals("Test Title", doc.title());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
    }
}