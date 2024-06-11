import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://www.example.com");
        assertEquals("#root", doc.tagName());
        assertEquals("https://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("https://www.example.com", doc.baseUri());
    }
    
    @Test
    public void testHeadAccessor() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }
    
    @Test
    public void testBodyAccessor() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
    }
    
    @Test
    public void testTitle() {
        Document doc = Document.createShell("https://www.example.com");
        doc.head().appendElement("title").text("Test Title");
        assertEquals("Test Title", doc.title());
        
        doc.title("New Title");
        assertEquals("New Title", doc.title());

        doc.getElementsByTag("head").first().getElementsByTag("title").first().remove();
        assertEquals("", doc.title());
    }
    
    @Test
    public void testGetElementByTagName() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.getElementsByTag("head").first());
        assertNotNull(doc.getElementsByTag("body").first());
        assertNull(doc.getElementsByTag("div").first());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("https://www.example.com");
        assertNotNull(doc.createElement("div"));
        assertEquals("div", doc.createElement("div").tagName());
        assertEquals("https://www.example.com", doc.createElement("div").baseUri());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://www.example.com");
        doc.body().appendElement("p").text("Paragraph 1");
        doc.body().prependElement("h1").text("Heading 1");
        doc.normalise();
        
        assertEquals("Paragraph 1", doc.body().text());
        assertNotNull(doc.head());
        assertNotNull(doc.select("html").first());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("https://www.example.com");
        doc.body().appendElement("p").text("Paragraph 1");
        doc.body().appendElement("p").text("Paragraph 2");
        doc.body().appendElement("p").text("Paragraph 3");
        
        assertEquals("<body>\n <p>Paragraph 1</p> \n <p>Paragraph 2</p> \n <p>Paragraph 3</p> \n</body>", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = Document.createShell("https://www.example.com");
        doc.text("Some text");
        assertEquals("Some text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = Document.createShell("https://www.example.com");
        assertEquals("#document", doc.nodeName());
    }
}