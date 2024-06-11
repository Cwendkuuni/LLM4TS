package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("http://example.com");
        assertEquals("<html>\n" +
                         " <head></head>\n" +
                         " <body></body>\n" +
                         "</html>", shellDoc.html());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
        document.appendElement("head");
        assertNotNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
        document.appendElement("body");
        assertNotNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());
        document.appendElement("title").text("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.appendElement("head");
        document.title("New Title");
        assertEquals("<title>New Title</title>", document.head().html());
    }

    @Test
    public void testCreateElement() {
        assertEquals("<p></p>", document.createElement("p").toString());
    }

    @Test
    public void testNormalise() {
        document.normalise();
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testText() {
        document.text("This is a test");
        assertEquals("This is a test", document.text());
    }

    @Test
    public void testOuterHtml() {
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertNotSame(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings()
                .charset("UTF-16")
                .escapeMode(Entities.EscapeMode.extended)
                .indentAmount(2)
                .prettyPrint(false);
        document.outputSettings(outputSettings);
        assertSame(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        assertEquals(Document.QuirksMode.noQuirks, document.quirksMode());
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

    // Add more test cases for other methods if needed

}