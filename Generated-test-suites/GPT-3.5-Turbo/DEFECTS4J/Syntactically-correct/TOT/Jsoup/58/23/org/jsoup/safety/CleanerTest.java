import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("");
        Element body = new Element("body");
        body.appendChild(new Element("div").text("Hello, World!"));
        body.appendChild(new TextNode("This is a test."));
        dirtyDocument.appendChild(body);
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Elements elements = cleanDocument.body().children();
        
        assertEquals(2, elements.size());
        assertEquals("div", elements.get(0).tagName());
        assertEquals("Hello, World!", elements.get(0).text());
        assertEquals("This is a test.", elements.get(1).text());
    }

    @Test
    public void testIsValid() {
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testClean_emptyBody() {
        dirtyDocument.body().empty();
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testClean_framesetDocument() {
        // Create a frameset document without a body
        dirtyDocument = new Document("");
        Element frameset = new Element("frameset");
        dirtyDocument.appendChild(frameset);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument.body());
    }

    @Test
    public void testClean_invalidTag() {
        dirtyDocument.body().appendChild(new Element("script")
                .attr("src", "https://www.example.com/malicious-script.js"));

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().children().isEmpty());
    }

    @Test
    public void testClean_invalidAttribute() {
        dirtyDocument.body().child(0).attr("onclick", "alert('Hello!')");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertFalse(cleanDocument.body().children().isEmpty());
        assertFalse(cleanDocument.body().child(0).hasAttr("onclick"));
    }
}