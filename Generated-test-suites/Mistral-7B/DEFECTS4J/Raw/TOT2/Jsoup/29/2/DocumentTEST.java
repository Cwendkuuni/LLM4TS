import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        assertTrue(doc instanceof Document);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        assertEquals(Tag.valueOf("#root"), doc.tagName());
        assertNull(doc.head());
        assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertTrue(doc instanceof Document);
        assertEquals("#document", doc.nodeName());
        assertEquals(baseUri, doc.baseUri());
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testGetHeadAndBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();
        Element body = doc.body();
        assertNotNull(head);
        assertNotNull(body);
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        String oldTitle = doc.title();
        doc.title("New Title");
        String newTitle = doc.title();
        assertEquals("New Title", newTitle);
        assertNotEquals(oldTitle, newTitle);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document();
        Element newElement = doc.createElement("div");
        assertNotNull(newElement);
        assertEquals("div", newElement.tagName());
        assertNull(newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.text("Test Text");
        Element div = doc.createElement("div").text("Div Text");
        doc.appendChild(div);
        doc.normalise();
        TextNode textNode = (TextNode) doc.getElementsByTag("body").first().child(0);
        assertEquals("Test TextDiv Text", textNode.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormaliseWithNullElement() {
        Document doc = new Document();
        doc.normaliseStructure("head", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstElementByTagNameWithInvalidTagName() {
        Document doc = new Document();
        Element invalidElement = doc.findFirstElementByTagName("invalid", this);
        assertNull(invalidElement);
    }

    @Test
    public void testTextAndHtmlMethods() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        doc.text("Test Text");
        doc.body().text("Body Text");
        doc.normalise();
        String html = doc.html();
        String outerHtml = doc.outerHtml();
        String text = doc.text();
        assertEquals("<html><head><title>Test Title</title></head><body>Body TextTest Text</body></html>", html);
        assertEquals("<html><head><title>Test Title</title></head><body>Test TextBody Text</body></html>", outerHtml);
        assertEquals("Test TextBody Text", text);
    }

    @Test
    public void testClone() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        doc.text("Test Text");
        Document clone = doc.clone();
        assertEquals("#document", clone.nodeName());
        assertEquals("Test Title", clone.title());
        assertEquals("Test Text", clone.text());
    }
}