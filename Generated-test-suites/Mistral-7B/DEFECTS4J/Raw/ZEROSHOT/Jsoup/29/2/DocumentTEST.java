package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Document.QuirksMode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author John Doe
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testDocumentCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertTrue(doc.head() != null);
        assertTrue(doc.body() != null);
    }

    @Test
    public void testDocumentGetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertNull(doc.title()); // default title should be empty
    }

    @Test
    public void testDocumentSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title 1");
        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testDocumentCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");
        assertEquals("div", element.nodeName());
    }

    @Test
    public void testDocumentNormalise() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Test Text 1");
        doc.head().appendElement(new Element(Tag.valueOf("meta"), doc.baseUri())).appendText("Test Meta 1");
        Element div = doc.createElement("div").appendText("Test Text 2");
        doc.body().appendElement(div);

        doc.normalise();

        assertEquals("Test Text 1Test Text 2", doc.body().text());
        assertNull(doc.head().child(0)); // meta element should have been merged with first head element
        assertEquals(4, doc.childNodes().size());
    }

    @Test
    public void testDocumentOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();
        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals(StandardCharsets.UTF_8, settings.charset());

        doc.outputSettings(new OutputSettings().charset("ISO-8859-1"));
        assertEquals("ISO-8859-1", settings.charset().name());

        doc.outputSettings(new OutputSettings().escapeMode(Entities.EscapeMode.extended));
        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());
    }

    @Test
    public void testDocumentQuirksMode() {
        Document doc = new Document("http://example.com");
        assertEquals(QuirksMode.noQuirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.quirks);
        assertEquals(QuirksMode.quirks, doc.quirksMode());

        doc.quirksMode(QuirksMode.limitedQuirks);
        assertEquals(QuirksMode.limitedQuirks, doc.quirksMode());
    }
}