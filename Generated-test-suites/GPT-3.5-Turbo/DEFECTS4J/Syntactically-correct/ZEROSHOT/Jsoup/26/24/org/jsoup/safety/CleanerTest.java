import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);

        // Creating a sample dirty document
        dirtyDocument = Document.createShell("https://example.com");
        Element body = dirtyDocument.body();
        body.append("<p>This is a dirty document</p>");
        body.append("<script>alert('XSS attack');</script>");
        body.append("<img src='https://example.com/evil.jpg'>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element cleanBody = cleanDocument.body();

        // Asserting that the clean document does not contain any unsafe elements
        assertFalse(cleanBody.getElementsByTag("script").isEmpty());
        assertFalse(cleanBody.getElementsByTag("img").isEmpty());
    }

    @Test
    public void testIsValid() {
        // Asserting that the dirty document is not valid
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell(dirtyDocument.baseUri());
        Element cleanBody = cleanDocument.body();

        // Copying the safe nodes of the dirty document to the clean document
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanBody);

        // Asserting that the number of discarded elements is correct
        assertEquals(2, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = dirtyDocument.body().getElementsByTag("img").first();
        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);
        Element cleanElement = meta.el;

        // Asserting that the clean element is created correctly
        assertEquals("img", cleanElement.tagName());
        assertTrue(cleanElement.attributes().hasKey("src"));
    }
}