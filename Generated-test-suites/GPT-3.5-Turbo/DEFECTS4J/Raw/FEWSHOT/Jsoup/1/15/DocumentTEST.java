import org.apache.commons.lang.Validate;
import org.assertj.core.api.Assertions;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("http://example.com");
    }

    @Test
    public void testConstructor() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);

        assertThat(document.baseUri()).isEqualTo(baseUri);
        assertThat(document.nodeName()).isEqualTo("#document");
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");

        assertThat(document.baseUri()).isEqualTo("http://example.com");
        assertThat(document.nodeName()).isEqualTo("#root");

        List<Element> htmlElements = document.getElementsByTag("html");
        assertThat(htmlElements).hasSize(1);

        Element htmlElement = htmlElements.get(0);
        assertThat(htmlElement).hasTagName("html");

        List<Element> headElements = htmlElement.getElementsByTag("head");
        assertThat(headElements).hasSize(1);
        assertThat(headElements.get(0)).hasTagName("head");

        List<Element> bodyElements = htmlElement.getElementsByTag("body");
        assertThat(bodyElements).hasSize(1);
        assertThat(bodyElements.get(0)).hasTagName("body");
    }

    @Test
    public void testHead() {
        Element headElement = document.head();
        assertThat(headElement).isNull();

        Element htmlElement = document.appendElement("html");
        htmlElement.appendElement("head");

        headElement = document.head();
        assertThat(headElement).isNotNull();
        assertThat(headElement).hasTagName("head");
    }

    @Test
    public void testBody() {
        Element bodyElement = document.body();
        assertThat(bodyElement).isNull();

        Element htmlElement = document.appendElement("html");
        htmlElement.appendElement("body");

        bodyElement = document.body();
        assertThat(bodyElement).isNotNull();
        assertThat(bodyElement).hasTagName("body");
    }

    @Test
    public void testTitle() {
        String title = document.title();
        assertThat(title).isEmpty();

        document.appendElement("head").appendElement("title").text("Test Title");
        title = document.title();
        assertThat(title).isEqualTo("Test Title");
    }

    @Test
    public void testSetTitle() {
        document.title("New Title");

        Element titleElement = document.getElementsByTag("title").first();
        assertThat(titleElement).isNotNull();
        assertThat(titleElement.text()).isEqualTo("New Title");

        document.title(null);
        assertThat(document.getElementsByTag("title")).hasSize(0);
    }

    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");

        assertThat(element).isNotNull();
        assertThat(element).hasTagName("div");
        assertThat(element.baseUri()).isEqualTo(document.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = Document.createShell("http://example.com");
        document.appendElement("title").text("Test Title");
        document.appendElement("p").text("Test Paragraph");

        Document normalisedDocument = document.normalise();

        assertThat(normalisedDocument.getElementsByTag("html")).hasSize(1);
        assertThat(normalisedDocument.getElementsByTag("head")).hasSize(1);
        assertThat(normalisedDocument.getElementsByTag("body")).hasSize(1);

        List<Node> bodyChildren = normalisedDocument.body().childNodes;
        assertThat(bodyChildren).hasSize(2);
        assertThat(bodyChildren.get(0)).isInstanceOf(TextNode.class);
        assertThat(((TextNode) bodyChildren.get(0)).text()).isEqualTo(" ");
        assertThat(bodyChildren.get(1)).isInstanceOf(Element.class);
        assertThat(((Element) bodyChildren.get(1)).tagName()).isEqualTo("p");
        assertThat(((Element) bodyChildren.get(1)).text()).isEqualTo("Test Paragraph");
    }

    @Test
    public void testOuterHtml() {
        document.appendElement("p").text("Test Paragraph");

        assertThat(document.outerHtml()).isEqualTo("<p>Test Paragraph</p>");
    }

    @Test
    public void testText() {
        Element bodyElement = document.appendElement("body");
        bodyElement.appendElement("p").text("Test Paragraph");

        assertThat(document.text()).isEqualTo("Test Paragraph");

        document.text(null);
        assertThat(bodyElement.childNodes).hasSize(0);
    }
}