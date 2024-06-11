package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testCreateEmptyDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.title());
        assertTrue(doc.head() == null);
        assertTrue(doc.body() == null);
    }

    @Test
    public void testCreateShellDocument() {
        Document doc = Document.createShell("baseUri");

        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.title());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals(3, doc.childElementsSize());
    }

    @Test
    public void testGetHeadElement() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        assertEquals("head", head.tagName());
        assertEquals(doc, head.parent());
    }

    @Test
    public void testGetBodyElement() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(doc, body.parent());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title(null);
        assertNull(doc.title());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element elem = doc.createElement("a");
        assertEquals("a", elem.tagName());
        assertEquals(doc.baseUri(), elem.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        Element body = html.appendElement("body");

        Element text1 = body.appendText("Text 1");
        Element text2 = body.appendText("Text 2");

        doc.normalise();

        // assert text nodes are moved to body
        assertEquals(3, doc.childElementsSize());
        List<Element> bodyChildren = body.childElements();
        assertEquals(2, bodyChildren.size());
        assertEquals(text1, bodyChildren.get(0));
        assertEquals(text2, bodyChildren.get(1));

        // assert title and head elements are moved to html
        List<Element> htmlChildren = html.childElements();
        assertEquals(3, htmlChildren.size());
        Element headElement = htmlChildren.get(1);
        assertEquals(head, headElement);
        Element titleElement = htmlChildren.get(2);
        assertEquals(title, titleElement);
    }

    @Test
    public void testNormaliseDocumentWithEmptyTitle() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title");

        doc.normalise();

        // assert title element is added to head if not present
        assertEquals(3, html.childElements().size());
        assertEquals(1, head.childElements().size());
        assertEquals(title, head.childElements().get(0));
    }

    @Test
    public void testNormaliseDocumentWithMultipleHeads() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head1 = html.appendElement("head");
        Element title1 = head1.appendElement("title").text("Title 1");
        Element head2 = html.appendElement("head");
        Element title2 = head2.appendElement("title").text("Title 2");

        doc.normalise();

        // assert only one head element remains
        assertEquals(3, html.childElements().size());
        assertEquals(1, html.getElementsByTag("head").size());
        assertEquals(title1, html.getElementsByTag("head").first().child(0));
        assertEquals(title2, html.getElementsByTag("head").first().lastChild());
    }

    @Test
    public void testNormaliseDocumentWithMultipleBodies() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element body1 = html.appendElement("body");
        Element body2 = html.appendElement("body");

        doc.normalise();

        // assert only one body element remains
        assertEquals(3, html.childElements().size());
        assertEquals(1, html.getElementsByTag("body").size());
        assertEquals(body1, html.getElementsByTag("body").first());
        assertTrue(body2.parent() == null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        Element body = html.appendElement("body");

        Element found = doc.findFirstElementByTagName("html", doc);
        assertEquals(html, found);

        found = doc.findFirstElementByTagName("head", doc);
        assertEquals(head, found);

        found = doc.findFirstElementByTagName("title", doc);
        assertEquals(title, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertEquals(body, found);

        found = doc.findFirstElementByTagName("p", doc); // not found
        assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        Element body = html.appendElement("body");
        body.appendText("Text 1");
        body.appendText("Text 2");

        assertEquals("<html><head><title>Test Title</title></head><body>Text 1Text 2</body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        body.text("Text 1Text 2");

        assertEquals("Text 1Text 2", doc.text());
    }

    @Test
    public void testClone() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element title = head.appendElement("title").text("Test Title");
        Element body = html.appendElement("body");

        Document cloned = doc.clone();

        assertEquals(doc, cloned.outputSettings());
        assertEquals(doc.outputSettings(), cloned.outputSettings());
        assertEquals(5, cloned.childElementsSize());

        Element htmlClone = cloned.getElementsByTag("html").first();
        assertEquals(html, htmlClone);

        Element headClone = htmlClone.getElementsByTag("head").first();
        assertEquals(head, headClone);

        Element titleClone = headClone.getElementsByTag("title").first();
        assertEquals(title, titleClone);

        Element bodyClone = htmlClone.getElementsByTag("body").first();
        assertEquals(body, bodyClone);

        Element textClone1 = bodyClone.getElementsByClass("textNode").first();
        Element textClone2 = bodyClone.getElementsByClass("textNode").last();
        assertEquals(text1, textClone1);
        assertEquals(text2, textClone2);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);

        assertEquals(outputSettings, doc.outputSettings());

        OutputSettings newOutputSettings = new OutputSettings();
        newOutputSettings.prettyPrint(false);
        doc.outputSettings(newOutputSettings);

        OutputSettings docOutputSettings = doc.outputSettings();
        assertTrue(docOutputSettings.prettyPrint() == false);
    }
}