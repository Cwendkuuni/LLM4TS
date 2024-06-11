package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentCreation() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        Element html = document.getElementsByTag("html").first();
        assertNotNull(html);
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testGetHeadElement() {
        Document document = new Document("http://example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testGetBodyElement() {
        Document document = new Document("http://example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testGetTitle() {
        Document document = new Document("<html><head><title>Test Title</title></head><body></body></html>");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("a");
        assertNotNull(element);
        assertEquals("a", element.nodeName());
    }

    @Test
    public void testNormalize() {
        Document document = new Document("<html><head></head><body><p>Test</p><p> test </p></body></html>");
        document.normalise();
        Element body = document.getElementsByTag("body").first();
        assertEquals(1, body.childNodes().size());
        Element p = body.getElementsByTag("p").first();
        assertEquals("Test test", p.text());
    }

    @Test(expected = NullPointerException.class)
    public void testNormalizeWithNullElement() {
        Document document = new Document("http://example.com");
        document.normalise(null);
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
        Element title = document.findFirstElementByTagName("title", document);
        assertNotNull(title);
        assertEquals("title", title.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
        String html = document.outerHtml();
        assertEquals("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>", html);
    }

    @Test
    public void testText() {
        Document document = new Document("<html><body><p>Test</p></body></html>");
        document.text("Test Text");
        Element body = document.getElementsByTag("body").first();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testClone() {
        Document document = new Document("<html><head><title>Test Title</title></head><body><p>Test</p></body></html>");
        Document clonedDocument = document.clone();
        assertEquals(document.hashCode(), document.hashCode());
        assertEquals(clonedDocument.hashCode(), clonedDocument.hashCode());
        assertEquals(document.toString(), clonedDocument.toString());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document();
        OutputSettings outputSettings = document.outputSettings();
        OutputSettings newOutputSettings = new OutputSettings();
        newOutputSettings.prettyPrint(false);
        document.outputSettings(newOutputSettings);
        OutputSettings outputSettingsAfterChange = document.outputSettings();
        assertFalse(outputSettingsAfterChange.prettyPrint());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document();
        QuirksMode quirksMode = QuirksMode.quirks;
        document.quirksMode(quirksMode);
        QuirksMode quirksModeAfterChange = document.quirksMode();
        assertEquals(quirksMode, quirksModeAfterChange);
    }
}