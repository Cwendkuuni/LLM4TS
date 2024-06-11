package org.jsoup.nodes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {
    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDocument = Document.createShell("http://example.com");
        assertNotNull(shellDocument);
        Element html = shellDocument.head();
        assertNotNull(html);
        Element body = shellDocument.body();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        document.title("Test Title");
        String newTitle = document.title();
        assertEquals("Test Title", newTitle);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document normalizedDoc = document.normalise();
        assertNotNull(normalizedDoc);
    }

    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }

    @Test
    public void testText() {
        document.text("Test Text");
        Element body = document.body();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
    }
}