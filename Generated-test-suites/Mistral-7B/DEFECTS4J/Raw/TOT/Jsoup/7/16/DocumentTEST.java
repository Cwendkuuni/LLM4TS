package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.Jsoup.parseBodyFragment;

public class DocumentTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = Document.createShell("http://example.com");
    }

    /**
     * Test creating a new document with a base URI.
     */
    @Test
    public void testDocumentCreation() {
        Document doc = new Document("http://example.com");
        assertThat(doc, notNullValue());
        assertThat(doc.baseUri(), is("http://example.com"));
    }

    /**
     * Test creating a shell document.
     */
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        Element head = html.head();
        Element body = doc.body();

        assertThat(html, notNullValue());
        assertThat(head, notNullValue());
        assertThat(body, notNullValue());

        assertThat(doc.title(), is(""));
        assertThat(doc.head().text(), is(""));
        assertThat(doc.body().text(), is(""));
    }

    /**
     * Test getting the document's head and body elements.
     */
    @Test
    public void testGetHeadAndBody() {
        Element head = doc.head();
        Element body = doc.body();

        assertThat(head, notNullValue());
        assertThat(body, notNullValue());
    }

    /**
     * Test getting the document's title.
     */
    @Test
    public void testTitle() {
        doc.title("Test Title");
        String title = doc.title();

        assertThat(title, is("Test Title"));
    }

    /**
     * Test setting the document's title.
     */
    @Test
    public void testSetTitle() {
        doc.title("Test Title");
        String title = doc.title();

        doc.title("New Title");
        title = doc.title();

        assertThat(title, is("New Title"));
    }

    /**
     * Test creating a new element with this document's base URI.
     */
    @Test
    public void testCreateElement() {
        Element element = doc.createElement("p");
        String elementHtml = element.outerHtml();

        assertThat(element, notNullValue());
        assertThat(elementHtml, is("<p></p>"));
    }

    /**
     * Test normalizing the document.
     */
    @Test
    public void testNormalise() {
        Element p1 = doc.createElement("p").text("Text 1");
        Element p2 = doc.createElement("p").text("Text 2");
        Element p3 = doc.createElement("p").text("Text 3");

        doc.appendChild(p1);
        doc.appendChild(p2);
        doc.appendChild(p3);

        doc.normalise();

        List<Element> children = doc.children();
        Element body = doc.body();

        assertThat(children.size(), is(3));
        assertThat(body.children().size(), is(3));
        for (Element child : children) {
            if (!child.equals(p1)) {
                assertThat(body.get(children.indexOf(child)), is(child));
            }
        }
    }

    /**
     * Test finding the first element by tag name.
     */
    @Test
    public void testFindFirstElementByTagName() {
        doc.title("Test Title");

        Element titleElement = doc.findFirstElementByTagName("title");
        String title = titleElement != null ? titleElement.text().trim() : "";

        doc.title("New Title");

        titleElement = doc.findFirstElementByTagName("title");
        title = titleElement != null ? titleElement.text().trim() : "";

        assertThat(title, is("Test Title"));
        assertThat(doc.findFirstElementByTagName("meta", doc), nullValue());
    }

    /**
     * Test setting the document's output settings.
     */
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();

        settings.prettyPrint(false);
        doc.outputSettings().prettyPrint();

        String html = doc.outerHtml();
        String expectedHtml = "<html><head></head><body></body></html>";

        assertThat(html, is(expectedHtml));

        settings.prettyPrint(true);
        doc.outputSettings().prettyPrint();

        html = doc.outerHtml();
        expectedHtml = "<html>\n<head>\n</head>\n<body>\n</body>\n</html>";

        assertThat(html, is(expectedHtml));
    }

    /**
     * Test setting the document's charset.
     */
    @Test
    public void testCharset() {
        Document doc = new Document("http://example.com");
        OutputSettings settings = doc.outputSettings();

        settings.charset("UTF-16");
        doc.outputSettings().charset("UTF-16");

        String text = doc.text("Test Text");
        Charset charset = doc.outputSettings().charset();

        assertThat(text, is("Test Text"));
        assertThat(charset.name(), is("UTF-16"));

        settings.charset("UTF-8");
        doc.outputSettings().charset("UTF-8");

        text = doc.text("Test Text");
        charset = doc.outputSettings().charset();

        assertThat(text, is("Test Text"));
        assertThat(charset.name(), is("UTF-8"));
    }

    /**
     * Test parsing an HTML document with a specific base URI.
     */
    @Test
    public void testParseDocumentWithBaseUri() {
        String html = "<html><head><title>Test Title</title></head><body><p>Test Text</p></body></html>";
        Document doc = parse(html, "http://example.com");

        String title = doc.title();
        String text = doc.text();

        assertThat(title, is("Test Title"));
        assertThat(text, is("Test Text"));
    }

    /**
     * Test parsing an HTML body fragment with a specific base URI.
     */
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        String bodyFragmentHtml = "<p>Test Text</p>";
        Document doc = parseBodyFragment(bodyFragmentHtml, "http://example.com");

        String text = doc.text();

        assertThat(text, is("Test Text"));
    }
}