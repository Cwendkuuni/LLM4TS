package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @Test
    public void testGetHeadAndBody() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");
        Element head = doc.head();
        Element body = doc.body();

        assertEquals("Test Title", doc.title());
        assertNotNull(head);
        assertNotNull(body);

        assertSame(doc, head.parent());
        assertSame(doc, body.parent());

        assertNull(head.text());
        assertNull(body.text());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title 1");
        assertEquals("Test Title 1", doc.title());

        doc.title("Test Title 2");
        assertEquals("Test Title 2", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        assertEquals("p", p.tagName());
        assertSame(doc.baseUri(), p.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");
        Element p1 = doc.createElement("p").text("Text 1");
        Element p2 = doc.createElement("p").text("Text 2");
        doc.appendChild(p1);
        doc.appendChild(p2);

        doc.normalise();

        assertSame(doc.body(), p1.parent());
        assertSame(doc.body(), p2.parent());
        assertEquals("", p1.outerHtml().trim());
        assertEquals("Text 2", p2.outerHtml().trim());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.createElement("html");
        doc.appendChild(html);
        Element head = doc.createElement("head");
        html.appendChild(head);
        Element body = doc.createElement("body");
        html.appendChild(body);

        Element title = doc.createElement("title").text("Test Title");
        head.appendChild(title);

        Element p = doc.createElement("p").text("Test Text");
        body.appendChild(p);

        Element found = doc.findFirstElementByTagName("title", this);
        assertEquals("title", found.tagName());
        assertEquals("Test Title", found.text().trim());

        found = doc.findFirstElementByTagName("p", this);
        assertEquals("p", found.tagName());
        assertEquals("Test Text", found.text().trim());

        found = doc.findFirstElementByTagName("script", this);
        assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();

        assertEquals(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals(Charset.forName("UTF-8"), settings.charset());

        settings.charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), settings.charset());
        assertEquals(Charset.forName("ISO-8859-1").newEncoder(), settings.encoder());

        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());

        settings.indentAmount(4);
        assertEquals(4, settings.indentAmount());
    }
}