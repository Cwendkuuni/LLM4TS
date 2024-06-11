
import org.jsoup.nodes.Document;

import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    private Document doc;
    
    @Before
    public void setUp() {
        doc = new Document("https://example.com");
    }
    
    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("https://example.com");
        
        assertNotNull(shellDoc);
        assertNotNull(shellDoc.head());
        assertNotNull(shellDoc.body());
        assertEquals("html", shellDoc.getElementsByTag("html").first().tagName());
        assertEquals("head", shellDoc.getElementsByTag("head").first().tagName());
        assertEquals("body", shellDoc.getElementsByTag("body").first().tagName());
    }
    
    @Test
    public void testHead() {
        assertNull(doc.head());
        
        doc.appendElement("head");
        
        assertNotNull(doc.head());
        assertEquals("head", doc.head().tagName());
    }

    @Test
    public void testBody() {
        assertNull(doc.body());
        
        doc.appendElement("body");
        
        assertNotNull(doc.body());
        assertEquals("body", doc.body().tagName());
    }

    @Test
    public void testTitle() {
        String title = doc.title();
        
        assertEquals("", title);
        
        doc.appendElement("head");
        doc.head().appendElement("title").text("Sample Title");
        
        title = doc.title();
        
        assertEquals("Sample Title", title);
    }

    @Test
    public void testSetTitle() {
        doc.title("New Title");
        
        String title = doc.title();
        
        assertEquals("New Title", title);
        
        doc.appendElement("head");
        doc.head().appendElement("title").text("Old Title");
        
        doc.title("Updated Title");
        
        title = doc.title();
        
        assertEquals("Updated Title", title);
    }

    @Test
    public void testCreateElement() {
        String tagName = "div";
        
        Document.Element newElement = doc.createElement(tagName);
        
        assertNotNull(newElement);
        assertEquals(tagName, newElement.tagName());
        assertEquals(doc.baseUri(), newElement.baseUri());
    }

    @Test
    public void testNormalise() {
        doc.appendElement("html");
        doc.appendElement("head");
        
        doc.normalise();
        
        assertNotNull(doc.getElementsByTag("html").first());
        assertNotNull(doc.getElementsByTag("head").first());
        assertNotNull(doc.body());
    }
    
    @Test
    public void testOuterHtml() {
        doc.appendElement("div").text("Lorem Ipsum");
        
        String outerHtml = doc.outerHtml();
        
        assertEquals("<div>Lorem Ipsum</div>", outerHtml);
    }

    @Test
    public void testSetText() {
        doc.appendElement("body");
        
        doc.text("Sample Text");
        
        assertEquals("Sample Text", doc.body().text());
    }

    @Test
    public void testNodeName() {
        String nodeName = doc.nodeName();
        
        assertEquals("#document", nodeName);
    }
}
