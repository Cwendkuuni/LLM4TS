
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DocumentTest {
    private Document document;

    @Before
    public void setup() {
        document = Document.createShell("https://www.example.com");
    }

    @Test
    public void testConstructor() {
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());

        document = new Document("https://www.example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals("html", doc.child(0).tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testHead() {
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);

        document.createElement("title").text("New Title");
        title = document.title();
        assertEquals("New Title", title);
    }

    @Test
    public void testHtmlElementCreation() {
        Element div = document.createElement("div");
        assertNotNull(div);
        assertEquals("div", div.tagName());
        assertEquals("https://www.example.com", div.baseUri());

        document.body().appendChild(div);
        Elements divs = document.getElementsByTag("div");
        assertNotNull(divs);
        assertEquals(1, divs.size());
        assertEquals("div", divs.get(0).tagName());
    }

    @Test
    public void testNormalise() {
        // Test without body and head elements
        Document doc = Document.createShell("https://www.example.com");
        doc.normalise();
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("head", doc.head().tagName());
        assertEquals("body", doc.body().tagName());

        // Test with body but without head element
        doc = new Document("https://www.example.com");
        doc.body().appendChild(new TextNode("Test Text"));
        doc.normalise();
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testNormaliseTextNodes() {
        Element element = new Element(Tag.valueOf("div"), "https://www.example.com");
        element.appendChild(new TextNode("Example Text"));
        document.body().appendChild(element);
        document.normalise();
        assertEquals("div", document.body().child(0).tagName());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void testOuterHtml() {
        document.body().appendChild(new Element(Tag.valueOf("p"), "https://www.example.com").text("Test Paragraph"));
        String outerHtml = document.outerHtml();
        assertEquals("<head></head><body><p>Test Paragraph</p></body>", outerHtml);
    }

    @Test
    public void testText() {
        document.body().appendChild(new Element(Tag.valueOf("p"), "https://www.example.com").text("Test Paragraph"));
        document.body().appendChild(new Element(Tag.valueOf("span"), "https://www.example.com").text("Test Span"));

        document.text("New Text");
        assertEquals("New Text", document.body().text());

        document.text("");
        assertEquals("", document.body().text());
    }

    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings settings = document.outputSettings();

        assertEquals(Entities.EscapeMode.base, settings.escapeMode());

        settings.escapeMode(Entities.EscapeMode.extended);
        assertEquals(Entities.EscapeMode.extended, settings.escapeMode());

        assertEquals(Charset.forName("UTF-8"), settings.charset());

        settings.charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), settings.charset());

        settings.charset("UTF-16");
        assertEquals(Charset.forName("UTF-16"), settings.charset());

        assertTrue(settings.prettyPrint());

        settings.prettyPrint(false);
        assertTrue(!settings.prettyPrint());

        assertEquals(1, settings.indentAmount());

        settings.indentAmount(2);
        assertEquals(2, settings.indentAmount());
    }
}
