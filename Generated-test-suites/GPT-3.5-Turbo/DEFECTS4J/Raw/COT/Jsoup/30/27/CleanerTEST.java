
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Document cleaned = cleaner.clean(dirtyDocument);

        assertNotNull(cleaned);
        assertEquals(cleaned.baseUri(), dirtyDocument.baseUri());
        assertEquals(cleaned.body().html(), "");
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(numDiscarded, 0);
        assertNotNull(destination);
        assertEquals(destination.tagName(), "div");
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
        assertEquals(elementMeta.el.tagName(), "div");
        assertEquals(elementMeta.numAttribsDiscarded, 0);
    }

}
