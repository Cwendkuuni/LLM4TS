package org.jsoup.nodes;

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
        assertNotNull(shell.getElementsByTag("html"));
        assertNotNull(shell.getElementsByTag("head"));
        assertNotNull(shell.getElementsByTag("body"));
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        document.appendElement("head");
        head = document.head();
        assertNotNull(head);

        document.getElementsByTag("head").first().remove();
        head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        document.appendElement("body");
        body = document.body();
        assertNotNull(body);

        document.getElementsByTag("body").first().remove();
        body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.appendElement("head").appendElement("title").text("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        document.getElementsByTag("title").first().remove();
        title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        Element titleEl = document.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text());

        document.getElementsByTag("head").first().remove();
        document.title("Test Title");
        titleEl = document.getElementsByTag("head").first().getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text());
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());

        element = document.createElement("");
        assertNotNull(element);
        assertEquals("", element.tagName());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.getElementsByTag("html"));
        assertNotNull(document.getElementsByTag("head"));
        assertNotNull(document.getElementsByTag("body"));

        document.select("html").empty();
        document.normalise();
        assertNotNull(document.getElementsByTag("html"));
        assertNotNull(document.getElementsByTag("head"));
        assertNotNull(document.getElementsByTag("body"));

        document.select("head").remove();
        document.normalise();
        assertNotNull(document.getElementsByTag("head"));
        assertNotNull(document.getElementsByTag("body"));

        document.select("body").remove();
        document.normalise();
        assertNotNull(document.getElementsByTag("body"));
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);

        document.body().text("Test Body");
        outerHtml = document.outerHtml();
        assertEquals("Test Body", outerHtml);
    }

    @Test
    public void testText() {
        Element body = document.body();
        body.text("Test");
        assertEquals("Test", body.text());

        Element newBody = document.body();
        assertEquals(body, newBody);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }

}