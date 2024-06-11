import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
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
        dirtyDocument.body().appendChild(new Tag("p"));
        dirtyDocument.body().appendChild(new Tag("div"));
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals(2, cleanDocument.body().children().size());
    }

    @Test
    public void testIsValid() {
        Document validDocument = new Document("");
        validDocument.body().appendChild(new Tag("p"));
        validDocument.body().appendChild(new Tag("a").attr("href", "http://example.com"));
        assertTrue(cleaner.isValid(validDocument));

        Document invalidDocument = new Document("");
        invalidDocument.body().appendChild(new Tag("script").attr("src", "http://example.com"));
        assertFalse(cleaner.isValid(invalidDocument));
    }

    // Edge case testing
    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = new Document("");
        dirtyDocument.body().appendChild(new Tag("p"));
        dirtyDocument.body().appendChild(new Tag("div"));

        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
    }

    // Exception handling testing
    @Test(expected = NullPointerException.class)
    public void testCreateSafeElementWithNullElement() {
        cleaner.createSafeElement(null);
    }

}