package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com/";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());

        Element html = doc.head();
        assertEquals("html", html.tagName());
        Element head = html.getFirstElementSibling();
        assertEquals("head", head.tagName());
        Element body = html.getLastElementChild();
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadAccessors() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);

        Element head = doc.head();
        assertEquals("head", head.tagName());

        Element title = doc.title();
        assertEquals("", title);
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
        assertEquals("Test Title", head.children().get(0).text());

        Element newTitle = doc.createElement("title");
        newTitle.text("New Title");
        head.replaceChildren(newTitle);
        assertEquals("New Title", doc.title());
    }

    @Test
    public void testBodyAccessors() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);

        Element body = doc.body();
        assertEquals("body", body.tagName());
        assertEquals(0, body.childElementCount());

        Element p = doc.createElement("p");
        p.text("Test text");
        body.appendChild(p);
        assertEquals(1, body.childElementCount());
        assertEquals("Test text", body.getFirstElementChild().text());

        Element newP = doc.createElement("p");
        newP.text("New text");
        body.replaceChildren(newP);
        assertEquals("New text", body.getFirstElementChild().text());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);

        Element p1 = doc.createElement("p").text("Test 1");
        Element p2 = doc.createElement("p").text("Test 2");
        Element div = doc.createElement("div").text("Test div");

        doc.body().appendChild(p1);
        doc.head().appendChild(div);
        doc.head().appendChild(p2);

        doc.normalise();
        Element body = doc.body();
        assertEquals(1, body.childElementCount());
        assertEquals("Test 1Test 2", body.getFirstElementChild().text());
        assertEquals(0, doc.head().childElementCount());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "http://example.com/";
        Document doc = Document.createShell(baseUri);

        OutputSettings settings = doc.outputSettings();
        assertSame(Entities.EscapeMode.base, settings.escapeMode());
        assertEquals("UTF-8", settings.charset().name());
        assertNull(settings.encoder());
        assertTrue(settings.prettyPrint());
        assertEquals(1, settings.indentAmount());

        settings.prettyPrint(false);
        assertFalse(settings.prettyPrint());

        settings.charset("ISO-8859-1");
        assertEquals("ISO-8859-1", settings.charset().name());
        settings.charset(Charset.forName("UTF-8"));
        assertEquals("UTF-8", settings.charset().name());

        settings.indentAmount(4);
        assertEquals(4, settings.indentAmount());
    }
}