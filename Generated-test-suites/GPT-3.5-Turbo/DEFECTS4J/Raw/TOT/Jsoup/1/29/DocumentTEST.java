import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("http://www.example.com");
        Assert.assertEquals("#root", doc.tagName());
        Assert.assertEquals("http://www.example.com", doc.baseUri());
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();

        // Check if html, head, and body elements are created
        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
    }

    @Test
    public void testHead() {
        Document doc = Document.createShell("http://www.example.com");
        Element head = doc.head();
        
        // Check if head element is retrieved correctly
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBody() {
        Document doc = Document.createShell("http://www.example.com");
        Element body = doc.body();
        
        // Check if body element is retrieved correctly
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitle() {
        Document doc = Document.createShell("http://www.example.com");
        
        // Test scenarios with and without title element
        String noTitle = doc.title();
        Assert.assertEquals("", noTitle);

        doc.title("Test Title");
        String withTitle = doc.title();
        Assert.assertEquals("Test Title", withTitle);
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://www.example.com");
        Element element = doc.createElement("div");
        
        // Check if new element is created with the correct tag name
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals("http://www.example.com", element.baseUri());
    }

    @Test
    public void testNormalise() {
        Document doc = Document.createShell("http://www.example.com");
        
        // Test scenarios with different elements existing in the document
        doc.normalise();
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.head();
        Element body = doc.body();
        
        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);

        // Test scenario with text nodes outside body element
        doc.text("Text outside body element");
        doc.normalise();
        Element newBody = doc.body();
        
        // Check if text node is moved inside body element
        List<Element> textNodes = newBody.getElementsByTag("#text");
        Assert.assertEquals(1, textNodes.size());
        Assert.assertEquals("Text outside body element", textNodes.get(0).text());
    }

    @Test
    public void testOuterHtml() {
        Document doc = Document.createShell("http://www.example.com");
        
        // Check if outerHtml() returns the same result as html()
        Assert.assertEquals(doc.html(), doc.outerHtml());
    }

    @Test
    public void testText() {
        Document doc = Document.createShell("http://www.example.com");
        
        // Test scenario with setting text of body element
        doc.text("Test text");
        Element body = doc.body();
        
        // Check if text is correctly set in the body element
        Assert.assertEquals(1, body.childNodes().size());
        Assert.assertEquals("Test text", body.text());
    }

    @Test
    public void testNodeName() {
        Document doc = Document.createShell("http://www.example.com");
        
        // Check if node name is correctly returned as "#document"
        Assert.assertEquals("#document", doc.nodeName());
    }
}