import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTest {
    @Test
    public void testConstructor() {
        Document document = new Document("http://example.com");
        
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://example.com");
        
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = Document.createShell("http://example.com");
        Element head = document.head();
        
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = Document.createShell("http://example.com");
        Element body = document.body();
        
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = Document.createShell("http://example.com");
        
        assertEquals("", document.title());
        
        document.title("Test Title");
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitle_withExistingTitleElement() {
        Document document = Document.createShell("http://example.com");
        Element titleElement = document.appendElement("title").text("Existing Title");
        
        assertEquals("Existing Title", document.title());
        
        document.title("New Title");
        assertEquals("New Title", titleElement.text());
    }

    @Test
    public void testTitle_withNoHeadElement() {
        Document document = new Document("http://example.com");
        
        assertEquals("", document.title());
        
        document.title("Test Title");
        
        assertNotNull(document.head());
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitle_withNullTitle() {
        Document document = Document.createShell("http://example.com");
        
        assertEquals("", document.title());
        
        document.title(null);
        assertEquals("", document.title());
    }

    @Test
    public void testTitle_withNullTitleElement() {
        Document document = Document.createShell("http://example.com");
        
        assertEquals("", document.title());
        
        document.getElementsByTag("head").first().remove();
        document.title("Test Title");
        
        assertNotNull(document.head());
        assertEquals("Test Title", document.title());
    }

    @Test
    public void testTitle_withBlankTitle() {
        Document document = Document.createShell("http://example.com");
        
        assertEquals("", document.title());
        
        document.title("");
        assertEquals("", document.title());
    }

    @Test
    public void testTitle_withMultipleTitleElements() {
        Document document = Document.createShell("http://example.com");
        document.head().appendElement("title").text("First Title");
        document.head().appendElement("title").text("Second Title");
        
        assertEquals("First Title", document.title());
        
        document.title("New Title");
        assertEquals("New Title", document.title());
    }

    @Test
    public void testElement() {
        Document document = new Document("http://example.com");
        Element element = document.createElement("div");
        
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://example.com");
        Element html = document.appendElement("html");
        html.appendElement("head");
        html.appendElement("head");
        html.appendElement("body");
        
        html.appendText("Text 1");
        document.appendText("Text 2");
        document.childNodes.add(new Comment("Comment"));
        
        assertEquals(2, document.childNodes.size());
        
        document.normalise();
        
        assertEquals(3, document.childNodes.size());
        assertTrue(document.childNodes.get(0) instanceof TextNode);
        assertTrue(document.childNodes.get(1) instanceof Comment);
        assertNotNull(document.childNodes.get(2));
        assertTrue(document.childNodes.get(2) instanceof Element);
    }

    @Test
    public void testNormaliseTextNodes() {
        Document document = new Document("http://example.com");
        Element element = document.appendElement("div");
        
        element.appendText(" Text 1 ");
        element.appendText(" Text 2 ");
        element.appendText(" Text 3 ");
        
        assertEquals(9, element.childNodes.size());
        
        document.normaliseTextNodes(element);
        
        assertEquals(3, element.childNodes.size());
        assertEquals("Text 1", element.childNodes.get(0).toString());
        assertTrue(element.childNodes.get(1) instanceof TextNode);
        assertEquals("Text 3", element.childNodes.get(2).toString());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://example.com");
        Element element = document.appendElement("div");
        element.appendElement("a").attr("href", "http://example.com").text("Link");
        
        assertEquals("<div><a href=\"http://example.com\">Link</a></div>", document.outerHtml());
    }

    @Test
    public void testText() {
        Document document = new Document("http://example.com");
        
        document.text("Example Text");
        
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testNodeName() {
        Document document = new Document("http://example.com");
        
        assertEquals("#document", document.nodeName());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://example.com");
        
        assertNotNull(document.outputSettings());
        assertTrue(document.outputSettings() instanceof Document.OutputSettings);
    }

    @Test
    public void testEscapeMode() {
        Document document = new Document("http://example.com");
        
        assertEquals(Entities.EscapeMode.base, document.outputSettings().escapeMode());
        
        document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals(Entities.EscapeMode.xhtml, document.outputSettings().escapeMode());
    }

    @Test
    public void testCharset() {
        Document document = new Document("http://example.com");
        
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
        
        document.outputSettings().charset(Charset.forName("ISO-8859-1"));
        assertEquals(Charset.forName("ISO-8859-1"), document.outputSettings().charset());
    }

    @Test
    public void testCharset_withString() {
        Document document = new Document("http://example.com");
        
        assertEquals(Charset.forName("UTF-8"), document.outputSettings().charset());
        
        document.outputSettings().charset("ISO-8859-1");
        assertEquals(Charset.forName("ISO-8859-1"), document.outputSettings().charset());
    }

    @Test
    public void testPrettyPrint() {
        Document document = new Document("http://example.com");
        
        assertTrue(document.outputSettings().prettyPrint());
        
        document.outputSettings().prettyPrint(false);
        assertFalse(document.outputSettings().prettyPrint());
    }

    @Test
    public void testIndentAmount() {
        Document document = new Document("http://example.com");
        
        assertEquals(1, document.outputSettings().indentAmount());
        
        document.outputSettings().indentAmount(2);
        assertEquals(2, document.outputSettings().indentAmount());
    }

    @Test
    public void testCharsetEncoder() {
        Document document = new Document("http://example.com");
        CharsetEncoder charsetEncoder = document.outputSettings().encoder();
        
        assertNotNull(charsetEncoder);
        assertEquals(Charset.forName("UTF-8"), charsetEncoder.charset());
    }
}