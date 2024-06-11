import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testConstructor() {
        Document document = new Document("http://www.example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("http://www.example.com");
        assertNotNull(document);
        assertEquals("#root", document.nodeName());
        assertEquals("http://www.example.com", document.baseUri());
        assertNotNull(document.head());
        assertNotNull(document.body());
    }

    @Test
    public void testHead() {
        Document document = new Document("http://www.example.com");
        Element head = document.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
    }

    @Test
    public void testBody() {
        Document document = new Document("http://www.example.com");
        Element body = document.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }

    @Test
    public void testTitle() {
        Document document = new Document("http://www.example.com");

        // Test when there is no title element
        String title1 = document.title();
        assertEquals("", title1);

        // Test when there is a title element
        document.head().appendElement("title").text("Example Title");
        String title2 = document.title();
        assertEquals("Example Title", title2);
    }

    @Test
    public void testSetTitle() {
        Document document = new Document("http://www.example.com");

        // Test when there is no title element
        document.title("Example Title");
        Element titleElement1 = document.getElementsByTag("title").first();
        assertNotNull(titleElement1);
        assertEquals("Example Title", titleElement1.text());

        // Test when there is a title element
        document.head().appendElement("title").text("Previous Title");
        document.title("New Title");
        Element titleElement2 = document.getElementsByTag("title").first();
        assertNotNull(titleElement2);
        assertEquals("New Title", titleElement2.text());
    }

    @Test
    public void testCreateElement() {
        Document document = new Document("http://www.example.com");
        Element element = document.createElement("div");
        assertNotNull(element);
        assertEquals("div", element.nodeName());
        assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document document = new Document("http://www.example.com");

        // Test when there is no <html> element
        Document normalised1 = document.normalise();
        assertNotNull(normalised1.getElementsByTag("html").first());

        // Test when there is no <head> element
        Element html1 = normalised1.getElementsByTag("html").first();
        html1.removeChild(html1.getElementsByTag("head").first());
        Document normalised2 = document.normalise();
        assertNotNull(normalised2.head());

        // Test when there is no <body> element
        Element html2 = normalised2.getElementsByTag("html").first();
        html2.removeChild(html2.getElementsByTag("body").first());
        Document normalised3 = document.normalise();
        assertNotNull(normalised3.body());
    }

    @Test
    public void testText() {
        Document document = new Document("http://www.example.com");
        document.text("Example Text");
        assertEquals("Example Text", document.body().text());
    }

    @Test
    public void testOuterHtml() {
        Document document = new Document("http://www.example.com");
        assertEquals("", document.outerHtml());
    }

    @Test
    public void testClone() {
        Document document = new Document("http://www.example.com");
        Document clone = document.clone();
        assertNotNull(clone);
        assertNotSame(clone, document);
        assertEquals(clone.baseUri(), document.baseUri());
        assertEquals(clone.outputSettings(), document.outputSettings());
    }

    @Test
    public void testOutputSettings() {
        Document document = new Document("http://www.example.com");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        document.outputSettings(outputSettings);
        assertEquals(outputSettings, document.outputSettings());
    }

    @Test
    public void testQuirksMode() {
        Document document = new Document("http://www.example.com");
        Document.QuirksMode quirksMode = Document.QuirksMode.quirks;
        document.quirksMode(quirksMode);
        assertEquals(quirksMode, document.quirksMode());
    }

}