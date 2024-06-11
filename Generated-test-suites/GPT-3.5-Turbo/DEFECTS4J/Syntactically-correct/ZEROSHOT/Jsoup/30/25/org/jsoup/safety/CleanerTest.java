import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><head></head><body></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("");
        Element body = new Element(Tag.valueOf("body"), "");
        dirtyDocument.appendChild(body);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertEquals(true, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("div"), "");

        int numDiscarded = cleaner.copySafeNodes(root, destination);

        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);

        assertEquals(sourceEl, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}