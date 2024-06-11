import org.junit.Assert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.List;

public class DocumentTest {

    @org.junit.Test
    public void testCreateDocument() {
        Document document = new Document("http://example.com");
        Assert.assertEquals("#document", document.nodeName());
    }

    @org.junit.Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.getFirstChild();
        Assert.assertNotNull(head);
        Element body = head.getNextSibling();
        Assert.assertNotNull(body);
        Assert.assertEquals("head", head.tagName());
        Assert.assertEquals("body", body.tagName());
    }

    @org.junit.Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        document.title("Test Title");
        Assert.assertEquals("Test Title", document.title());
    }

    @org.junit.Test
    public void testSetTitleNull() {
        Document document = Document.createShell("http://example.com");
        Assert.assertNull(document.title());
        document.title(null);
        Assert.assertNull(document.title());
    }

    @org.junit.Test
    public void testCreateElement() {
        Document document = Jsoup.parse("<html><body></body></html>");
        Element p = document.createElement("p");
        Assert.assertEquals("p", p.tagName());
    }

    @org.junit.Test
    public void testNormalise() {
        Document document = Jsoup.parse("<html><head>Hello</head><body></body></html>");
        document.normalise();
        Assert.assertEquals(1, document.children().size());
        Element body = document.body();
        Assert.assertEquals(1, body.childNodes().size());
        TextNode textNode = (TextNode) body.childNodes().get(0);
        Assert.assertEquals("Hello", textNode.text());
    }

    @org.junit.Test
    public void testFindFirstElementByTagName() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><p>Test</p></body></html>");
        Element title = document.findFirstElementByTagName("title");
        Assert.assertNotNull(title);
        Assert.assertEquals("title", title.tagName());
        Element body = document.findFirstElementByTagName("body");
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
        Element p = document.findFirstElementByTagName("p", body);
        Assert.assertNotNull(p);
        Assert.assertEquals("p", p.tagName());
    }

    @org.junit.Test
    public void testText() {
        Document document = Jsoup.parse("<html><body><p>Test</p></body></html>");
        document.body().text("New Text");
        Assert.assertEquals("New Text", document.body().text());
    }

    @org.junit.Test
    public void testOutputSettings() {
        Document document = Jsoup.parse("<html><body><p>Test</p></body></html>");
        OutputSettings outputSettings = document.outputSettings();
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }

    @org.junit.Test
    public void testSetOutputSettings() {
        Document document = Jsoup.parse("<html><body><p>Test</p></body></html>");
        OutputSettings outputSettings = new OutputSettings();
        outputSettings.charset("UTF-16");
        outputSettings.prettyPrint(false);
        outputSettings.indentAmount(2);
        document.outputSettings(outputSettings);
        Charset charset = document.outputSettings().charset();
        Assert.assertEquals(Charset.forName("UTF-16"), charset);
        Assert.assertFalse(document.outputSettings().prettyPrint());
        Assert.assertEquals(2, document.outputSettings().indentAmount());
    }
}