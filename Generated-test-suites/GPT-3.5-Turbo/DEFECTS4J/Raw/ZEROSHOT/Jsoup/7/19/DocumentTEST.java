package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        String baseUri = "http://example.com";
        Document doc = Document.createShell(baseUri);
        assertNotNull(doc);
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = html.getElementsByTag("head").first();
        assertNotNull(head);
        
        Element body = html.getElementsByTag("body").first();
        assertNotNull(body);
    }

    @Test
    public void testHead() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element head = doc.head();
        assertNull(head);
        
        doc.createElement("head");
        head = doc.head();
        assertNotNull(head);
    }

    @Test
    public void testBody() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element body = doc.body();
        assertNull(body);
        
        doc.createElement("body");
        body = doc.body();
        assertNotNull(body);
    }

    @Test
    public void testTitle() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        String title = doc.title();
        assertEquals("", title);
        
        doc.title("Test Title");
        title = doc.title();
        assertEquals("Test Title", title);
    }

    @Test
    public void testcreateElement() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Element element = doc.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.tagName());
        assertEquals(baseUri, element.baseUri());
    }

    @Test
    public void testNormalise() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        
        body.text("Test Text");
        
        doc.normalise();
        
        Element newBody = doc.body();
        assertTrue(newBody.childNodes().isEmpty());
        assertNull(head.parent());
        assertEquals(newBody.parent(), html);
        assertTrue(html.childNodes().contains(newBody));
    }

    @Test
    public void testOuterHtml() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.createElement("div").text("Test Text");
        
        String html = doc.outerHtml();
        assertTrue(html.contains("Test Text"));
        assertFalse(html.contains("#document"));
    }

    @Test
    public void testText() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        doc.createElement("body");
        
        doc.text("Test Text");
        
        Element body = doc.body();
        assertEquals("Test Text", body.text());
    }

    @Test
    public void testOutputSettings() {
        String baseUri = "http://example.com";
        Document doc = new Document(baseUri);
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        
        assertFalse(outputSettings.prettyPrint());
        assertEquals(1, outputSettings.indentAmount());
        
        outputSettings.prettyPrint(true).indentAmount(2);
        assertTrue(outputSettings.prettyPrint());
        assertEquals(2, outputSettings.indentAmount());
    }
}