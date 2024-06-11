import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = new Document("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertTrue(doc instanceof Document);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        assertNotNull(doc);
        assertEquals("#document", doc.nodeName());
        assertTrue(doc instanceof Document);

        Element html = doc.head();
        assertNotNull(html);
        assertEquals("html", html.tagName());

        Element head = html.appendElement("head");
        Element body = html.appendElement("body");

        assertNotNull(head);
        assertEquals("head", head.tagName());

        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadMethod() {
        Document doc = new Document("baseUri");
        Element head = doc.head();

        assertNotNull(head);
        assertEquals("head", head.tagName());

        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }

    @Test
    public void testBodyMethod() {
        Document doc = new Document("baseUri");
        Element body = doc.body();

        assertNotNull(body);
        assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleMethod() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");

        assertEquals("Test Title", doc.title());

        doc.title("");
        assertEquals("", doc.title());
    }

    @Test
    public void testCreateElementMethod() {
        Document doc = new Document("baseUri");
        Element newElement = doc.createElement("a");

        assertNotNull(newElement);
        assertEquals("a", newElement.tagName());
    }

    @Test
    public void testNormaliseMethod() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        body.appendText("Test Text");

        doc.normalise();

        assertEquals(3, doc.childNodeSize());
        assertNotNull(doc.getElementsByTag("html").first());
        assertNotNull(doc.getElementsByTag("head").first());
        assertNotNull(doc.getElementsByTag("body").first());

        List<Node> textNodes = new ArrayList<>();
        for (Node node : body.childNodes()) {
            if (node instanceof TextNode) {
                textNodes.add(node);
            }
        }

        assertEquals(1, textNodes.size());
        assertEquals("Test Text", textNodes.get(0).text());
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUriInDocumentConstructor() {
        new Document(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagNameInCreateElementMethod() {
        Document doc = new Document("baseUri");
        doc.createElement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagNameInCreateElementMethod() {
        Document doc = new Document("baseUri");
        doc.createElement("invalidTag");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagNameInFindFirstElementByTagNameMethod() {
        Document doc = new Document("baseUri");
        doc.findFirstElementByTagName(null, doc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTagNameInFindFirstElementByTagNameMethod() {
        Document doc = new Document("baseUri");
        doc.findFirstElementByTagName("invalidTag", doc);
    }

    @Test
    public void testFindFirstElementByTagNameMethodWithMultipleElements() {
        Document doc = new Document("baseUri");
        Element element1 = doc.appendElement("html");
        Element element2 = doc.appendElement("html");

        Element foundElement = doc.findFirstElementByTagName("html", doc);
        assertEquals(element1, foundElement);
    }

    @Test(expected = IllegalStateException.class)
    public void testNormaliseMethodWithEmptyDocument() {
        Document doc = new Document("baseUri");
        doc.normalise();
    }

    @Test(expected = IllegalStateException.class)
    public void testNormaliseMethodWithDocumentContainingRootElement() {
        Document doc = new Document("baseUri");
        doc.appendElement("html");
        doc.normalise();
    }
}