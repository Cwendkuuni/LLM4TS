import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://www.example.com");
        assertThat(document.baseUri(), is(equalTo("http://www.example.com")));
        assertThat(document.nodeName(), is(equalTo("#root")));
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertThat(document.baseUri(), is(equalTo("http://www.example.com")));
        assertThat(document.head(), is(notNullValue()));
        assertThat(document.body(), is(notNullValue()));
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://www.example.com");
        Element head = document.head();
        assertThat(head.tagName(), is(equalTo("head")));
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://www.example.com");
        Element body = document.body();
        assertThat(body.tagName(), is(equalTo("body")));
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://www.example.com");
        document.title("Page Title");
        assertThat(document.title(), is(equalTo("Page Title")));
    }

    @Test
    public void testTitleNoElement() {
        Document document = Document.createShell("http://www.example.com");
        document.getElementsByTag("title").remove();
        assertThat(document.title(), is(equalTo("")));
    }

    @Test
    public void testTitleInvalidTitle() {
        Document document = Document.createShell("http://www.example.com");
        try {
            document.title(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(equalTo("title must not be null")));
        }
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://www.example.com");
        document.head().appendElement("meta");
        document.normalise();
        assertThat(document.select("html").isEmpty(), is(false));
        assertThat(document.head(), is(notNullValue()));
        assertThat(document.body(), is(notNullValue()));
    }

    @Test
    public void testNormaliseNoHtml() {
        Document document = new Document("http://www.example.com");
        document.normalise();
        assertThat(document.select("html").isEmpty(), is(false));
        assertThat(document.head(), is(notNullValue()));
        assertThat(document.body(), is(notNullValue()));
    }

    @Test
    public void testNormaliseNoHead() {
        Document document = new Document("http://www.example.com");
        document.select("html").appendElement("meta");
        document.normalise();
        assertThat(document.head(), is(notNullValue()));
    }

    @Test
    public void testNormaliseNoBody() {
        Document document = new Document("http://www.example.com");
        document.select("html").appendElement("meta");
        document.normalise();
        assertThat(document.body(), is(notNullValue()));
    }

    @Test
    public void testNormaliseTextNode() {
        Document document = Document.createShell("http://www.example.com");
        document.normalise();
        assertThat(document.body().childNodes().get(0).nodeName(), is(equalTo("#text")));
    }

    @Test
    public void testNormaliseBlankTextNode() {
        Document document = Document.createShell("http://www.example.com");
        document.normalise();
        assertThat(document.body().childNodes().size(), is(equalTo(2)));
    }

    @Test
    public void testNormaliseTextNodeContent() {
        Document document = Document.createShell("http://www.example.com");
        document.appendElement("div").text("Hello");
        document.normalise();
        assertThat(document.body().childNodes().get(1).toString(), is(equalTo("Hello")));
    }

    @Test
    public void testNormaliseTextNodeContentWithWhitespace() {
        Document document = Document.createShell("http://www.example.com");
        document.appendElement("div").text("  Hello  ");
        document.normalise();
        assertThat(document.body().childNodes().get(1).toString(), is(equalTo("Hello")));
    }

    @Test
    public void testNormaliseTextNodeContentWithBlank() {
        Document document = Document.createShell("http://www.example.com");
        document.appendElement("div").text("");
        document.normalise();
        assertThat(document.body().childNodes().size(), is(equalTo(1)));
    }

    @Test
    public void testNormaliseTextNodeContentWithBlankSpace() {
        Document document = Document.createShell("http://www.example.com");
        document.appendElement("div").text(" ");
        document.normalise();
        assertThat(document.body().childNodes().size(), is(equalTo(1)));
    }

    @Test
    public void testNormaliseTextNodeContentWithBlankSpaceAndText() {
        Document document = Document.createShell("http://www.example.com");
        document.appendElement("div").text(" Hello ");
        document.normalise();
        assertThat(document.body().childNodes().get(1).toString(), is(equalTo("Hello")));
    }

    @Test
    public void testOuterHtml() {
        Document document = Document.createShell("http://www.example.com");
        assertThat(document.outerHtml(), is(equalTo(document.html())));
    }

    @Test
    public void testText() {
        Document document = Document.createShell("http://www.example.com");
        document.text("Hello");
        assertThat(document.body().text(), is(equalTo("Hello")));
    }

    @Test
    public void testTextNoEncoding() {
        Document document = Document.createShell("http://www.example.com");
        document.text("<div>Hello</div>");
        assertThat(document.body().text(), is(equalTo("<div>Hello</div>")));
    }

    @Test
    public void testTextEmptyString() {
        Document document = Document.createShell("http://www.example.com");
        document.text("");
        assertThat(document.body().text(), is(equalTo("")));
    }

    @Test
    public void testNodeName() {
        Document document = Document.createShell("http://www.example.com");
        assertThat(document.nodeName(), is(equalTo("#document")));
    }
}