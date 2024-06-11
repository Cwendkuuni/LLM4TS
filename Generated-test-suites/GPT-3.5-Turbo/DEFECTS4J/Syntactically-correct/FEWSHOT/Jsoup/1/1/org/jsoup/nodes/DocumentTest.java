package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * JUnit test cases for Document class.
 */
public class DocumentTest {
    @Test
    public void testConstructor() {
        Document document = new Document("http://www.example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());

        Element htmlElement = document.getElementsByTag("html").first();
        assertNotNull(htmlElement);

        Element headElement = document.getElementsByTag("head").first();
        assertNotNull(headElement);

        Element bodyElement = document.getElementsByTag("body").first();
        assertNotNull(bodyElement);
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://www.example.com");
        Element headElement = document.head();
        assertNotNull(headElement);
        assertEquals("head", headElement.nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://www.example.com");
        Element bodyElement = document.body();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://www.example.com");
        assertNull(document.title());

        document.title("My Title");
        assertEquals("My Title", document.title());

        document.title("");
        assertEquals("", document.title());

        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testCreateElement() {
        Document document = Document.createShell("http://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://www.example.com");
        Element body = document.body();

        Element divElement = new Element(Tag.valueOf("div"), "http://www.example.com/test");
        divElement.text("Test Text");
        body.appendChild(divElement);

        document.normalise();

        assertEquals("html", document.getElementsByTag("html").first().nodeName());
        assertEquals("head", document.head().nodeName());
        assertEquals("body", document.body().nodeName());

        assertTrue(document.select("html").first().hasText());
        assertFalse(document.select("head").first().hasText());
        assertTrue(document.select("body").first().hasText());
        assertFalse(body.hasText());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://www.example.com");
        document.appendElement("div");

        assertEquals("<div></div>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://www.example.com");
        Element body = document.body();

        document.text("Test Text");
        assertEquals("Test Text", body.text());
    }
}