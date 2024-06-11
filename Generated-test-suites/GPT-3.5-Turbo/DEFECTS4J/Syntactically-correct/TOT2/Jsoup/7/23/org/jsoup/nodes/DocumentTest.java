import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class DocumentTest {
    
    private Document document;

    @Before
    public void setUp() {
        document = Document.createShell("http://example.com");
    }

    @Test
    public void testCreateShell() {
        Document shellDoc = Document.createShell("http://example.com");
        
        Elements html = shellDoc.getElementsByTag("html");
        Elements head = shellDoc.getElementsByTag("head");
        Elements body = shellDoc.getElementsByTag("body");

        assertEquals("html element should exist", 1, html.size());
        assertEquals("head element should exist", 1, head.size());
        assertEquals("body element should exist", 1, body.size());
    }

    @Test
    public void testHead() {
        Element head = document.head();

        assertNull("head element should be null in a new document", head);
        
        Element newHead = document.appendElement("head");
        Element retrievedHead = document.head();

        assertNotNull("head element should not be null after being added", retrievedHead);
        assertEquals("retrieved head element should be the same as the one added", newHead, retrievedHead);
    }

    @Test
    public void testBody() {
        Element body = document.body();

        assertNull("body element should be null in a new document", body);
        
        Element newBody = document.appendElement("body");
        Element retrievedBody = document.body();

        assertNotNull("body element should not be null after being added", retrievedBody);
        assertEquals("retrieved body element should be the same as the one added", newBody, retrievedBody);
    }

    // More test cases for other methods

}