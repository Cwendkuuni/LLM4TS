import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTest {

    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("https://www.example.com");
        
        assertNotNull(doc);
        assertEquals("#root", doc.nodeName());
        
        Element html = doc.getElementsByTag("html").first();
        assertNotNull(html);
        
        Element head = doc.head();
        assertNotNull(head);
        assertEquals("head", head.nodeName());
        
        Element body = doc.body();
        assertNotNull(body);
        assertEquals("body", body.nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>");
        
        String title = doc.title();
        assertEquals("Test Title", title);
        
        doc.title("New Title");
        assertEquals("New Title", doc.title());
        
        Element titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("title", titleEl.nodeName());
        assertEquals("New Title", titleEl.text());
        
        doc = Jsoup.parse("<html><head><meta charset=\"UTF-8\"></head><body></body></html>");
        doc.title("Title");
        assertEquals("Title", doc.title());
        
        titleEl = doc.getElementsByTag("title").first();
        assertNotNull(titleEl);
        assertEquals("title", titleEl.nodeName());
        assertEquals("Title", titleEl.text());
    }
    
    @Test
    public void testNormalise() {
        Document doc = Jsoup.parse("<html><head></head><div>Text1</div><div>Text2</div><body></body></html>");
        doc.normalise();
        
        Element body = doc.body();
        assertNotNull(body);
        
        int elementCount = body.children().size();
        assertEquals(2, elementCount);
        
        Element firstDiv = body.child(0);
        assertNotNull(firstDiv);
        assertEquals("div", firstDiv.nodeName());
        assertEquals("Text1", firstDiv.text());
        
        Element secondDiv = body.child(1);
        assertNotNull(secondDiv);
        assertEquals("div", secondDiv.nodeName());
        assertEquals("Text2", secondDiv.text());
    }
    
    @Test
    public void testText() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        doc.text("Example Text");
        
        Element body = doc.body();
        assertNotNull(body);
        
        int elementCount = body.children().size();
        assertEquals(1, elementCount);
        
        Element textElement = body.child(0);
        assertNotNull(textElement);
        assertEquals("#text", textElement.nodeName());
        assertEquals("Example Text", textElement.text());
    }
    
    @Test
    public void testClone() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        Document clone = doc.clone();
        
        assertNotNull(clone);
        assertNotSame(doc, clone);
        assertEquals(doc.outerHtml(), clone.outerHtml());
        
        // Ensure the outputSettings is cloned correctly
        assertNotSame(doc.outputSettings(), clone.outputSettings());
        assertEquals(doc.outputSettings().escapeMode(), clone.outputSettings().escapeMode());
        assertEquals(doc.outputSettings().charset().name(), clone.outputSettings().charset().name());
        assertEquals(doc.outputSettings().prettyPrint(), clone.outputSettings().prettyPrint());
        assertEquals(doc.outputSettings().indentAmount(), clone.outputSettings().indentAmount());
    }
    
    @Test
    public void testOutputSettings() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        Document.OutputSettings outputSettings = new Document.OutputSettings()
            .escapeMode(Entities.EscapeMode.base)
            .charset(Charset.forName("UTF-8"))
            .prettyPrint(true)
            .indentAmount(2);
            
        doc.outputSettings(outputSettings);
        
        assertSame(outputSettings, doc.outputSettings());
        assertEquals(outputSettings.escapeMode(), doc.outputSettings().escapeMode());
        assertEquals(outputSettings.charset().name(), doc.outputSettings().charset().name());
        assertEquals(outputSettings.prettyPrint(), doc.outputSettings().prettyPrint());
        assertEquals(outputSettings.indentAmount(), doc.outputSettings().indentAmount());
    }
    
    @Test
    public void testQuirksMode() {
        Document doc = Jsoup.parse("<html><head></head><body></body></html>");
        
        assertEquals(Document.QuirksMode.noQuirks, doc.quirksMode());
        
        doc.quirksMode(Document.QuirksMode.quirks);
        assertEquals(Document.QuirksMode.quirks, doc.quirksMode());
        
        doc.quirksMode(Document.QuirksMode.limitedQuirks);
        assertEquals(Document.QuirksMode.limitedQuirks, doc.quirksMode());
    }
}