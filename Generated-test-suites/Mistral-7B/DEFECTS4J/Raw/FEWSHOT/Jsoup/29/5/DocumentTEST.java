package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals(Tag.valueOf("#root"), doc.tag());
        assertEquals("baseUri", doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");
        assertEquals(Tag.valueOf("#root"), doc.tag());
        assertEquals("baseUri", doc.baseUri());
        assertEquals(Tag.valueOf("html"), doc.head().tag());
        assertEquals(Tag.valueOf("head"), doc.head().head().tag());
        assertEquals(Tag.valueOf("body"), doc.body().tag());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertEquals(Tag.valueOf("head"), head.tag());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertEquals(Tag.valueOf("body"), body.tag());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("title");
        assertEquals("title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
        assertNull(doc.title());
        doc.title("title");
        assertEquals("title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("tagName");
        assertEquals(Tag.valueOf("tagName"), element.tag());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("baseUri");
        doc.head().appendElement("title").text("title");
        doc.head().appendElement("title").text("title2");
        doc.appendElement("p").text("text1");
        doc.appendElement("p").text("text2");
        Element body = doc.body();
        body.appendElement("p").text("text3");

        doc.normalise();

        assertEquals(3, doc.getElementsByTag("html").size());
        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(1, doc.getElementsByTag("title").size());
        assertEquals(4, doc.getElementsByTag("body").size());
        assertEquals("title", doc.title());
        assertEquals(3, doc.getElementsByTag("p").size());
        assertEquals("text1", doc.getElementsByTag("p").get(0).text());
        assertEquals("text3", doc.getElementsByTag("p").get(2).text());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("baseUri");
        Element root = doc.appendElement("div");
        root.appendText(" text1");
        root.appendText(" text2");
        root.appendElement("p").text("text3");

        doc.normalise();

        assertEquals(3, doc.getElementsByTag("div").size());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals(" text1text2", doc.getElementsByTag("div").get(0).text());
        assertEquals("text3", doc.getElementsByTag("p").get(0).text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("baseUri");
        Element head = doc.appendElement("head");
        Element title1 = head.appendElement("title").text("title1");
        Element title2 = head.appendElement("title").text("title2");
        Element body = doc.appendElement("body");
        Element p1 = body.appendElement("p").text("p1");
        Element p2 = body.appendElement("p").text("p2");

        doc.normalise();

        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(1, doc.getElementsByTag("title").size());
        assertEquals("title1", doc.getElementsByTag("title").get(0).text());
        assertEquals(1, doc.getElementsByTag("body").size());
        assertEquals(1, doc.getElementsByTag("p").size());
        assertEquals("p1p2", doc.getElementsByTag("body").get(0).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element root = doc.appendElement("html").appendElement("body");
        Element h1 = root.appendElement("h1").text("h1");
        Element p1 = root.appendElement("p").text("p1");
        Element h2 = root.appendElement("h2").text("h2");

        Element found = doc.findFirstElementByTagName("h1", root);
        assertEquals(h1, found);

        found = doc.findFirstElementByTagName("h2", root);
        assertEquals(h2, found);

        found = doc.findFirstElementByTagName("h3", root);
        assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element root = doc.appendElement("html").appendElement("body");
        Element h1 = root.appendElement("h1").text("h1");
        Element p1 = root.appendElement("p").text("p1");

        assertEquals("<html><body><h1>h1</h1><p>p1</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        Element root = doc.appendElement("html").appendElement("body");
        Element p1 = root.appendElement("p").text("p1");

        doc.text("text");
        assertEquals("text", p1.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);

        Document clone = doc.clone();
        assertEquals(outputSettings, clone.outputSettings());
    }

    @Test
    public void testOutputSettingsEscapeMode() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());
    }

    @Test
    public void testOutputSettingsCharset() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals("UTF-8", outputSettings.charset().name());

        outputSettings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", doc.outputSettings().charset().name());
    }

    @Test
    public void testOutputSettingsPrettyPrint() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertTrue(outputSettings.prettyPrint());

        outputSettings.prettyPrint(false);
        assertFalse(doc.outputSettings().prettyPrint());
    }

    @Test
    public void testOutputSettingsIndentAmount() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        assertEquals(1, outputSettings.indentAmount());

        outputSettings.indentAmount(2);
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}