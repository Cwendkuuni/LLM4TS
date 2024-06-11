import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
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
        Document dirtyDocument = new Document("<html><body><div><p>Hello</p></div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNotNull(cleanDocument);
        assertEquals("<html><head></head><body><div><p>Hello</p></div></body></html>", cleanDocument.outerHtml());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><script>alert('XSS')</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = new Element("<div><p>Hello</p></div>");
        Element dest = new Element("");

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(0, numDiscarded);
        assertEquals("<div><p>Hello</p></div>", dest.outerHtml());
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element("<p>Hello</p>");
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
        assertEquals("<p>Hello</p>", elementMeta.el.outerHtml());
    }
}