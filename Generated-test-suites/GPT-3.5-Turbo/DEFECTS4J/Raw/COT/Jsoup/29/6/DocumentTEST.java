
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document doc = new Document("https://example.com");
        Assert.assertNotNull(doc);
    }

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://example.com");
        Assert.assertNotNull(doc);
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("https://example.com");
        Element head = doc.head();
        Assert.assertNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("https://example.com");
        Element body = doc.body();
        Assert.assertNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("https://example.com");
        String title = doc.title();
        Assert.assertEquals("", title);
    }
    
    @Test
    public void testSetTitle() {
        Document doc = new Document("https://example.com");
        doc.title("Title");
        String title = doc.title();
        Assert.assertEquals("Title", title);
    }
    
    @Test
    public void testCreateElement() {
        Document doc = new Document("https://example.com");
        Element element = doc.createElement("div");
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Document.createShell("https://example.com");
        doc.normalise();
        Element html = doc.getElementsByTag("html").first();
        Element head = doc.getElementsByTag("head").first();
        Element body = doc.getElementsByTag("body").first();
        Assert.assertNotNull(html);
        Assert.assertNotNull(head);
        Assert.assertNotNull(body);
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("https://example.com");
        String outerHtml = doc.outerHtml();
        Assert.assertEquals("", outerHtml);
    }
    
    @Test
    public void testText() {
        Document doc = new Document("https://example.com");
        doc.text("Text");
        String text = doc.text();
        Assert.assertEquals("Text", text);
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("https://example.com");
        String nodeName = doc.nodeName();
        Assert.assertEquals("#document", nodeName);
    }
    
    @Test
    public void testClone() {
        Document doc = new Document("https://example.com");
        Document clone = doc.clone();
        Assert.assertNotNull(clone);
        Assert.assertNotSame(doc, clone);
        Assert.assertEquals(doc.outerHtml(), clone.outerHtml());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("https://example.com");
        Document.OutputSettings outputSettings = doc.outputSettings();
        Assert.assertNotNull(outputSettings);
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("https://example.com");
        Document.QuirksMode quirksMode = doc.quirksMode();
        Assert.assertEquals(Document.QuirksMode.noQuirks, quirksMode);
    }
}
