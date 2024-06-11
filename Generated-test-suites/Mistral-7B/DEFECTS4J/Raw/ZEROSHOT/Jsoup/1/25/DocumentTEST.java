package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Document.java
 */
public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShellDocument() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        Element html = document.getElementsByTag("html").first();
        Element head = html.getElementsByTag("head").first();
        Element body = html.getElementsByTag("body").first();
        assertNotNull(html);
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testGetHeadElement() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.tagName());
    }

    @Test
    public void testGetBodyElement() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testGetTitle() {
        Document document = Document.createShell("http://example.com");
        String title = document.title();
        assertEquals(StringUtils.EMPTY, title);

        document.title("Test Title");
        title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://example.com");
        Element divElement = document.createElement("div");
        assertNotNull(divElement);
        assertEquals("div", divElement.tagName());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = Document.createShell("http://example.com");
        Element divElement = document.createElement("div");
        Element pElement = document.createElement("p");
        TextNode textNode = document.createTextNode("Test Text");

        document.body().appendChild(divElement);
        divElement.appendChild(textNode);
        document.body().appendChild(pElement);
        document.body().appendChild(document.createTextNode("Other Text"));

        document.normalise();

        List<Node> divChildren = divElement.childNodes();
        List<Node> pChildren = pElement.childNodes();

        assertEquals(1, divChildren.size());
        assertEquals(1, pChildren.size());

        Node divChild = divChildren.get(0);
        Node pChild = pChildren.get(0);

        assertTrue(divChild instanceof TextNode);
        assertTrue(pChild instanceof Element);

        TextNode divTextNode = (TextNode) divChild;
        assertEquals("Test TextOther Text", divTextNode.text());
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://example.com");
        Element divElement = document.createElement("div");
        Element pElement = document.createElement("p");
        TextNode textNode = document.createTextNode("Test Text");

        document.body().appendChild(divElement);
        divElement.appendChild(textNode);
        document.body().appendChild(pElement);
        document.body().appendChild(document.createTextNode("Other Text"));

        document.normalise();

        String outerHtml = document.outerHtml();
        String expectedOuterHtml = "<html><head></head><body>" +
                "<div>Test TextOther Text</div>" +
                "<p></p>" +
                "</body></html>";

        assertEquals(expectedOuterHtml, outerHtml);
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://example.com");
        document.body().text("Test Text");

        String text = document.text();
        assertEquals("Test Text", text);
    }
}