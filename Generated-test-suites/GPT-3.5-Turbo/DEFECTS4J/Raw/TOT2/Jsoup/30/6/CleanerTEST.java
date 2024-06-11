import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("div").text("Hello, World!");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("http://example.com", cleanDocument.baseUri());
        assertEquals(1, cleanDocument.body().children().size());
        assertTrue(cleanDocument.body().child(0).tagName().equals("div"));
        assertTrue(cleanDocument.body().child(0).text().equals("Hello, World!"));
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("script").text("alert('XSS attack')");

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Document.createShell("http://example.com");
        dirtyDocument.body().appendElement("div").text("Hello, World!");

        Document cleanDocument = Document.createShell("http://example.com");
        cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());

        assertEquals(1, cleanDocument.body().children().size());
        assertTrue(cleanDocument.body().child(0).tagName().equals("div"));
        assertTrue(cleanDocument.body().child(0).text().equals("Hello, World!"));
    }

    @Test
    public void testCreateSafeElement() {
        ElementMeta elementMeta = cleaner.createSafeElement(Document.createShell("http://example.com").body().appendElement("a").attr("href", "https://www.example.com"));

        assertEquals("a", elementMeta.el.tagName());
        assertEquals(1, elementMeta.numAttribsDiscarded);
        assertEquals("https://www.example.com", elementMeta.el.absUrl("href"));
    }
}