package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document("");
    }

    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
        assertEquals("html", shell.head().nodeName());
        assertEquals("head", shell.head().nodeName());
        assertEquals("body", shell.head().nodeName());
    }

    @Test
    public void testHead() {
        assertNull(document.head());
    }

    @Test
    public void testBody() {
        assertNull(document.body());
    }

    @Test
    public void testTitle() {
        assertEquals("", document.title());

        Element head = document.head();
        head.appendElement("title").text("Title");

        assertEquals("Title", document.title());
    }

    @Test
    public void testSetTitle() {
        document.title("Title");
        assertEquals("Title", document.title());

        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testNormalise() {
        document.normalise();

        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);

        Element head = document.head();
        assertNotNull(head);

        Element body = document.body();
        assertNotNull(body);

        assertEquals("html", html.nodeName());
        assertEquals("head", head.nodeName());
        assertEquals("body", body.nodeName());

        // Test moving text nodes to body
        Element root = document.createElement("root");
        root.appendText("Text 1");
        root.appendElement("div").appendText("Text 2");
        root.appendText("Text 3");

        document.appendChild(root);

        document.normalise();

        Element newBody = document.body();
        assertEquals(3, newBody.children.size());
        assertEquals("Text 1", newBody.childNode(0).outerHtml());
        assertEquals("Text 2", newBody.childNode(1).childNode(0).outerHtml());
        assertEquals("Text 3", newBody.childNode(2).outerHtml());

        // Test merging duplicate head and body tags
        Element html1 = new Element(Tag.valueOf("html"), "");
        html1.appendChild(new Element(Tag.valueOf("head"), ""));
        html1.appendChild(new Element(Tag.valueOf("head"), ""));
        html1.appendChild(new Element(Tag.valueOf("body"), ""));
        html1.appendChild(new Element(Tag.valueOf("body"), ""));

        document.append(html1);

        document.normalise();

        assertEquals(1, document.getElementsByTag("html").size());
        assertEquals(1, document.getElementsByTag("head").size());
        assertEquals(1, document.getElementsByTag("body").size());

        // Test ensuring parented by html
        Element head1 = document.head();
        Element body1 = document.body();

        assertEquals(html, head1.parent());
        assertEquals(html, body1.parent());
    }

    @Test
    public void testText() {
        Element body = document.body();
        assertEquals("", body.text());

        document.text("Hello World");
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testOuterHtml() {
        document.text("Hello World");
        assertEquals("Hello World", document.outerHtml());
    }

    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotSame(document, clone);
        assertNotSame(document.outputSettings(), clone.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);

        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        document.outputSettings(newOutputSettings);

        assertSame(newOutputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);

        Document.QuirksMode newQuirksMode = Document.QuirksMode.quirks;
        document.quirksMode(newQuirksMode);

        assertSame(newQuirksMode, document.quirksMode());
    }
}