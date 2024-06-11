package org.jsoup.nodes;

import org.junit.Assert;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
    }

    @Test
    public void testHeadAccess() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();
        Assert.assertNotNull(head);
    }

    @Test
    public void testBodyAccess() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();
        Assert.assertNotNull(body);
    }

    @Test
    public void testTitleAccess() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSet() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertEquals("Test Title", titleEl.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element link = doc.createElement("a");
        Assert.assertEquals("a", link.tagName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        doc.body().appendText("Test 1");
        doc.head().appendText("Test 2");
        doc.text("Test 3");
        doc.normalise();
        Element body = doc.body();
        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }
        Assert.assertEquals(3, textNodes.size());
        Assert.assertEquals("Test 1Test 2Test 3", body.text().trim());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.findFirstElementByTagName("html", doc);
        Assert.assertNotNull(html);
    }

    @Test
    public void testSetAndGetOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings outputSettings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        doc.outputSettings(new OutputSettings().charset("ISO-8859-1"));
        Assert.assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals(QuirksMode.noQuirks, doc.quirksMode());
        doc.quirksMode(QuirksMode.quirks);
        Assert.assertEquals(QuirksMode.quirks, doc.quirksMode());
    }
}