
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {
    
    @Test
    public void testDocument() {
        Document doc = new Document("http://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testHead() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.head());
    }
    
    @Test
    public void testBody() {
        Document doc = Document.createShell("http://example.com");
        assertNotNull(doc.body());
    }
    
    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        assertEquals("Test Title", doc.title());
    }
    
    @Test
    public void testTitleNoTitleElement() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("", doc.title());
    }
    
    @Test
    public void testTitleNullTitle() {
        Document doc = Document.createShell("http://example.com");
        Throwable exception = assertThrows(NullPointerException.class, () -> {
            doc.title(null);
        });
        assertNotNull(exception);
    }
    
    @Test
    public void testTitleWithExistingTitleElement() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Old Title");
        doc.title("New Title");
        assertEquals("New Title", doc.title());
    }
    
    @Test
    public void testHtmlToElement() {
        Document doc = Document.createShell("http://example.com");
        Element el = doc.createElement("div");
        assertEquals("div", el.tagName());
        assertEquals("http://example.com", el.baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://example.com");
        doc.normalise();
        assertNotNull(doc.select("html"));
        assertNotNull(doc.head());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://example.com");
        assertEquals(doc.html(), doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = Document.createShell("http://example.com");
        doc.text("Test Text");
        assertEquals("Test Text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://example.com");
        assertEquals("#document", doc.nodeName());
    }
}
