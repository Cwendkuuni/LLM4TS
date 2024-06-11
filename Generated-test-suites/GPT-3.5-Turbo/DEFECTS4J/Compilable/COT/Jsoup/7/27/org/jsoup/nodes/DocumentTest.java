package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    
    Document document;
    
    @Before
    public void setUp() {
        document = new Document("http://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shell = Document.createShell("http://example.com");
        assertNotNull(shell.head());
        assertNotNull(shell.body());
    }
    
    @Test
    public void testHead() {
        Element head = document.head();
        assertNull(head);
    }
    
    @Test
    public void testBody() {
        Element body = document.body();
        assertNull(body);
    }
    
    @Test
    public void testTitle() {
        String title = document.title();
        assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        document.title("Test Title");
        String title = document.title();
        assertEquals("Test Title", title);
    }
    
    @Test
    public void testNormalise() {
        Document normalised = document.normalise();
        assertNotNull(normalised);
    }
    
    @Test
    public void testText() {
        document.text("Test Text");
        String bodyText = document.body().text();
        assertEquals("Test Text", bodyText);
    }
    
    @Test
    public void testCreateElement() {
        Element element = document.createElement("div");
        assertNotNull(element);
    }
    
    @Test
    public void testOuterHtml() {
        String outerHtml = document.outerHtml();
        assertEquals("", outerHtml);
    }
    
    @Test
    public void testOutputSettings() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings);
    }
    
    @Test
    public void testOutputSettingsEscapeMode() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings.escapeMode());
    }
    
    @Test
    public void testOutputSettingsCharset() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertNotNull(outputSettings.charset());
    }
    
    @Test
    public void testOutputSettingsPrettyPrint() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(true, outputSettings.prettyPrint());
    }
    
    @Test
    public void testOutputSettingsIndentAmount() {
        Document.OutputSettings outputSettings = document.outputSettings();
        assertEquals(1, outputSettings.indentAmount());
    }
}
