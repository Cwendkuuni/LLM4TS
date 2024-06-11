package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test class for Document.java
 *
 * @author [Your Name]
 */
public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("baseUri");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.nodeName());

        html = document.body();
        assertNotNull(html);
        assertEquals("body", html.nodeName());
    }

    @Test
    public void testHeadAccess() {
        Document document = Document.createShell("baseUri");
        Element head = document.head();
        assertNotNull(head);

        Element title = document.title();
        assertEquals("", title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testBodyAccess() {
        Document document = Document.createShell("baseUri");
        Element body = document.body();
        assertNotNull(body);

        document.text("Test Body Text");
        assertEquals("Test Body Text", body.text());
    }

    @Test
    public void testTitleSetting() {
        Document document = Document.createShell("baseUri");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("baseUri");
        Element element = document.createElement("p");
        assertNotNull(element);
        assertEquals("p", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("baseUri");
        document.body().appendText("Test Text");
        document.head().appendElement(new Element("meta"));
        document.appendElement(new Element("div"));
        assertEquals(3, document.childNodes().size());

        document.normalise();
        assertEquals(2, document.childNodes().size());
        Element body = document.body();
        TextNode textNode = (TextNode) body.childNodes().get(0);
        assertEquals("Test Text", textNode.text());
        Element div = document.getElementsByTag("div").first();
        assertNotNull(div);
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = new Document("baseUri");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");
        Element p = document.createElement("p");
        TextNode textNode1 = document.createTextNode("Text 1");
        TextNode textNode2 = document.createTextNode("Text 2");
        TextNode textNode3 = document.createTextNode("Text 3");

        head.appendChild(textNode1);
        body.appendChild(textNode2);
        body.appendChild(p);
        p.appendChild(textNode3);

        document.normaliseTextNodes(head);
        document.normaliseTextNodes(body);
        document.normaliseTextNodes(document);

        List<Node> headChildNodes = new ArrayList<>(head.childNodes());
        assertEquals(0, headChildNodes.size());

        List<Node> bodyChildNodes = new ArrayList<>(body.childNodes());
        assertEquals(2, bodyChildNodes.size());
        TextNode bodyTextNode = (TextNode) bodyChildNodes.get(0);
        assertEquals("Text 1Text 2", bodyTextNode.text());
        assertEquals("Text 3", p.childNodes().get(0).text());
    }

    @Test
    public void testNormaliseStructure() {
        Document document = new Document("baseUri");
        Element head1 = document.appendElement("head");
        Element head2 = document.appendElement("head");
        Element body = document.appendElement("body");

        document.normaliseStructure("head", document);
        assertEquals(1, document.getElementsByTag("head").size());

        document.normaliseStructure("body", document);
        assertEquals(1, document.getElementsByTag("body").size());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        Element found = document.findFirstElementByTagName("html", document);
        assertEquals(html, found);

        found = document.findFirstElementByTagName("head", document);
        assertEquals(head, found);

        found = document.findFirstElementByTagName("body", document);
        assertEquals(body, found);

        found = document.findFirstElementByTagName("unknownTag", document);
        assertNull(found);
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("baseUri");
        Element html = document.appendElement("html");
        Element head = document.appendElement("head");
        Element body = document.appendElement("body");

        document.title("Test Title");
        head.appendElement(new Element("meta"));
        body.appendText("Test Body Text");

        assertEquals("<html><head><title>Test Title</title></head><body>Test Body Text</body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("baseUri");
        Element body = document.body();

        document.text("Test Text");
        assertEquals("Test Text", body.text());

        body.appendText(" Added Text ");
        assertEquals("Test Text Added Text ", document.text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("baseUri");
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testClone() {
        Document document = new Document("baseUri");
        Document clonedDocument = document.clone();

        Element html = document.appendElement("html");
        html.appendElement(new Element("head"));
        html.appendElement(new Element("body"));

        assertEquals(document.outputSettings(), clonedDocument.outputSettings());
    }
}