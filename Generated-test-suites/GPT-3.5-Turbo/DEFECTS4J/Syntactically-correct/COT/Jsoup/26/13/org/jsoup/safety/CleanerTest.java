import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Basic test case
        Document dirtyDocument = Document.createShell("http://example.com");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);

        // Test if original document is not modified
        assertEquals(dirtyDocument, cleaner.clean(dirtyDocument));
    }

    @Test
    public void testIsValid() {
        // Basic test case
        Document dirtyDocument = Document.createShell("http://example.com");
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);

        // Test with invalid tag
        dirtyDocument.body().appendElement("script");
        isValid = cleaner.isValid(dirtyDocument);
        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        // Basic test case
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);

        // Test with unsafe tag
        source.appendElement("script");
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertFalse(dest.children().isEmpty());

        // Test with unsafe attribute
        Element unsafeElement = new Element(Tag.valueOf("a"), "");
        unsafeElement.attr("onclick", "alert('XSS');");
        source.appendChild(unsafeElement);
        numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(1, numDiscarded);
        assertTrue(dest.select("a").isEmpty());
    }

    @Test
    public void testCreateSafeElement() {
        // Basic test case
        Element sourceEl = new Element(Tag.valueOf("a"), "");
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertNotNull(meta.el);
        assertEquals(0, meta.numAttribsDiscarded);

        // Test with unsafe attribute
        sourceEl.attr("onclick", "alert('XSS');");
        meta = cleaner.createSafeElement(sourceEl);
        assertNotNull(meta.el);
        assertEquals(1, meta.numAttribsDiscarded);
    }
}