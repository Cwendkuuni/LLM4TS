import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendElement("p");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().size() == 1);
        assertEquals("p", cleanDocument.body().children().get(0).tagName());
    }

    @Test
    public void testIsValidWithValidDocument() {
        Document validDocument = new Document("");
        validDocument.body().appendElement("a").attr("href", "https://www.example.com");

        assertTrue(cleaner.isValid(validDocument));
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        Document invalidDocument = new Document("");
        invalidDocument.body().appendElement("script").text("alert('XSS')");

        assertFalse(cleaner.isValid(invalidDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Element root = new Element("div", "");
        Element destination = new Element("div", "");
        
        root.appendElement("p");
        root.append(new Element("unknownTag"));
        root.appendText("Some text");
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertTrue(destination.children().size() == 1);
        assertEquals("p", destination.children().get(0).tagName());
        assertEquals(2, numDiscarded);
    }
}