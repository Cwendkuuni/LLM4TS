package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com/");
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        String emptyTitle = document.title();
        assertEquals("", emptyTitle);

        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com/", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
        assertNotNull(normalizedDocument.head());
        assertNotNull(normalizedDocument.body());
        assertEquals("html", normalizedDocument.select("html").first().nodeName());
        assertEquals("head", normalizedDocument.head().nodeName());
        assertEquals("body", normalizedDocument.body().nodeName());
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
    }

    @Test
    public void testText() {
        Element body = document.body();
        String text = "Test text";
        
        document.text(text);
        String bodyText = body.text();
        assertEquals(text, bodyText);
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}