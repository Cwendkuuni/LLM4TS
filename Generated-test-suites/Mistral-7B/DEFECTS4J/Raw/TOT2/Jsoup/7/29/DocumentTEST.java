import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        assertEquals("#document", document.nodeName());
        assertEquals(baseUri, document.baseUri());
        assertNull(document.head());
        assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Element html = document.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());
        assertEquals(baseUri, html.baseUri());
        Element head = html.getFirstElementChild();
        Element body = head.getNextElementSibling();
        assertNotNull(head);
        assertEquals("head", head.tagName());
        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadAccessors() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        Element head = document.head();
        Element title = head.getFirstElementChild();
        assertEquals("Test Title", title.text().trim());
        String titleText = document.title();
        assertEquals("Test Title", titleText.trim());
    }

    @Test
    public void testTitleAccessors() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.title("Test Title");
        assertEquals("Test Title", document.title().trim());
        document.title("");
        assertEquals("", document.title().trim());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("");
        Element element = document.createElement("a");
        assertEquals("a", element.tagName());
        assertEquals("", element.baseUri());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWithNullElement() {
        Document document = new Document("");
        document.normalise(null);
    }

    @Test
    public void testNormaliseTextNodes() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        TextNode titleText = document.createElementText("Test Title");
        head.appendChild(titleText);
        TextNode bodyText = document.createElementText("This is a test body");
        Element body = html.appendElement("body");
        body.appendChild(bodyText);
        document.normalise();
        List<Node> bodyNodes = body.childNodes();
        assertEquals(2, bodyNodes.size());
        assertEquals(titleText, bodyNodes.get(0));
        assertEquals(bodyText, bodyNodes.get(1));
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        Element title = document.createElement("title");
        title.text("Test Title");
        head.appendChild(title);
        Element paragraph = document.createElement("p");
        paragraph.text("This is a test paragraph");
        body.appendChild(paragraph);
        Element link = document.createElement("a", "a");
        link.text("Test Link");
        body.appendChild(link);
        Element element = document.findFirstElementByTagName("a", body);
        assertEquals(link, element);
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element html = document.appendElement("html");
        Element head = html.appendElement("head");
        Element title = document.createElement("title");
        title.text("Test Title");
        head.appendChild(title);
        Element body = html.appendElement("body");
        Element p = body.appendElement("p");
        p.text("This is a test paragraph");
        Element link = body.appendElement("a", "a");
        link.text("Test Link");
        document.normalise();
        assertEquals("<html><head><title>Test Title</title></head><body><p>This is a test paragraph</p><a href=\"\">Test Link</a></body></html>", document.outerHtml());
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element body = document.appendElement("body");
        TextNode textNode = document.createElementText("This is a test text");
        body.appendChild(textNode);
        document.text(body, "Test Text");
        assertEquals("Test Text", body.text().trim());
    }

    @Test(expected = NullPointerException.class)
    public void testTextWithNullBody() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        document.text(null, "Test Text");
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("");
        OutputSettings outputSettings = document.outputSettings();
        assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        assertEquals("UTF-8", outputSettings.charset().name());
        assertEquals(Charset.forName("UTF-8"), outputSettings.encoder().charset());
        assertTrue(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());

        OutputSettings newOutputSettings = outputSettings.escapeMode(Entities.EscapeMode.extended).charset("ISO-8859-1");
        assertEquals(Entities.EscapeMode.extended, newOutputSettings.escapeMode());
        assertEquals("ISO-8859-1", newOutputSettings.charset().name());
        assertEquals(Charset.forName("ISO-8859-1"), newOutputSettings.encoder().charset());
        assertFalse(newOutputSettings.prettyPrint());
        assertEquals(1, newOutputSettings.indentAmount());
    }
}