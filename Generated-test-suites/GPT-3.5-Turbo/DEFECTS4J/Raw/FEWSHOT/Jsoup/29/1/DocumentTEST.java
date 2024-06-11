package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("https://www.example.com");
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
        assertEquals("head", document.head().nodeName());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
        assertEquals("body", document.body().nodeName());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());

        document.title("Test Title");
        assertEquals("Test Title", document.title());

        document.getElementsByTag("title").remove();
        assertEquals("", document.title());
    }

    @Test
    public void testTitle_Null() {
        document.getElementsByTag("title").remove();
        assertNull(document.getElementsByTag("title").first());

        document.title(null);
        assertNotNull(document.getElementsByTag("title").first());
        assertEquals("", document.title());
    }

    @Test
    public void testTitle_Empty() {
        document.getElementsByTag("title").remove();
        assertNull(document.getElementsByTag("title").first());

        document.title("");
        assertNotNull(document.getElementsByTag("title").first());
        assertEquals("", document.title());
    }

    @Test
    public void testTitle_Whitespace() {
        document.getElementsByTag("title").remove();
        assertNull(document.getElementsByTag("title").first());

        document.title("    ");
        assertNotNull(document.getElementsByTag("title").first());
        assertEquals("", document.title());
    }

    @Test
    public void testOuterHtml() {
        assertFalse(document.outerHtml().isEmpty());
        assertEquals(document.html(), document.outerHtml());
    }

    @Test
    public void testText() {
        String text = "Test Text";
        document.text(text);
        assertEquals(text, document.body().text());
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        Element element = document.createElement(tagName);
        assertNotNull(element);
        assertEquals(tagName, element.nodeName());
        assertEquals(document.baseUri(), element.baseUri());
    }

    @Test
    public void testNormalise() {
        assertNull(document.head());
        assertNull(document.body());

        document.normalise();

        assertNotNull(document.head());
        assertNotNull(document.body());

        assertEquals("html", document.head().parent().nodeName());
        assertEquals("html", document.body().parent().nodeName());
    }
}