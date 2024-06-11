import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocumentWithBaseUri() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("http://example.com", doc.baseUri());
        Assert.assertEquals("#document", doc.nodeName());
    }

    @Test
    public void testCreateEmptyDocumentShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("head", html.tagName());

        html = doc.body();
        Assert.assertNotNull(html);
        Assert.assertEquals("body", html.tagName());

        Assert.assertNull(doc.title());
    }

    @Test
    public void testGetTitleElement() {
        Document doc = new Document("http://example.com");
        doc.title("Test Title");

        Element title = doc.head().getElementsByTag("title").first();
        Assert.assertNotNull(title);
        Assert.assertEquals("Test Title", title.text().trim());
    }

    @Test
    public void testSetTitleElement() {
        Document doc = new Document("http://example.com");

        doc.title("Test Title 1");
        Element title = doc.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title 1", title.text().trim());

        doc.title("Test Title 2");
        title = doc.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title 2", title.text().trim());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document doc = new Document("http://example.com");
        doc.head().appendElement("title").text("Test Title");
        doc.body().appendElement("p").text("Test Content");
        doc.appendElement("div").text("Test Div");

        doc.normalise();

        Element title = doc.head().getElementsByTag("title").first();
        Assert.assertEquals("Test Title", title.text().trim());

        Element body = doc.body();
        Element p = body.getElementsByTag("p").first();
        Assert.assertEquals("Test Content", p.text().trim());
        Assert.assertNull(body.getElementsByTag("div").first());
    }

    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://example.com");
        Element p = doc.createElement("p");
        p.appendText("Test ");
        p.appendText("Content");

        doc.body().appendChild(p);
        doc.normalise();

        Element body = doc.body();
        TextNode textNode1 = body.getElementsByTag("text").first();
        Assert.assertEquals("Test ", textNode1.text());
        TextNode textNode2 = body.getElementsByTag("text").get(1);
        Assert.assertEquals("Content", textNode2.text());
    }

    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("http://example.com");
        Element p1 = doc.createElement("p");
        p1.appendText("Test ");
        Element p2 = doc.createElement("p");
        p2.appendText("Content");
        doc.body().appendChild(p1);
        doc.body().appendChild(p2);

        doc.normalise();

        Element body = doc.body();
        Element masterP = body.getElementsByTag("p").first();
        Assert.assertNotNull(masterP);
        List<Node> nodesToMove = new ArrayList<>();
        for (int i = 1; i < body.childNodes().size(); i++) {
            Node node = body.childNodes().get(i);
            nodesToMove.add(node);
            body.removeChild(node);
        }

        for (Node node : nodesToMove) {
            masterP.appendChild(node);
        }

        Assert.assertEquals(2, masterP.childNodes().size());
        Assert.assertEquals("Test ", masterP.childNodes().get(0).text());
        Assert.assertEquals("Content", masterP.childNodes().get(1).text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("http://example.com");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        Element p = doc.createElement("p");

        html.appendChild(head);
        html.appendChild(body);
        head.appendChild(p);

        doc.appendChild(html);

        Element element = doc.findFirstElementByTagName("p", html);
        Assert.assertEquals(p, element);
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        Element title = doc.createElement("title");
        title.text("Test Title");

        html.appendChild(head);
        html.appendChild(body);
        head.appendChild(title);

        doc.appendChild(html);

        doc.title("Test Document Title");

        String htmlOutput = doc.outerHtml();
        String expectedHtmlOutput = "<html><head><title>Test Document Title</title></head><body></body></html>";
        Assert.assertEquals(expectedHtmlOutput, htmlOutput);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        Element body = doc.createElement("body");
        body.text("Test Text");

        doc.appendChild(body);
        String textOutput = doc.text();
        String expectedTextOutput = "Test Text";
        Assert.assertEquals(expectedTextOutput, textOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTitle() {
        Document doc = new Document("http://example.com");
        doc.title(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        Document doc = new Document("http://example.com");
        doc.title("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        Document doc = new Document(null);
    }
}