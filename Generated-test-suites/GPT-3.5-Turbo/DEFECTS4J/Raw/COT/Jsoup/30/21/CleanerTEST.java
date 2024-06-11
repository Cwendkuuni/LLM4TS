
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = Jsoup.parse("<div><script>alert('XSS Attack');</script><p>Hello, World!</p></div>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element div = cleanDocument.getElementsByTag("div").first();

        assertNotNull(cleanDocument);
        assertEquals("<div><p>Hello, World!</p></div>", div.outerHtml());
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = dirtyDocument.getElementsByTag("div").first();
        Element destination = new Element("div");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(1, numDiscarded);
        assertTrue(destination.childNodes().size() > 0);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = dirtyDocument.getElementsByTag("div").first();
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertNotNull(elementMeta);
        assertNotNull(elementMeta.el);
    }
}
