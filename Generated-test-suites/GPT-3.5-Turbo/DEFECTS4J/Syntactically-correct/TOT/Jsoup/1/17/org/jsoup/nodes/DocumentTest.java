package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private String baseUri;
    private Document doc;

    @Before
    public void setup() {
        baseUri = "http://example.com";
        doc = new Document(baseUri);
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell(baseUri);

        assertNotNull("Shell document should not be null", shell);
        assertNotNull("Shell document head element should not be null", shell.head());
        assertNotNull("Shell document body element should not be null", shell.body());
    }

    @Test
    public void testHead() {
        doc.appendElement("head");

        Element head = doc.head();

        assertNotNull("Head element should not be null", head);
        assertEquals("Head element tag should be 'head'", "head", head.nodeName());
    }

    @Test
    public void testBody() {
        doc.appendElement("body");

        Element body = doc.body();

        assertNotNull("Body element should not be null", body);
        assertEquals("Body element tag should be 'body'", "body", body.nodeName());
    }

    @Test
    public void testTitleWithExistingTitleElement() {
        doc.head().appendElement("title").text("Old Title");

        String title = doc.title();
        assertEquals("Title should be 'Old Title'", "Old Title", title);
    }

    @Test
    public void testTitleWithNoTitleElement() {
        String title = doc.title();
        assertEquals("Title should be an empty string", "", title);
    }

    @Test
    public void testSetTitleWithNoTitleElement() {
        doc.title("New Title");

        String title = doc.title();
        assertEquals("Title should be 'New Title'", "New Title", title);
    }

    @Test
    public void testSetTitleWithExistingTitleElement() {
        doc.head().appendElement("title").text("Old Title");

        doc.title("New Title");

        String title = doc.title();
        assertEquals("Title should be 'New Title'", "New Title", title);
    }

    @Test
    public void testCreateElement() {
        Element element = doc.createElement("div");

        assertNotNull("Created element should not be null", element);
        assertEquals("Created element tag should be 'div'", "div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        doc.normalise();

        assertNotNull("Html element should exist", doc.getElementsByTag("html").first());
        assertNotNull("Head element should exist", doc.head());
        assertNotNull("Body element should exist", doc.body());
    }

    @Test
    public void testText() {
        String text = "Lorem ipsum dolor sit amet.";

        doc.text(text);

        assertEquals("Text of body element should be the input text", text, doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = doc.outerHtml();

        assertEquals("Outer HTML should be equal to HTML", doc.html(), outerHtml);
    }

    @Test
    public void testNodeName() {
        String nodeName = doc.nodeName();

        assertEquals("Node name should be '#document'", "#document", nodeName);
    }
}