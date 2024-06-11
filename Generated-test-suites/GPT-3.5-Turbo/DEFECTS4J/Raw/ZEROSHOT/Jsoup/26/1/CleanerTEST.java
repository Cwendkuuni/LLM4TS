import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setUp() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><p>Dirty Content</p></body></html>");

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body><p>Dirty Content</p></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><p>Dirty Content</p></body></html>");

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals(source, dest);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}