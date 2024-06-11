package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.OutputSettings;
import org.jsoup.nodes.QuirksMode;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
        Assert.assertFalse(doc.hasText());
    }

    @Test
    public void testCreateShellDocument() {
        String baseUri = "https://example.com";
        Document doc = Document.createShell(baseUri);
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals("#document", doc.nodeName());
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.child(0);
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        Element body = html.child(1);
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
        Assert.assertFalse(doc.hasText());
    }

    @Test
    public void testGetSetTitle() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        String title = "Test Title";
        doc.title(title);
        Assert.assertEquals(title, doc.title());
    }

    @Test
    public void testCreateElement() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element element = doc.createElement("div");
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals(baseUri, element.baseUri());
        Assert.assertNull(element.parent());
        Assert.assertFalse(element.hasText());
    }

    @Test
    public void testNormaliseDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        Element html = doc.createElement("html");
        doc.appendChild(html);
        Element head = doc.createElement("head");
        Element title = doc.createElement("title").text("Test Title");
        head.appendChild(title);
        html.appendChild(head);
        Element body = doc.createElement("body").text("Test Body");
        html.appendChild(body);

        doc.normalise();

        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertEquals(1, doc.childNodeSize());
        Assert.assertEquals("html", doc.child(0).tagName());
        Assert.assertEquals(3, doc.child(0).childNodeSize());
        Assert.assertEquals("head", doc.child(0).child(0).tagName());
        Assert.assertEquals("title", doc.child(0).child(0).child(0).tagName());
        Assert.assertEquals("Test Title", doc.child(0).child(0).child(0).text());
        Assert.assertEquals("body", doc.child(0).child(1).tagName());
        Assert.assertEquals("Test Body", doc.child(0).child(1).text());
    }

    @Test
    public void testCloneDocument() {
        String baseUri = "https://example.com";
        Document doc = new Document(baseUri);
        OutputSettings outputSettings = new OutputSettings();
        doc.outputSettings(outputSettings);
        Document clonedDoc = doc.clone();
        Assert.assertEquals(baseUri, clonedDoc.baseUri());
        Assert.assertEquals("#document", clonedDoc.nodeName());
        Assert.assertEquals(outputSettings, clonedDoc.outputSettings());
    }
}