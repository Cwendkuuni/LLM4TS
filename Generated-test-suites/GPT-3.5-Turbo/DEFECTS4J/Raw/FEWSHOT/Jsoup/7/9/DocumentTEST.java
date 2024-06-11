package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);

        Element headElement = document.appendElement("head");
        head = document.head();
        assertEquals(headElement, head);
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);

        Element bodyElement = document.appendElement("body");
        body = document.body();
        assertEquals(bodyElement, body);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Document");
        title = document.title();
        assertEquals("Test Document", title);
    }

    @Test
    public void testNormalise() {
        Document shell = Document.createShell("http://example.com");
        shell.normalise();

        Element html = shell.findFirstElementByTagName("html", shell);
        assertNotNull(html);

        Element head = shell.head();
        assertNotNull(head);

        Element body = shell.body();
        assertNotNull(body);

        shell.text("test text");
        String outerHtml = shell.outerHtml();
        assertEquals("test text", outerHtml);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
    }

    @Test
    public void testText() {
        document.appendElement("body").text("test text");

        String text = document.text();
        assertEquals("test text", text);
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings settings = document.outputSettings();
        assertNotNull(settings);

        Entities.EscapeMode escapeMode = settings.escapeMode();
        assertNotNull(escapeMode);

        Charset charset = settings.charset();
        assertNotNull(charset);

        CharsetEncoder encoder = settings.encoder();
        assertNotNull(encoder);

        boolean prettyPrint = settings.prettyPrint();
        assertEquals(true, prettyPrint);

        int indentAmount = settings.indentAmount();
        assertEquals(1, indentAmount);
    }

}