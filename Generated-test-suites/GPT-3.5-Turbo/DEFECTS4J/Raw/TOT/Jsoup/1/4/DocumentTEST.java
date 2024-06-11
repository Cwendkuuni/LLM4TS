
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }

    @Test
    public void testDocumentConstructor() {
        assertEquals("The base URI should be set correctly", "http://example.com", document.baseUri());
        assertEquals("The document should have a root element with the tag '#root'", "#root", document.nodeName());
        assertNotNull("The document should have a head element", document.head());
        assertNotNull("The document should have a body element", document.body());
    }

    @Test
    public void testCreateShell() {
        Document shellDocument = Document.createShell("http://example.com");

        assertEquals("The base URI should be set correctly", "http://example.com", shellDocument.baseUri());
        assertEquals("The document should have a root element with the tag '#root'", "#root", shellDocument.nodeName());
        Element htmlElement = shellDocument.getElementsByTag("html").first();
        assertNotNull("The document should have an html element", htmlElement);
        assertNotNull("The html element should have a head element", htmlElement.getElementsByTag("head").first());
        assertNotNull("The html element should have a body element", htmlElement.getElementsByTag("body").first());
    }

    @Test
    public void testHead() {
        Element headElement = document.head();
        assertNull("The document should not have a head element initially", headElement);

        Element newHeadElement = document.appendElement("head");
        headElement = document.head();
        assertEquals("The document should have a head element", newHeadElement, headElement);
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertNull("The document should not have a body element initially", bodyElement);

        Element newBodyElement = document.appendElement("body");
        bodyElement = document.body();
        assertEquals("The document should have a body element", newBodyElement, bodyElement);
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("The document should have an empty title initially", "", title);

        document.head().appendElement("title").text("Example Title");
        title = document.title();
        assertEquals("The document should return the correct title", "Example Title", title);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleNull() {
        document.title(null);
        fail("Setting the title to null should throw an IllegalArgumentException");
    }

    @Test
    public void testTitleExistingElement() {
        document.head().appendElement("title").text("Example Title");

        document.title("New Title");
        String title = document.title();
        assertEquals("The document should update the existing title element", "New Title", title);
    }

    @Test
    public void testTitleNewElement() {
        document.title("New Title");

        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull("The document should create a new title element if none exist", titleElement);
        assertEquals("The document should set the text of the new title element", "New Title", titleElement.text());
    }

    @Test
    public void testNormalise() {
        document.createShell("http://example.com");
        Element htmlElement = document.select("html").first();
        Element headElement = document.head();
        Element bodyElement = document.body();

        assertNull("The document should not have an html element initially", htmlElement);
        assertNull("The document should not have a head element initially", headElement);
        assertNull("The document should not have a body element initially", bodyElement);

        document.normalise();

        htmlElement = document.select("html").first();
        headElement = document.head();
        bodyElement = document.body();

        assertNotNull("The document should have an html element after normalization", htmlElement);
        assertNotNull("The document should have a head element after normalization", headElement);
        assertNotNull("The document should have a body element after normalization", bodyElement);
    }

    @Test
    public void testNormaliseWithExistingElements() {
        document.head().appendElement("title");
        document.appendElement("html");
        document.appendElement("body");

        document.normalise();

        Element htmlElement = document.select("html").first();
        assertNotNull("The document should retain the existing html element", htmlElement);

        Element headElement = document.head();
        assertNotNull("The document should retain the existing head element", headElement);

        Element bodyElement = document.body();
        assertNotNull("The document should retain the existing body element", bodyElement);
    }

    @Test
    public void testNormaliseTextNodes() {
        document.text("Example Text");
        Element bodyElement = document.body();
        assertEquals("The body element should contain the text node", "Example Text", bodyElement.text());

        document.normalise();
        bodyElement = document.body();
        assertEquals("The body element should contain the text node after normalization", "Example Text", bodyElement.text());
    }

    @Test
    public void testNormaliseTextNodesMultiple() {
        document.text("Text 1");
        document.head().appendElement("title").text("Title");
        document.text("Text 2");

        document.normalise();

        Element bodyElement = document.body();
        assertEquals("The body element should contain the text nodes in the correct order after normalization", "Text 1 Text 2", bodyElement.text());
    }

    @Test
    public void testNormaliseTextNodesBlank() {
        document.text("Text 1");
        document.head().appendElement("title").text("");
        document.text("Text 2");

        document.normalise();

        Element bodyElement = document.body();
        assertEquals("The body element should contain the text nodes in the correct order after normalization, ignoring the blank text node", "Text 1 Text 2", bodyElement.text());
    }

    @Test
    public void testOuterHtml() {
        String html = document.outerHtml();
        assertEquals("The outer HTML should be equal to the inner HTML", document.html(), html);
    }

    @Test
    public void testText() {
        document.text("Example Text");
        Element bodyElement = document.body();
        assertEquals("The body element should contain the text", "Example Text", bodyElement.text());
    }

    @Test
    public void testTextOverrideStructure() {
        Element existingElement = document.appendElement("div");
        existingElement.appendElement("p");
        document.text("Example Text");

        Element bodyElement = document.body();
        assertEquals("The body element should contain the text", "Example Text", bodyElement.text());
        assertSame("The document structure should not be removed", existingElement, bodyElement.parent());
    }
}
