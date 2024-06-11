import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.nio.charset.Charset;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("#root", doc.tagName());
        Element html = doc.head();
        Assert.assertNull(html);
        html = doc.body();
        Assert.assertNull(html);
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("head", html.tagName());
        Element head = html.head();
        Assert.assertNull(head);
        html = doc.body();
        Assert.assertNotNull(html);
        Assert.assertEquals("body", html.tagName());
    }

    @Test
    public void testGetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
        doc.title(null);
        title = doc.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testSetTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
        String newTitle = "New Title";
        doc.title(newTitle);
        Assert.assertEquals(newTitle, doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateElementWithNullTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.createElement(null);
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.title("Test Title");
        doc.appendText(" test text ");
        doc.head().appendText(" test head text ");
        doc.appendElement("test").text(" test text ");
        doc.normalise();
        Element body = doc.body();
        Assert.assertEquals(" test text ", body.text());
        Element title = doc.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title", title.text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseWithNullElement() {
        Document doc = new Document("");
        doc.normalise();
        doc.normaliseTextNodes(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");
        Element title = doc.appendElement("title");
        title.text("Test Title");
        Element h1 = doc.createElement("h1");
        h1.text("Test Heading");
        html.appendElement(head);
        head.appendElement(title);
        head.appendElement(h1);
        head.appendElement(body);
        body.appendElement(h1);

        Element foundTitle = doc.findFirstElementByTagName("title", doc);
        Assert.assertEquals(title, foundTitle);
        Element foundH1 = doc.findFirstElementByTagName("h1", doc);
        Assert.assertEquals(h1, foundH1);
        Element notFound = doc.findFirstElementByTagName("div", doc);
        Assert.assertNull(notFound);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("");
        OutputSettings outputSettings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertNotNull(outputSettings.encoder());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Assert.assertEquals(Entities.EscapeMode.extended, doc.outputSettings().escapeMode());

        outputSettings.charset("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", doc.outputSettings().charset().name());

        outputSettings.prettyPrint(false);
        Assert.assertFalse(doc.outputSettings().prettyPrint());

        outputSettings.indentAmount(2);
        Assert.assertEquals(2, doc.outputSettings().indentAmount());
    }
}