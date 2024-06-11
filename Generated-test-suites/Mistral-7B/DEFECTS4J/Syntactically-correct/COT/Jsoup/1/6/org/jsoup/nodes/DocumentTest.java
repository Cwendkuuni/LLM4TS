package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testDocumentConstructor() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#root", doc.tagName());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testDocumentHeadNullBaseUri() {
        Document doc = new Document(null);
    }

    @org.junit.Test
    public void testDocumentTitleGet() {
        Document doc = Document.createShell("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test
    public void testDocumentTitleSet() {
        Document doc = Document.createShell("baseUri");
        doc.title(null);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testDocumentTitleNullTitle() {
        Document doc = Document.createShell("baseUri");
        doc.title(null);
    }

    @org.junit.Test
    public void testDocumentCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element element = doc.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.tagName());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testDocumentCreateElementNullTagName() {
        Document doc = Document.createShell("baseUri");
        doc.createElement(null);
    }

    @org.junit.Test
    public void testDocumentNormalise() {
        Document doc = Document.createShell("baseUri");
        Element p = doc.createElement("p").text("Text to be moved to body");
        Element body = doc.body();
        body.appendChild(p);
        doc.normalise();
        Element bodyAfterNormalise = doc.body();
        assertFalse(body.hasChild(p));
        assertTrue(bodyAfterNormalise.hasChild(p));
    }

    @org.junit.Test
    public void testDocumentOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element p = doc.createElement("p").text("Text to be returned by outerHtml");
        doc.body().appendChild(p);
        String html = doc.outerHtml();
        assertTrue(html.contains(p.outerHtml()));
    }

    @org.junit.Test
    public void testDocumentText() {
        Document doc = Document.createShell("baseUri");
        doc.text("Test Text");
        Element body = doc.body();
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Test Text", textNode.text());
    }

    @org.junit.Test
    public void testDocumentTextNullText() {
        Document doc = Document.createShell("baseUri");
        doc.text(null);
        Element body = doc.body();
        assertEquals("", body.text());
    }

    @org.junit.Test
    public void testDocumentNodeName() {
        Document doc = Document.createShell("baseUri");
        assertEquals("#document", doc.nodeName());
    }
}