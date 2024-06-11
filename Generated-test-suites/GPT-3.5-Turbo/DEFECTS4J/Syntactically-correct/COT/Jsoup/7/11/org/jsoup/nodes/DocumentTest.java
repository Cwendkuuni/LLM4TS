
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testConstructor() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("#root", doc.nodeName());
        Assert.assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();

        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = new Document("http://example.com");
        Element head = doc.head();

        Assert.assertNull(head);

        doc.appendElement("head");

        head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = new Document("http://example.com");
        Element body = doc.body();

        Assert.assertNull(body);

        doc.appendElement("body");

        body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = new Document("http://example.com");

        String title = doc.title();
        Assert.assertEquals("", title);

        doc.appendElement("head").appendElement("title").text("Test Title");

        title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleWithNull() {
        Document doc = new Document("http://example.com");

        String title = doc.title();
        Assert.assertEquals("", title);

        doc.appendElement("head").appendElement("title").text(null);

        title = doc.title();
        Assert.assertEquals("", title);
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("http://example.com");
        Element element = doc.createElement("div");

        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("http://example.com");

        doc.normalise();

        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();

        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
        Assert.assertEquals(1, html.childNodeSize());
        Assert.assertEquals(1, html.childNodeSize());
        Assert.assertEquals(1, html.childNodeSize());
    }

    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://example.com");
        doc.appendElement("p").text("Test Text");

        String html = doc.outerHtml();

        Assert.assertEquals("<p>Test Text</p>", html);
    }

    @Test
    public void testText() {
        Document doc = new Document("http://example.com");
        doc.text("Test Text");

        Element body = doc.body();

        Assert.assertEquals("Test Text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = new Document("http://example.com");

        String nodeName = doc.nodeName();

        Assert.assertEquals("#document", nodeName);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();

        Assert.assertNotNull(outputSettings);
        Assert.assertEquals(Document.OutputSettings.class, outputSettings.getClass());
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());
    }
}
