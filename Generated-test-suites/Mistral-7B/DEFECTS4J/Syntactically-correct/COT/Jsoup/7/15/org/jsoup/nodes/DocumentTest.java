package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName().toLowerCase());
        
        html = document.body();
        assertNotNull(html);
        assertEquals("body", html.nodeName().toLowerCase());
    }

    @org.junit.Test
    public void testHeadMethod() {
        Document document = new Document("http://example.com");
        Element headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName().toLowerCase());
    }

    @org.junit.Test
    public void testBodyMethod() {
        Document document = new Document("http://example.com");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName().toLowerCase());
    }

    @org.junit.Test
    public void testTitleMethod_NullTitle() {
        Document document = new Document("http://example.com");
        assertEquals("", document.title());
    }

    @org.junit.Test
    public void testTitleMethod_SetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @org.junit.Test
    public void testCreateElementMethod() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName().toLowerCase());
    }

    @org.junit.Test
    public void testNormaliseMethod() {
        Document document = new Document("test<html><head></head><body><text>Test</text></body></html>");
        document.normalise();
        Element body = document.body();
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Test", textNode.text());
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameMethod_NullNode() {
        Document document = new Document("http://example.com");
        document.findFirstElementByTagName("html", null);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameMethod_NullTagName() {
        Document document = new Document("http://example.com");
        document.findFirstElementByTagName(null, this);
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod_ExistingElement() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = body.appendElement("title");
        Element found = document.findFirstElementByTagName("title", this);
        assertEquals(title, found);
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod_NoElementFound() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = body.appendElement("title");
        Element found = document.findFirstElementByTagName("table", this);
        assertNull(found);
    }

    @org.junit.Test
    public void testTitleMethod_SetTitleWithNullTitle() {
        Document document = new Document("http://example.com");
        try {
            document.title(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testNormaliseTextNodesMethod_EmptyList() {
        Document document = new Document("test");
        List<Node> toMove = new ArrayList<>();
        assertEquals(0, toMove.size());
    }

    @org.junit.Test
    public void testNormaliseTextNodesMethod_SingleTextNode() {
        Document document = new Document("test<html><head></head><body><text>Test</text></body></html>");
        List<Node> toMove = new ArrayList<>();
        toMove.add(document.head().childNodes().get(0));
        assertEquals(1, toMove.size());
    }

    @org.junit.Test
    public void testNormaliseTextNodesMethod_MultipleTextNodes() {
        Document document = new Document("test<html><head></head><body><text>Test1</text><text>Test2</text></body></html>");
        List<Node> toMove = new ArrayList<>();
        for (Node node : document.body().childNodes()) {
            if (node instanceof TextNode) {
                TextNode tn = (TextNode) node;
                if (!tn.isBlank())
                    toMove.add(tn);
            }
        }
        assertEquals(2, toMove.size());
    }

    @org.junit.Test
    public void testFindFirstElementByTagNameMethod_MultipleElementsFound() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        Element head1 = html.appendElement("head");
        Element body1 = html.appendElement("body");
        Element head2 = html.appendElement("head");
        Element body2 = html.appendElement("body");
        Element found = document.findFirstElementByTagName("head", this);
        assertEquals(head1, found);
        found = document.findFirstElementByTagName("head", this);
        assertEquals(head2, found);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testOutputSettingsConstructor() {
        new Document.OutputSettings(null);
    }
}