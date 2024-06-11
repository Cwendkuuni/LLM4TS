
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    
    private Document document;

    @Before
    public void setup() {
        document = new Document("");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("");
        Element html = shell.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();

        assertEquals("html", html.tagName());
        assertEquals("head", head.tagName());
        assertEquals("body", body.tagName());
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
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNull() {
        document.title(null);
    }

    @Test
    public void testTitleElement() {
        document.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitleElementNotExist() {
        assertEquals("", document.title());
        document.title("Test Title");
        Element title = document.head().getElementsByTag("title").first();
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertTrue(document.select("html").size() == 1);
        assertTrue(document.head() != null);
        assertTrue(document.body() != null);
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("div").html("<p>Test</p>");
        assertEquals("<div><p>Test</p></div>", document.outerHtml());
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

    @Test
    public void testElement() {
        Element element = document.createElement("div");
        assertEquals("div", element.tagName());
        assertEquals("", element.text());
        assertEquals("", element.html());
    }
}
