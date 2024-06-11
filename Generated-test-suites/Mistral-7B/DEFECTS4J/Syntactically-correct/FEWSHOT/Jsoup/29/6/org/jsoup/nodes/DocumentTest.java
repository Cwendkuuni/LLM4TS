package org.jsoup.nodes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
        assertNotNull(doc.head());
        assertNotNull(doc.head().tagName());
        assertNotNull(doc.body());
        assertNotNull(doc.body().tagName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.appendChild(doc.createElement("html"));
        Element head = doc.createElement("head").appendChildTo(html);
        Element body = doc.createElement("body").appendChildTo(html);

        Element title = doc.createElement("title").text("Test Title").appendChildTo(head);

        Element found = doc.findFirstElementByTagName("html", doc);
        assertSame(html, found);

        found = doc.findFirstElementByTagName("head", doc);
        assertSame(head, found);

        found = doc.findFirstElementByTagName("body", doc);
        assertSame(body, found);

        found = doc.findFirstElementByTagName("title", doc);
        assertSame(title, found);

        found = doc.findFirstElementByTagName("unknown", doc);
        assertNull(found);
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = Document.createShell("baseUri");

        Element head = doc.createElement("head");
        TextNode textNode1 = doc.createTextNode("Text 1").appendChildTo(head);
        TextNode textNode2 = doc.createTextNode("Text 2").appendChildTo(head);
        Element body = doc.createElement("body");
        TextNode textNode3 = doc.createTextNode("Text 3").appendChildTo(body);

        doc.normaliseTextNodes(head);
        assertNull(head.child(0));
        assertSame(textNode3, head.child(0));

        doc.normaliseTextNodes(body);
        assertNull(body.child(0));
        assertSame(textNode1, body.child(0));
        assertSame(textNode2, body.child(1));
        assertSame(textNode3, body.child(2));
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = Document.createShell("baseUri");

        Element head1 = doc.createElement("head").appendChildTo(doc);
        Element head2 = doc.createElement("head").appendChildTo(doc);

        Element body1 = doc.createElement("body").appendChildTo(head1);
        Element body2 = doc.createElement("body").appendChildTo(head2);

        TextNode textNode1 = doc.createTextNode("Text 1").appendChildTo(body1);
        TextNode textNode2 = doc.createTextNode("Text 2").appendChildTo(body1);
        TextNode textNode3 = doc.createTextNode("Text 3").appendChildTo(body2);

        doc.normaliseStructure("head", doc);
        assertSame(head1, doc.head());
        assertNull(head2);

        Element body = doc.body();
        assertSame(body1, body);

        assertNull(textNode3);
        assertEquals(2, body.numChildElements());
        assertEquals("Text 1", body.child(0).text());
        assertEquals("Text 2", body.child(1).text());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("baseUri");
        Element title = doc.createElement("title").text("Test Title").appendChildTo(doc.head());

        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", title.text());

        doc.title("New Title");
        assertEquals("New Title", doc.title());
        assertEquals("New Title", title.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("p");

        assertEquals("p", element.tagName());
        assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");

        Element head = doc.createElement("head");
        doc.appendChild(head);

        Element body = doc.createElement("body");
        TextNode textNode1 = doc.createTextNode("Text 1").appendChildTo(body);
        TextNode textNode2 = doc.createTextNode("Text 2").appendChildTo(body);

        TextNode textNode3 = doc.createTextNode("Text 3").appendChildTo(doc);

        doc.normalise();

        assertEquals(3, doc.numChildElements());
        assertEquals("#document", doc.tagName());
        assertSame(head, doc.child(0));
        assertSame(body, doc.child(1));

        assertEquals(2, body.numChildElements());
        assertEquals("Text 1", body.child(0).text());
        assertEquals("Text 2", body.child(1).text());

        assertNull(textNode3);
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element head = doc.createElement("head");
        doc.appendChild(head);

        Element title = doc.createElement("title").text("Test Title").appendChildTo(head);
        Element body = doc.createElement("body").appendChildTo(doc);
        TextNode textNode = doc.createTextNode("Test").appendChildTo(body);

        doc.title("New Title");

        assertEquals("<html><head><title>Test Title</title></head><body></body></html>", doc.outerHtml());
        assertEquals("<html><head><title>New Title</title></head><body><p>Test</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("baseUri");

        Element body = doc.createElement("body");
        TextNode textNode1 = doc.createTextNode("Text 1").appendChildTo(body);
        TextNode textNode2 = doc.createTextNode("Text 2").appendChildTo(body);

        doc.text("Test");

        assertEquals("Test", doc.text().get());
        assertEquals("Test\nText 1Text 2", body.text().get());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        doc.outputSettings(new OutputSettings());

        Document clonedDoc = doc.clone();
        assertEquals(outputSettings, doc.outputSettings());
        assertNotSame(outputSettings, clonedDoc.outputSettings());
    }
}