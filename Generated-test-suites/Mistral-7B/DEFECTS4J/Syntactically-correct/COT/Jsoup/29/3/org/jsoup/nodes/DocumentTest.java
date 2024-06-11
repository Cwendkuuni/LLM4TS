package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.QuirksMode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    @org.junit.Before
    public void setUp() {
        // Initialize any mocks or setup objects here if needed
    }

    @org.junit.After
    public void tearDown() {
        // Release any resources or teardown objects here if needed
    }

    // Test for constructor
    @org.junit.Test
    public void testDocumentConstructor() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertNull(doc.head());
        assertNull(doc.body());
        assertNull(doc.title());
        // Test for createShell method
        Document shellDoc = Document.createShell(baseUri);
        assertTrue(shellDoc instanceof Document);
        Element html = shellDoc.head();
        assertEquals("html", html.tagName());
        assertEquals(baseUri, html.baseUri());
        Element head = html.head();
        assertEquals("head", head.tagName());
        Element body = html.body();
        assertEquals("body", body.tagName());
        assertNull(head.title());
        assertNull(body.text());
    }

    // Test for head() and title() methods
    @org.junit.Test
    public void testHeadAndTitleMethods() {
        Document doc = Document.createShell("http://example.com");
        Element head = doc.head();
        Element title = head.title();
        assertNull(title);
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title.text().trim());
        assertEquals("Test Title", doc.title());
        doc.head().appendElement("title").text("Another Title");
        title = doc.head().getElementsByTag("title").first();
        assertEquals("Another Title", title.text().trim());
        assertEquals("Another Title", doc.title());
    }

    // Test for body() method
    @org.junit.Test
    public void testBodyMethod() {
        Document doc = Document.createShell("http://example.com");
        assertNull(doc.body());
        Element body = doc.createElement("body");
        doc.body().appendElement("p").text("Test");
        assertEquals("pTest", doc.body().html());
    }

    // Test for title(String) method with null argument
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTitleWithNullArgument() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
    }

    // Test for createElement(String) method with null argument
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCreateElementWithNullArgument() {
        Document doc = Document.createShell("http://example.com");
        doc.createElement(null);
    }

    // Test for title() method with empty string argument
    @org.junit.Test
    public void testTitleWithEmptyStringArgument() {
        Document doc = Document.createShell("http://example.com");
        doc.title("");
        assertEquals("", doc.title());
    }

    // Test for normalise() method
    @org.junit.Test
    public void testNormaliseMethod() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p").text("Test");
        Element div = doc.createElement("div").text("Div");
        Element body = doc.createElement("body");
        body.appendChild(p);
        body.appendChild(div);
        doc.appendChild(body);

        doc.normalise();

        Element html = doc.findFirstElementByTagName("html", doc);
        assertEquals(1, html.childNodes().size());
        assertEquals("html", html.tagName());

        Element head = html.getElementsByTag("head").first();
        assertEquals(0, head.childNodes().size());

        Element body2 = html.getElementsByTag("body").first();
        assertEquals(1, body2.childNodes().size());
        Element p2 = body2.getElementsByTag("p").first();
        assertEquals("pTest", p2.html());
    }

    // Test for findFirstElementByTagName(String, Node) method
    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", doc);
        assertNull(title);

        title = doc.head().appendElement("title").text("Test Title");
        assertEquals("titleTest Title", doc.findFirstElementByTagName("title", doc).text().trim());
        assertEquals("Test Title", doc.title());

        doc.title("Another Title");
        assertEquals("Another Title", doc.findFirstElementByTagName("title", doc).text().trim());
    }

    // Test for outerHtml() method
    @org.junit.Test
    public void testOuterHtmlMethod() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element p = doc.createElement("p").text("Test");
        body.appendChild(p);

        doc.normalise();

        assertEquals("<html><head></head><body><p>Test</p></body></html>", doc.outerHtml());
    }

    // Test for text(String) method with null argument
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTextWithNullArgument() {
        Document doc = Document.createShell("http://example.com");
        doc.body().text(null);
    }

    // Test for text(String) method with empty string argument
    @org.junit.Test
    public void testTextWithEmptyStringArgument() {
        Document doc = Document.createShell("http://example.com");
        doc.body().text("");
        assertEquals("", doc.body().html());
    }

    // Test for clone() method
    @org.junit.Test
    public void testCloneMethod() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element p = doc.createElement("p").text("Test");
        body.appendChild(p);

        doc.normalise();

        Document clone = doc.clone();

        Element htmlClone = clone.findFirstElementByTagName("html", clone);
        assertEquals(html, htmlClone);

        Element titleClone = clone.head().getElementsByTag("title").first();
        assertEquals(0, titleClone.childNodes().size());

        Element bodyClone = clone.getElementsByTag("body").first();
        Element pClone = bodyClone.getElementsByTag("p").first();
        assertEquals("pTest", pClone.html());
    }
}