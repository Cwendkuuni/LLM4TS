
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentWithBaseUri() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
        assertNull(doc.getElementsByTag("title").first());
        assertEquals("", doc.outerHtml());
        assertNotNull(doc.outputSettings());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals(baseUri, doc.baseUri());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("", doc.title());
        assertNotNull(doc.getElementsByTag("title").first());
        assertEquals("<html><head><title></title></head><body></body></html>", doc.outerHtml());
        assertNotNull(doc.outputSettings());
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        doc.body().appendElement("div").text("Test Div");
        assertEquals("Test Div", doc.body().text());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.html("<html><title>Test Title</title><head></head><body>Test Body</body></html>");
        doc.normalise();
        assertEquals("Test Title", doc.title());
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", doc.outerHtml());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        doc.html("<html><head><title>Test Title</title></head><body>Test Body</body></html>");
        assertEquals("<html><head><title>Test Title</title></head><body>Test Body</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        outputSettings.escapeMode(Document.Entities.EscapeMode.extended);
        assertEquals(Document.Entities.EscapeMode.extended, outputSettings.escapeMode());
        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", outputSettings.charset().name());
        outputSettings.prettyPrint(false);
        assertEquals(false, outputSettings.prettyPrint());
        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());
    }
}
