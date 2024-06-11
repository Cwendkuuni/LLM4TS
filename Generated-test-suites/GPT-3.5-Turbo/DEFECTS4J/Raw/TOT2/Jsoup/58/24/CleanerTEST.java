import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("<html><body><div><p>Hello, World!</p></div></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertTrue(cleanDocument != null); // Ensure clean document is not null
        assertEquals("<html><head></head><body><div><p>Hello, World!</p></div></body></html>", cleanDocument.toString());
    }

    @Test
    public void testIsValid() {
        assertTrue(cleaner.isValid(dirtyDocument)); // Ensure the document is valid
    }

    @Test
    public void testCleaningVisitor() {
        Element sourceElement = new Element("div");
        Element destElement = new Element("span");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(sourceElement, destElement);
        cleaningVisitor.head(new Element("p"), 0);
        assertEquals(1, cleaningVisitor.numDiscarded);
    }

    @Test
    public void testCopySafeNodes() {
        Element sourceElement = new Element("div");
        Element destElement = new Element("span");
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Element sourceElement = new Element("a");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        assertTrue(elementMeta != null); // Ensure safe element is created
        assertEquals(1, elementMeta.numAttribsDiscarded);
    }
}