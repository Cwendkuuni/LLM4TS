import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanerTest {
    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Dirty Document</p></body></html>");
        Document clean = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><p>Dirty Document</p></body></html>", clean.toString());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Dirty Document</p></body></html>");
        boolean valid = cleaner.isValid(dirtyDocument);
        assertEquals(true, valid);
    }

    @Test
    public void testCopySafeNodes() {
        Document dirtyDocument = Jsoup.parse("<html><body><p>Dirty Document</p></body></html>");
        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), clean.body());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        assertEquals("p", meta.el.tagName());
        assertEquals(0, meta.numAttribsDiscarded);
    }
}