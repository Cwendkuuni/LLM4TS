package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstruction() {
        Document doc = new Document("baseUri");
        Assert.assertEquals("#root", doc.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Element html = doc.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.nodeName());

        html = doc.body();
        Assert.assertNotNull(html);
        Assert.assertEquals("body", html.nodeName());
    }

    @Test
    public void testHeadAccess() {
        Document doc = new Document("baseUri");
        Element head = doc.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.nodeName());
    }

    @Test
    public void testBodyAccess() {
        Document doc = new Document("baseUri");
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitleAccess() {
        Document doc = new Document("baseUri");
        doc.title("Test Title");
        String title = doc.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testTitleSettingNull() {
        Document doc = new Document("baseUri");
        try {
            doc.title(null);
            Assert.fail();
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element element = doc.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.nodeName());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        doc.appendText("Test text");
        Element p = doc.createElement("p");
        p.appendText("Test paragraph");
        doc.appendChild(p);

        doc.normalise();

        List<Node> bodyChildren = doc.body().childNodes();
        Assert.assertEquals(3, bodyChildren.size());
        Assert.assertEquals("Test text", bodyChildren.get(0).text());
        Assert.assertEquals("Test paragraph", bodyChildren.get(1).text());
        Assert.assertEquals(" ", bodyChildren.get(2).nodeName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element body = doc.createElement("body");
        doc.appendChild(html);
        html.appendChild(head);
        html.appendChild(body);

        Element found = doc.findFirstElementByTagName("head", doc);
        Assert.assertEquals(head, found);

        found = doc.findFirstElementByTagName("html", doc);
        Assert.assertEquals(html, found);

        found = doc.findFirstElementByTagName("body", doc);
        Assert.assertEquals(body, found);

        found = doc.findFirstElementByTagName("notag", doc);
        Assert.assertNull(found);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals(Charset.forName("UTF-8"), outputSettings.charset());

        outputSettings.charset("UTF-16");
        Assert.assertEquals(Charset.forName("UTF-16"), outputSettings.charset());

        outputSettings.prettyPrint(false);
        Assert.assertFalse(outputSettings.prettyPrint());

        outputSettings.indentAmount(2);
        Assert.assertEquals(2, outputSettings.indentAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutputSettingsNegativeIndentAmount() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();
        outputSettings.indentAmount(-1);
    }
}