
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell);
        assertNotNull(shell.head());
        assertNotNull(shell.body());
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
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testNormalise() {
        Document normalizedDocument = document.normalise();
        assertNotNull(normalizedDocument);
        assertNotNull(normalizedDocument.head());
        assertNotNull(normalizedDocument.body());
        assertEquals("head", normalizedDocument.head().nodeName());
        assertEquals("body", normalizedDocument.body().nodeName());
        assertTrue(normalizedDocument.select("html").size() > 0);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertNotNull(outerHtml);
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        String nodeName = document.nodeName();
        assertEquals("#document", nodeName);
    }
}
