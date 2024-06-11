package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert;

public class DocumentTest {

    private Document document;

    @Test
    public void testCreateShell() {
        document = Document.createShell("http://example.com");
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.head());
        Assert.assertNotNull(document.body());
        Assert.assertEquals("html", document.tagName());
        Assert.assertEquals("head", document.head().tagName());
        Assert.assertEquals("body", document.body().tagName());
    }

    @Test
    public void testHead() {
        document = new Document("http://example.com");
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        document = new Document("http://example.com");
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        document = new Document("http://example.com");
        String title = document.title();
        Assert.assertEquals("", title);

        document.title("Test Title");
        String updatedTitle = document.title();
        Assert.assertEquals("Test Title", updatedTitle);
    }

    @Test
    public void testCreatElement() {
        document = new Document("http://example.com");
        Element element = document.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
    }

    @Test
    public void testNormalise() {
        document = new Document("http://example.com");

        // Test before normalisation
        Element bodyBefore = document.body();
        Assert.assertNotNull(bodyBefore);
        Assert.assertTrue(bodyBefore.childNodes.isEmpty());

        // Test normalisation
        document.normalise();

        // Test after normalisation
        Element bodyAfter = document.body();
        Assert.assertNotNull(bodyAfter);
        Assert.assertFalse(bodyAfter.childNodes.isEmpty());
        Assert.assertTrue(bodyAfter.childNodes.get(0) instanceof TextNode);
    }

    @Test
    public void testOuterHtml() {
        document = new Document("http://example.com");
        String outerHtml = document.outerHtml();
        Assert.assertNotNull(outerHtml);
        Assert.assertTrue(outerHtml.contains("<html"));
        Assert.assertTrue(outerHtml.contains("</html>"));
    }

    @Test
    public void testText() {
        document = new Document("http://example.com");
        Element body = document.body();
        body.text("Test Text");

        String text = body.text();
        Assert.assertEquals("Test Text", text);
    }

    @Test
    public void testNodeName() {
        document = new Document("http://example.com");
        String nodeName = document.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
}