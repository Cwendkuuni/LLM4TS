package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertEquals("http://example.com", shell.baseUri());
        assertEquals("html", shell.getElementsByTag("html").first().tagName());
        assertEquals("head", shell.getElementsByTag("head").first().tagName());
        assertEquals("body", shell.getElementsByTag("body").first().tagName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testNormalise() {
        Element element1 = document.createElement("div");
        element1.text("Text1");
        document.append(element1);

        Element element2 = document.createElement("div");
        element2.text("Text2");
        document.head().appendChild(element2);

        document.normalise();

        assertEquals(0, document.getElementsByTag("div").size());
        assertEquals(1, document.body().getElementsByTag("div").size());
        assertEquals("Text1", document.body().getElementsByTag("div").first().text());
        assertEquals(1, document.body().textNodes().size());
        assertEquals("Text2", document.body().textNodes().get(0).text());
    }

    @Test
    public void testOuterHtml() {
        document.text("Test Text");
        String html = document.outerHtml();
        assertEquals("Test Text", html);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
}