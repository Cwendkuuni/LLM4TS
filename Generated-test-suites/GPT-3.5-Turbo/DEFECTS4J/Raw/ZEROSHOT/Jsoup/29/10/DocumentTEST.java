import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("http://www.example.com");
        assertNotNull(doc);
        assertNotNull(doc.head());
        assertNotNull(doc.body());
        assertEquals("html", doc.head().parent().nodeName());
        assertEquals("html", doc.body().parent().nodeName());
    }
    
    @Test
    public void testHead() {
        Document doc = new Document("http://www.example.com");
        Element head = doc.head();
        assertNull(head);
        
        doc.appendElement("head");
        head = doc.head();
        assertNotNull(head);
    }
    
    @Test
    public void testBody() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.body();
        assertNull(body);
        
        doc.appendElement("body");
        body = doc.body();
        assertNotNull(body);
    }
    
    @Test
    public void testTitle() {
        Document doc = new Document("http://www.example.com");
        String title = doc.title();
        assertEquals("", title);
        
        doc.title("Example Title");
        title = doc.title();
        assertEquals("Example Title", title);
    }
    
    @Test
    public void testTitleNoHead() {
        Document doc = new Document("http://www.example.com");
        doc.normalise();
        doc.title("Example Title");
        String title = doc.title();
        assertEquals("Example Title", title);
    }
    
    @Test
    public void testTitleExistingTitle() {
        Document doc = new Document("http://www.example.com");
        doc.appendElement("head");
        
        Element titleEl = doc.createElement("title");
        titleEl.text("Existing Title");
        doc.head().appendChild(titleEl);
        
        doc.title("Example Title");
        String title = doc.title();
        
        assertEquals("Example Title", title);
        assertEquals(titleEl, doc.getElementsByTag("title").first());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("http://www.example.com");
        
        Element html = doc.appendElement("html");
        Element head = html.appendElement("head");
        Element body = html.appendElement("body");
        
        Element div1 = head.appendElement("div");
        Element div2 = body.appendElement("div");
        
        doc.normalise();
        
        assertEquals(div1, doc.getElementsByTag("head").first());
        assertEquals(div2, doc.getElementsByTag("body").first());
    }
    
    @Test
    public void testNormaliseTextNodes() {
        Document doc = new Document("http://www.example.com");
        Element html = doc.appendElement("html");
        
        TextNode textNode1 = new TextNode("Text Node 1", "");
        TextNode textNode2 = new TextNode("Text Node 2", "");
        TextNode blankTextNode = new TextNode(" ", "");
        
        html.appendChild(textNode1);
        html.prependChild(blankTextNode);
        html.prependChild(textNode2);
        
        doc.normalise();
        Element body = doc.body();
        assertEquals(textNode2, body.childNodes.get(0));
        assertEquals(blankTextNode, body.childNodes.get(1));
        assertEquals(textNode1, body.childNodes.get(2));
    }
    
    @Test
    public void testNormaliseStructure() {
        Document doc = new Document("http://www.example.com");
        Element html = doc.appendElement("html");
        
        Element head1 = html.appendElement("head");
        Element head2 = html.appendElement("head");
        Element body1 = html.appendElement("body");
        Element body2 = html.appendElement("body");
        
        head1.appendElement("title");
        head2.appendElement("title");
        
        doc.normalise();
        
        assertEquals(head1, doc.getElementsByTag("head").first());
        assertEquals(body1, doc.getElementsByTag("body").first());
        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(1, doc.getElementsByTag("body").size());
    }
    
    @Test
    public void testNormaliseStructureNoHtmlTag() {
        Document doc = new Document("http://www.example.com");
        
        Element head1 = doc.appendElement("head");
        Element head2 = doc.appendElement("head");
        Element body1 = doc.appendElement("body");
        Element body2 = doc.appendElement("body");
        
        head1.appendElement("title");
        head2.appendElement("title");
        
        doc.normalise();
        
        assertEquals(head1, doc.getElementsByTag("head").first());
        assertEquals(body1, doc.getElementsByTag("body").first());
        assertEquals(1, doc.getElementsByTag("head").size());
        assertEquals(1, doc.getElementsByTag("body").size());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("http://www.example.com");
        Element body = doc.appendElement("body");
        
        assertEquals("<body></body>", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = new Document("http://www.example.com");
        doc.text("Example Text");
        
        assertEquals("Example Text", doc.body().text());
    }
    
    @Test
    public void testNodeName() {
        Document doc = new Document("http://www.example.com");
        
        assertEquals("#document", doc.nodeName());
    }
    
    @Test
    public void testClone() {
        Document doc = new Document("http://www.example.com");
        doc.title("Example Title");
        
        Document clone = doc.clone();
        
        assertNotSame(doc, clone);
        assertEquals(doc.title(), clone.title());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = new Document("http://www.example.com");
        
        Document.OutputSettings outputSettings = doc.outputSettings();
        assertNotNull(outputSettings);
        
        Document.OutputSettings newOutputSettings = new Document.OutputSettings();
        doc.outputSettings(newOutputSettings);
        assertSame(newOutputSettings, doc.outputSettings());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = new Document("http://www.example.com");
        
        Document.QuirksMode quirksMode = doc.quirksMode();
        assertEquals(Document.QuirksMode.noQuirks, quirksMode);
        
        doc.quirksMode(Document.QuirksMode.quirks);
        quirksMode = doc.quirksMode();
        assertEquals(Document.QuirksMode.quirks, quirksMode);
    }
}