import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DocumentTest {

    @Test
    public void test_createShell() {
        Document document = Document.createShell("https://example.com");
        
        assertNotNull(document.head());
        assertNotNull(document.body());
        
        assertEquals("html", document.getElementsByTag("html").first().tagName());
        assertEquals("head", document.getElementsByTag("head").first().tagName());
        assertEquals("body", document.getElementsByTag("body").first().tagName());
    }

    @Test
    public void test_head() {
        Document document = new Document("https://example.com");
        
        assertNull(document.head());
        
        document.appendElement("head");
        
        assertNotNull(document.head());
        assertEquals("head", document.head().tagName());
    }

    @Test
    public void test_body() {
        Document document = new Document("https://example.com");
        
        assertNull(document.body());
        
        document.appendElement("body");
        
        assertNotNull(document.body());
        assertEquals("body", document.body().tagName());
    }

    @Test
    public void test_title() {
        Document document = new Document("https://example.com");
        
        assertEquals("", document.title());
        
        document.head().appendElement("title").text("Test Title");
        
        assertEquals("Test Title", document.title());
    }

    @Test
    public void test_title_withExistingTitleElement() {
        Document document = new Document("https://example.com");
        document.head().appendElement("title").text("Existing Title");
        
        document.title("New Title");
        
        assertEquals("New Title", document.title());
    }

    @Test
    public void test_createElement() {
        Document document = new Document("https://example.com");
        
        assertEquals("a", document.createElement("a").tagName());
        assertEquals("div", document.createElement("div").tagName());
    }

    @Test
    public void test_normalise() {
        Document document = Document.createShell("https://example.com");
        document.body().appendChild(new org.jsoup.nodes.TextNode("Hello", ""));
        document.appendElement("div").appendChild(new org.jsoup.nodes.TextNode("World", ""));
        
        Document normalizedDoc = document.normalise();
        
        assertTrue(normalizedDoc.select("html").size() > 0);
        assertNotNull(normalizedDoc.head());
        assertNotNull(normalizedDoc.body());
        assertEquals("Hello ", normalizedDoc.body().text());
    }
}