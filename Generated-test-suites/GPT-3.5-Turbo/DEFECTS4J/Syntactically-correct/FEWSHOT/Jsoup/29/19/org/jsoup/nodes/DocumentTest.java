
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);
        assertEquals("http://www.example.com", doc.baseUri());

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);

        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);

        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        String title = doc.title();
        assertEquals("", title);

        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);

        doc.appendElement("title").text("New Title");
        title = doc.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.appendElement("body");

        doc.text("Hello World");
        assertEquals("Hello World", doc.body().text());

        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        doc.appendElement("body").text("Test");

        String outerHtml = doc.outerHtml();
        assertEquals("<body>\n Test </body>", outerHtml);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://www.example.com");
        Element element = doc.createElement("div");

        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testClone() {
        Document doc1 = new Document("http://www.example.com");
        doc1.title("Test Title");

        Document doc2 = doc1.clone();

        assertEquals("http://www.example.com", doc2.baseUri());
        assertEquals("Test Title", doc2.title());
        assertEquals(doc1.outputSettings(), doc2.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.extended);
        assertEquals(Document.OutputSettings.EscapeMode.extended, outputSettings.escapeMode());

        outputSettings.charset("UTF-8");
        assertEquals("UTF-8", outputSettings.charset().name());

        outputSettings.prettyPrint(false);
        assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        assertEquals(2, outputSettings.indentAmount());

        Document doc = new Document("http://www.example.com");
        doc.outputSettings(outputSettings);
        assertEquals(outputSettings, doc.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://www.example.com");

        Document.QuirksMode quirksMode = Document.QuirksMode.noQuirks;
        doc.quirksMode(quirksMode);

        assertEquals(quirksMode, doc.quirksMode());
    }
}
