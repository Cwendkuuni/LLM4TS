import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
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
        Document dirtyDoc = new Document("");
        dirtyDoc.body().appendChild(Tag.valueOf("div"));

        Document cleanDoc = cleaner.clean(dirtyDoc);

        assertEquals(1, cleanDoc.body().children().size());
    }

    @Test
    public void testIsValid() {
        Document dirtyDoc = new Document("");
        dirtyDoc.body().appendChild(Tag.valueOf("div"));

        assertFalse(cleaner.isValid(dirtyDoc));
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDoc = new Document("");
        dirtyDoc.body().appendChild(Tag.valueOf("div"));

        Document cleanDoc = Document.createShell("");
        int discarded = cleaner.copySafeNodes(dirtyDoc.body(), cleanDoc.body());

        assertEquals(0, discarded);
        assertEquals(1, cleanDoc.body().children().size());
    }

    @Test
    public void testCreateSafeElement() {
        Document dirtyDoc = new Document("");
        dirtyDoc.body().appendChild(Tag.valueOf("a"));
        dirtyDoc.body().getElementsByTag("a").first().attributes().put("href", "http://example.com");

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyDoc.body().getElementsByTag("a").first());

        assertNotNull(elementMeta);
    }

}