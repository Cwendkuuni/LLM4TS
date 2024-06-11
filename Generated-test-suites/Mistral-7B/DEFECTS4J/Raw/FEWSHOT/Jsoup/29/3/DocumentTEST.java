package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertSame(Tag.valueOf("#document"), doc.tag());
        assertEquals("baseUri", doc.baseUri());

        Document shellDoc = Document.createShell("baseUri");
        assertNotNull(shellDoc);
        assertSame(Tag.valueOf("#document"), shellDoc.tag());
        assertEquals("baseUri", shellDoc.baseUri());
        assertTrue(shellDoc.head() != null);
        assertTrue(shellDoc.body() != null);
    }

    @Test
    public void testDocumentTitle() {
        Document doc = new Document("baseUri");
        assertTrue(doc.title().isEmpty());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());

        doc.title(null);
        assertTrue(doc.title().isEmpty());

        doc.title("");
        assertTrue(doc.title().isEmpty());
    }

    @Test
    public void testDocumentTitleSetAndGet() {
        Document doc = new Document("baseUri");
        Element titleEl = doc.getElementsByTag("title").first();
        assertNull(titleEl);

        doc.title("Test Title");
        titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("Test Title", titleEl.text().trim());

        doc.title(null);
        titleEl = doc.getElementsByTag("title").first();
        assertNull(titleEl);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element p = doc.createElement("p");
        assertNotNull(p);
        assertEquals("p", p.tagName());
        assertEquals("baseUri", p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        Element p = doc.createElement("p");
        p.text("Test Text");
        doc.body().appendChild(p);

        Element head = doc.createElement("head");
        Element title = doc.createElement("title");
        title.text("Test Title");
        head.appendChild(title);
        doc.head().appendChild(head);

        Element textNode1 = doc.createElement("textNode1");
        doc.appendChild(textNode1);

        doc.normalise();

        List<Element> bodyChildren = new ArrayList<>();
        List<Element> headChildren = new ArrayList<>();
        List<TextNode> textNodes = new ArrayList<>();

        for (Node node : doc.childNodes) {
            if (node instanceof Element) {
                if (node.tagName().equals("body")) {
                    bodyChildren.add((Element) node);
                } else if (node.tagName().equals("head")) {
                    headChildren.add((Element) node);
                }
            } else if (node instanceof TextNode) {
                textNodes.add((TextNode) node);
            }
        }

        assertEquals(3, doc.childNodes().size());
        assertEquals(1, bodyChildren.size());
        assertEquals(1, headChildren.size());
        assertEquals(1, textNodes.size());

        assertEquals(p, bodyChildren.get(0));
        assertEquals(head, headChildren.get(0));
        assertEquals(new TextNode(" ", ""), textNodes.get(0));

        assertSame(p, doc.getElementsByTag("p").first());
        assertSame(title, doc.getElementsByTag("title").first());
        assertSame(textNodes.get(0), doc.getElementsByTag("textNode1").first());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element title = doc.createElement("title");
        title.text("Test Title");
        head.appendChild(title);

        Element p = doc.createElement("p");
        p.text("Test Text");
        body.appendChild(p);

        Element h1 = doc.createElement("h1");
        Element h2 = doc.createElement("h2");

        head.appendChild(h1);
        head.appendChild(h2);

        Element found = doc.findFirstElementByTagName("p", null);
        assertSame(p, found);

        found = doc.findFirstElementByTagName("p", html);
        assertNull(found);

        found = doc.findFirstElementByTagName("p", head);
        assertNull(found);

        found = doc.findFirstElementByTagName("p", body);
        assertSame(p, found);

        found = doc.findFirstElementByTagName("title", null);
        assertSame(title, found);

        found = doc.findFirstElementByTagName("title", html);
        assertNull(found);

        found = doc.findFirstElementByTagName("title", head);
        assertSame(title, found);

        found = doc.findFirstElementByTagName("title", body);
        assertNull(found);

        found = doc.findFirstElementByTagName("h1", null);
        assertSame(h1, found);

        found = doc.findFirstElementByTagName("h1", html);
        assertSame(h1, found);

        found = doc.findFirstElementByTagName("h1", head);
        assertSame(h1, found);

        found = doc.findFirstElementByTagName("h1", body);
        assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element title = doc.createElement("title");
        title.text("Test Title");
        head.appendChild(title);

        Element p = doc.createElement("p");
        p.text("Test Text");
        body.appendChild(p);

        Element h1 = doc.createElement("h1");
        h1.text("Test H1");
        head.appendChild(h1);

        assertEquals("<html><head><title>Test Title</title><h1>Test H1</h1></head><body><p>Test Text</p></body></html>", doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("baseUri");
        Element body = doc.createElement("body");
        doc.body(body);
        body.text("Test Text");

        assertEquals("Test Text", doc.text());
    }

    @Test
    public void testClone() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element title = doc.createElement("title");
        title.text("Test Title");
        head.appendChild(title);

        Element p = doc.createElement("p");
        p.text("Test Text");
        body.appendChild(p);

        Element h1 = doc.createElement("h1");
        h1.text("Test H1");
        head.appendChild(h1);

        Document clone = doc.clone();

        assertEquals(5, clone.childNodes().size());
        assertEquals(1, clone.getElementsByTag("html").size());
        assertEquals(1, clone.getElementsByTag("head").size());
        assertEquals(1, clone.getElementsByTag("body").size());
        assertEquals(1, clone.getElementsByTag("title").size());
        assertEquals(1, clone.getElementsByTag("p").size());
        assertEquals(1, clone.getElementsByTag("h1").size());

        assertEquals("Test Title", clone.title());
        assertEquals("Test Text", clone.getElementsByTag("p").first().text());
    }

    @Test
    public void testOutputSettings() {
        Document doc = Document.createShell("baseUri");
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);

        assertEquals(Entities.EscapeMode.base, doc.outputSettings().escapeMode());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        assertEquals(false, doc.outputSettings().prettyPrint());
        assertEquals(1, doc.outputSettings().indentAmount());

        OutputSettings updatedSettings = new OutputSettings();
        updatedSettings.prettyPrint(false);
        updatedSettings.indentAmount(2);
        doc.outputSettings(updatedSettings);

        assertEquals(Entities.EscapeMode.base, doc.outputSettings().escapeMode());
        assertEquals("UTF-8", doc.outputSettings().charset().name());
        assertEquals(false, doc.outputSettings().prettyPrint());
        assertEquals(2, doc.outputSettings().indentAmount());
    }
}