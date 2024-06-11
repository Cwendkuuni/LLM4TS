package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.HtmlElement;
import org.jsoup.nodes.HeadElement;
import org.jsoup.nodes.BodyElement;
import org.jsoup.nodes.TitleElement;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertEquals("#document", doc.nodeName());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNotNull(doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element htmlEl = doc.getElementsByTag("html").first();
        Element headEl = htmlEl.getElementsByTag("head").first();
        Element bodyEl = htmlEl.getElementsByTag("body").first();

        assertEquals("#html", htmlEl.nodeName());
        assertEquals("#head", headEl.nodeName());
        assertEquals("#body", bodyEl.nodeName());
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertNotNull(doc.title());
        assertEquals("", doc.title());
    }

    @Test
    public void testHeadElement() {
        Document doc = Document.createShell("baseUri");
        Element headEl = doc.head();
        HeadElement headElExpected = new HeadElement(Tag.HEAD);
        headElExpected.baseUri = doc.baseUri();

        assertEquals(headElExpected.nodeName(), headEl.nodeName());
        assertEquals(headElExpected.baseUri(), headEl.baseUri());
    }

    @Test
    public void testBodyElement() {
        Document doc = Document.createShell("baseUri");
        Element bodyEl = doc.body();
        BodyElement bodyElExpected = new BodyElement(Tag.BODY);
        bodyElExpected.baseUri = doc.baseUri();

        assertEquals(bodyElExpected.nodeName(), bodyEl.nodeName());
        assertEquals(bodyElExpected.baseUri(), bodyEl.baseUri());
    }

    @Test
    public void testTitleElement() {
        Document doc = Document.createShell("baseUri");
        TitleElement titleEl = (TitleElement) doc.getElementsByTag("title").first();
        TitleElement titleElExpected = new TitleElement(Tag.TITLE);
        titleElExpected.baseUri = doc.baseUri();
        titleElExpected.text = "";

        assertEquals(titleElExpected.nodeName(), titleEl.nodeName());
        assertEquals(titleElExpected.baseUri(), titleEl.baseUri());
        assertEquals(titleElExpected.text(), titleEl.text());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("baseUri");
        Element newElement = doc.createElement("a");

        assertEquals("a", newElement.nodeName());
        assertEquals(doc.baseUri(), newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("baseUri");
        Element titleEl = doc.getElementsByTag("title").first();
        titleEl.appendText(" title");
        Element divEl = doc.createElement("div");
        divEl.appendText("div text");
        doc.body().appendChild(divEl);
        doc.head().appendChild(titleEl);

        doc.normalise();

        assertEquals(0, doc.getElementsByTag("title").size());
        assertEquals("div text", doc.getElementsByTag("div").first().text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("baseUri");
        Element titleEl = doc.getElementsByTag("title").first();
        titleEl.appendText(" title");
        Element divEl = doc.createElement("div");
        divEl.appendText("div text");
        doc.body().appendChild(divEl);
        doc.head().appendChild(titleEl);
        doc.title("new title");

        String expected = "<html><head><title>new title</title></head><body>" +
                "<div>div text</div></body></html>";

        String actual = doc.outerHtml();

        assertEquals(expected, actual);
    }

    @Test
    public void testTextNode() {
        Document doc = Document.createShell("baseUri");
        Element textEl = doc.createTextNode("text");
        doc.body().appendChild(textEl);

        assertEquals("text", textEl.text());
    }

    @Test
    public void testNullBaseUri() {
        Document doc = new Document(null);
        Validate.assertNull(doc.head());
        Validate.assertNull(doc.body());
        assertNotNull(doc.baseUri());
    }
}