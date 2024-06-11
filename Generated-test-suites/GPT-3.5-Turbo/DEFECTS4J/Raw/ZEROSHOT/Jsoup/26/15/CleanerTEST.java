import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("<html><body><div><p>This is a test</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Element source = dirtyDocument.body();
        Element dest = new Element("div");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = dirtyDocument.body().child(0);
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        Element destElement = elementMeta.el;
        assertNotNull(destElement);
    }
}