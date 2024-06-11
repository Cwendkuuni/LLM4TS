package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testNewDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testDocumentTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testDocumentTitleNull() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title(null);
        assertNull(doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element newElement = doc.createElement("div");
        assertEquals("div", newElement.nodeName());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.body().appendText("Test").appendText(" Text");
        doc.head().appendElement("title").text("Test Title");
        doc.head().appendText("Test ");
        doc.normalise();
        Element body = doc.body();
        Element title = doc.head().getElementsByTag("title").first();
        TextNode bodyText = (TextNode) body.childNodes().get(0);
        TextNode titleText = (TextNode) title.childNodes().get(0);
        assertEquals("Test Text", bodyText.text());
        assertEquals("Test Title", titleText.text());
    }

    @Test
    public void testNormaliseStructure() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element head1 = doc.head().appendElement("head");
        Element head2 = doc.appendElement("head");
        Element body = doc.body();
        body.appendText("Test");
        doc.normaliseStructure("head", doc);
        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(0, head1.childNodes().size());
        assertEquals(0, head2.childNodes().size());
        assertEquals("Test", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.head();
        head.appendElement("title").text("Test Title");
        Element body = doc.body();
        body.appendText("Test");
        Element title = doc.findFirstElementByTagName("title", doc);
        assertEquals("title", title.nodeName());
        assertEquals("Test Title", title.text());
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.head();
        head.appendElement("title").text("Test Title");
        Element body = doc.body();
        body.appendText("Test");
        doc.title("Test Title 2");
        doc.normalise();
        String expected = "<html><head><title>Test Title 2</title></head><body>Test</body></html>";
        assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.body().appendText("Test");
        doc.text("Test 2");
        assertEquals("Test 2", doc.text().get());
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document originalDoc = new Document(baseUri);
        OutputSettings originalOutputSettings = originalDoc.outputSettings();
        Document clonedDoc = originalDoc.clone();
        OutputSettings clonedOutputSettings = clonedDoc.outputSettings();
        assertEquals(originalOutputSettings.escapeMode(), clonedOutputSettings.escapeMode());
        assertEquals(originalOutputSettings.charset(), clonedOutputSettings.charset());
        assertNotEquals(originalOutputSettings, clonedOutputSettings);
    }
}