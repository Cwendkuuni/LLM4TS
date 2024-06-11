package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.jsoup.nodes.Document.OutputSettings.*;
import static org.jsoup.nodes.Document.QuirksMode.*;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document doc = new Document("baseUri");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertNull(doc.head());
        Assert.assertNull(doc.body());
        Assert.assertEquals("", doc.title());
        Assert.assertNull(doc.title("title"));
        Assert.assertNull(doc.createElement("tagName"));
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("baseUri");
        Assert.assertEquals("#document", doc.nodeName());
        Assert.assertNotNull(doc.head());
        Assert.assertNotNull(doc.body());
        Assert.assertEquals("", doc.title());
        Assert.assertNull(doc.title("title"));
        Assert.assertNotNull(doc.createElement("tagName"));

        Element html = doc.head();
        Assert.assertEquals("html", html.tagName());
        Assert.assertNotNull(html.head());
        Assert.assertNotNull(html.body());

        Element head = html.head();
        Assert.assertEquals("head", head.tagName());
        Assert.assertNotNull(head.children());

        Element body = html.body();
        Assert.assertEquals("body", body.tagName());
        Assert.assertNotNull(body.children());
    }

    @Test
    public void testGetTitle() {
        Document doc = new Document("baseUri");
        doc.title("title");
        Assert.assertEquals("title", doc.title());
    }

    @Test
    public void testSetTitle() {
        Document doc = new Document("baseUri");
        doc.title("");
        doc.title("title");
        Assert.assertEquals("title", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = new Document("baseUri");
        Element tag = doc.createElement("tagName");
        Assert.assertEquals("tagName", tag.tagName());
        Assert.assertEquals("baseUri", tag.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        TextNode textNode1 = body.appendText("text1");
        TextNode textNode2 = body.appendText("text2 ");
        TextNode textNode3 = body.appendText("text3");

        doc.normalise();

        Assert.assertNull(head.child(0));
        Assert.assertNull(html.child(0));
        Assert.assertEquals("text1 text2 text3 ", body.text());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document doc = new Document("baseUri");
        Element html = doc.appendElement("html");
        Element head = doc.appendElement("head");
        Element body = doc.appendElement("body");

        Element title = doc.findFirstElementByTagName("title", doc);
        Assert.assertNull(title);

        title = doc.findFirstElementByTagName("title", html);
        Assert.assertNull(title);

        title = doc.findFirstElementByTagName("title", head);
        Assert.assertNull(title);

        title = doc.findFirstElementByTagName("title", body);
        Assert.assertNull(title);

        title = html.findFirstElementByTagName("title", doc);
        Assert.assertNull(title);

        title = html.findFirstElementByTagName("title", html);
        Assert.assertNull(title);

        title = html.findFirstElementByTagName("title", head);
        Assert.assertNull(title);

        title = html.findFirstElementByTagName("title", body);
        Assert.assertNull(title);

        title = head.findFirstElementByTagName("title", doc);
        Assert.assertNull(title);

        title = head.findFirstElementByTagName("title", html);
        Assert.assertNull(title);

        title = head.findFirstElementByTagName("title", head);
        Assert.assertNull(title);

        title = head.findFirstElementByTagName("title", body);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("title", doc);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("title", html);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("title", head);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("title", body);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("tagName", doc);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("tagName", html);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("tagName", head);
        Assert.assertNull(title);

        title = body.findFirstElementByTagName("tagName", body);
        Assert.assertNotNull(title);
    }

    @Test
    public void testOutputSettings() {
        Document doc = new Document("baseUri");
        OutputSettings outputSettings = doc.outputSettings();

        Assert.assertEquals(Entities.EscapeMode.base, outputSettings.escapeMode());
        Assert.assertEquals("UTF-8", outputSettings.charset().name());
        Assert.assertNull(outputSettings.encoder());
        Assert.assertTrue(outputSettings.prettyPrint());
        Assert.assertEquals(1, outputSettings.indentAmount());

        OutputSettings newSettings = outputSettings.charset("ISO-8859-1");
        Assert.assertEquals("ISO-8859-1", newSettings.charset().name());
        Assert.assertNotNull(newSettings.encoder());
        Assert.assertTrue(newSettings.prettyPrint());
        Assert.assertEquals(1, newSettings.indentAmount());

        newSettings = newSettings.prettyPrint(false);
        Assert.assertEquals("ISO-8859-1", newSettings.charset().name());
        Assert.assertNotNull(newSettings.encoder());
        Assert.assertFalse(newSettings.prettyPrint());
        Assert.assertEquals(1, newSettings.indentAmount());

        newSettings = newSettings.indentAmount(2);
        Assert.assertEquals("ISO-8859-1", newSettings.charset().name());
        Assert.assertNotNull(newSettings.encoder());
        Assert.assertFalse(newSettings.prettyPrint());
        Assert.assertEquals(2, newSettings.indentAmount());

        newSettings = newSettings.clone();
        Assert.assertEquals("ISO-8859-1", newSettings.charset().name());
        Assert.assertNotNull(newSettings.encoder());
        Assert.assertFalse(newSettings.prettyPrint());
        Assert.assertEquals(2, newSettings.indentAmount());
    }

    @Test
    public void testQuirksMode() {
        Document doc = new Document("baseUri");
        Assert.assertEquals(QuirksMode.noQuirks, doc.quirksMode());

        Document docQuirks = doc.quirksMode(QuirksMode.quirks);
        Assert.assertEquals(QuirksMode.quirks, docQuirks.quirksMode());

        Document docLimitedQuirks = docQuirks.quirksMode(QuirksMode.limitedQuirks);
        Assert.assertEquals(QuirksMode.limitedQuirks, docLimitedQuirks.quirksMode());

        Assert.assertEquals(QuirksMode.noQuirks, doc.quirksMode());
    }
}