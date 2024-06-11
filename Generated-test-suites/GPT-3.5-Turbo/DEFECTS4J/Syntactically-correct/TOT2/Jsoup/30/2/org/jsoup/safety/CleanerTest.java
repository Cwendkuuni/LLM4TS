import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    private final String dirtyHtml = "<html><head><title>Test Page</title></head><body><div><p>Hello, World!</p></div></body></html>";
    private final Whitelist whitelist = Whitelist.basic();
    private final Cleaner cleaner = new Cleaner(whitelist);

    @Test
    public void testClean() {
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertTrue(cleanDocument.body().hasText());
        assertEquals("<p>Hello, World!</p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertFalse(cleanDocument.body().hasAttr("id"));
        assertFalse(cleanDocument.body().hasAttr("class"));
    }

    @Test
    public void testCreateSafeElement() {
        Document dirtyDocument = Parser.parseBodyFragment(dirtyHtml, "");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument.body());
        assertFalse(cleanDocument.body().hasAttr("style"));
    }
}