package org.jsoup.nodes;
import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("http://example.com");
        assertNotNull(document);
    }
    
    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        assertNotNull(document);
        assertEquals(3, document.select("html").size());
        assertEquals(1, document.select("head").size());
        assertEquals(1, document.select("body").size());
    }
    
    @Test
    public void testHead() {
        Document document = new Document("http://example.com");
        assertNotNull(document.head());
    }
    
    @Test
    public void testBody() {
        Document document = new Document("http://example.com");
        assertNotNull(document.body());
    }
    
    @Test
    public void testTitle() {
        Document document = new Document("http://example.com");
        assertEquals("", document.title());
    }
    
    @Test
    public void testSetTitle() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testSetTitleWithExistingTitleElement() {
        Document document = new Document("http://example.com");
        document.head().appendElement("title").text("Existing Title");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }
    
    @Test
    public void testSetTitleWithoutExistingTitleElement() {
        Document document = new Document("http://example.com");
        document.title("Test Title");
        assertEquals("Test Title", document.title());
        assertEquals(1, document.select("title").size());
        assertEquals("Test Title", document.select("title").first().text());
    }
    
    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        document.body().text("Test Text");
        document.normalise();
        assertEquals("Test Text", document.body().text());
        assertEquals(1, document.body().childNodes.size());
    }
    
    @Test
    public void testNormaliseWithExistingHtmlHeadAndBody() {
        Document document = new Document("http://example.com");
        document.appendElement("html");
        document.appendElement("head");
        document.appendElement("body");
        document.body().text("Test Text");
        document.normalise();
        assertEquals("Test Text", document.body().text());
        assertEquals(1, document.body().childNodes.size());
    }
    
    @Test
    public void testNormaliseWithoutBody() {
        Document document = new Document("http://example.com");
        document.normalise();
        assertNotNull(document.body());
    }
    
    @Test
    public void testNormaliseWithoutHead() {
        Document document = new Document("http://example.com");
        document.normalise();
        assertNotNull(document.head());
    }
    
    @Test
    public void testNormaliseWithoutHtml() {
        Document document = new Document("http://example.com");
        document.normalise();
        assertEquals(1, document.select("html").size());
    }
    
    @Test
    public void testNormaliseTextNode() {
        Document document = new Document("http://example.com");
        TextNode textNode = new TextNode("Test Text", "");
        document.normalise(textNode);
        assertEquals(" ", document.body().childNodes.get(0).outerHtml());
        assertEquals("Test Text", document.body().childNodes.get(1).outerHtml());
    }
    
    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        assertEquals(document.html(), document.outerHtml());
    }
    
    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        document.text("Test Text");
        assertEquals("Test Text", document.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        assertEquals("#document", document.nodeName());
    }
}