import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(Tag.valueOf("#root"), doc.tagName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
    }

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(Tag.valueOf("html"), doc.tagName());
        Element html = doc.head();
        Assert.assertEquals(Tag.valueOf("head"), html.tagName());
        Assert.assertEquals(Tag.valueOf("title"), html.firstChild().tagName());
        Assert.assertNull(html.lastChild());
        Assert.assertEquals(Tag.valueOf("body"), doc.body().tagName());
        Assert.assertNull(doc.title());
    }

    @Test
    public void testGetAndSetTitle() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        Assert.assertNull(doc.title());
        Attributes titleAttributes = new Attributes();
        titleAttributes.put("id", "myTitle");
        Element titleElement = doc.createElement("title");
        titleElement.attributes().putAll(titleAttributes);
        doc.head().appendElement(titleElement);
        Assert.assertEquals("myTitle", doc.title());
        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        Assert.assertEquals(Tag.valueOf("p"), p.tagName());
        Assert.assertEquals("", p.baseUri());
    }

    @Test
    public void testNormalize() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        TextNode t1 = doc.newTextNode("Text 1");
        TextNode t2 = doc.newTextNode("Text 2");
        p.appendChild(t1);
        p.appendChild(t2);
        doc.appendChild(p);
        doc.normalize();
        Element body = doc.body();
        Assert.assertEquals(Tag.valueOf("p"), body.firstChild().tagName());
        Assert.assertEquals("Text 1Text 2", body.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeNullElement() {
        Document doc = new Document("");
        doc.normalise();
        doc.findFirstElementByTagName(null, doc).remove();
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("");
        Element html = doc.createElement("html");
        doc.appendChild(html);
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        head.appendChild(doc.createElement("title"));
        html.appendChild(head);
        html.appendChild(body);
        Element title = doc.findFirstElementByTagName("title", doc);
        Assert.assertEquals(Tag.valueOf("title"), title.tagName());
        Assert.assertNull(doc.findFirstElementByTagName(null, doc));
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        TextNode t1 = doc.newTextNode("Text 1");
        TextNode t2 = doc.newTextNode("Text 2");
        p.appendChild(t1);
        p.appendChild(t2);
        doc.appendChild(p);
        String expected = "<html><head></head><body><p>Text 1Text 2</p></body></html>";
        Assert.assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        TextNode t1 = doc.newTextNode("Text 1");
        TextNode t2 = doc.newTextNode("Text 2");
        p.appendChild(t1);
        p.appendChild(t2);
        doc.appendChild(p);
        doc.text("New Text");
        String expected = "<html><head></head><body>New Text</body></html>";
        Assert.assertEquals(expected, doc.outerHtml());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        TextNode t1 = doc.newTextNode("Text 1");
        TextNode t2 = doc.newTextNode("Text 2");
        p.appendChild(t1);
        p.appendChild(t2);
        doc.appendChild(p);
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertEquals("p", p.nodeName());
        Assert.assertEquals("Text 1", t1.nodeName());
        Assert.assertEquals("Text 2", t2.nodeName());
    }

    @Test
    public void testClone() {
        Document doc = new Document("");
        Element p = doc.createElement("p");
        TextNode t1 = doc.newTextNode("Text 1");
        TextNode t2 = doc.newTextNode("Text 2");
        p.appendChild(t1);
        p.appendChild(t2);
        doc.appendChild(p);
        Document cloned = doc.clone();
        String expected = "<html><head></head><body><p>Text 1Text 2</p></body></html>";
        Assert.assertEquals(expected, cloned.outerHtml());
    }
}