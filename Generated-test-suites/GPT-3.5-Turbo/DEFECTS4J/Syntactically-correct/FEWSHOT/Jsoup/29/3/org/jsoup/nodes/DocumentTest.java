package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    private Document document;
    private Element element;
    
    @Before
    public void setUp() {
        document = new Document("");
        element = new Element(Tag.valueOf("div"), "");
        document.appendChild(element);
    }
    
    @Test
    public void testDocument() {
        assertEquals("", document.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        assertNotNull(document.head());
    }
    
    @Test
    public void testBody() {
        assertNotNull(document.body());
    }
    
    @Test
    public void testTitle() {
        assertEquals("", document.title());
        
        document.title("Title");
        assertEquals("Title", document.title());
        
        Element titleElement = new Element(Tag.valueOf("title"), "");
        titleElement.text("New Title");
        document.body().appendChild(titleElement);
        assertEquals("New Title", document.title());
    }
    
    @Test
    public void testTitleNull() {
        Validate.notNull(null);
    }
    
    @Test
    public void testTitleElement() {
        document.body().appendChild(new Element(Tag.valueOf("title"), ""));
        assertNotNull(document.getElementsByTag("title"));
    }
    
    @Test
    public void testTitleElementText() {
        document.body().appendChild(new Element(Tag.valueOf("title"), "").text("Title Text"));
        Element titleElement = document.getElementsByTag("title").first();
        assertNotNull(titleElement);
        assertEquals("Title Text", titleElement.text());
    }
    
    @Test
    public void testCreateElement() {
        Element newElement = document.createElement("div");
        assertNotNull(newElement);
        assertEquals("div", newElement.tagName());
        assertEquals("", newElement.baseUri());
    }
    
    @Test
    public void testNormalise() {
        document.normalise();
        assertEquals("<html><head></head><body><div></div></body></html>", document.outerHtml());
    }
    
    @Test
    public void testFirstChildElementByTagName() {
        Element foundElement = document.findFirstElementByTagName("div", document);
        assertNotNull(foundElement);
        assertEquals("div", foundElement.tagName());
    }
    
    @Test
    public void testFirstChildElementByTagNameNotFound() {
        Element foundElement = document.findFirstElementByTagName("span", document);
        assertNull(foundElement);
    }
    
    @Test
    public void testOuterHtml() {
        assertEquals("<div></div>", document.outerHtml());
    }
    
    @Test
    public void testText() {
        assertEquals("", document.text());
        
        document.text("Text");
        assertEquals("Text", document.text());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#document", document.nodeName());
    }
    
    @Test
    public void testClone() {
        Document clone = document.clone();
        assertNotNull(clone);
        assertEquals(document.outerHtml(), clone.outerHtml());
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
        document.outputSettings(outputSettings);
        
        outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Document.OutputSettings.EscapeMode.base);
        document.outputSettings(outputSettings);
        
        assertEquals(Document.OutputSettings.EscapeMode.base, document.outputSettings().escapeMode());
    }
    
    @Test
    public void testQuirksMode() {
        Document.QuirksMode quirksMode = document.quirksMode();
        assertNotNull(quirksMode);
        
        document.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, document.quirksMode());
    }

}