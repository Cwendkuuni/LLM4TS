import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
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
        dirtyDocument = Document.createShell("");
        dirtyDocument.body().appendChild(Tag.valueOf("div"));
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("div", cleanDocument.body().child(0).tagName());
    }

    @Test
    public void testIsValidWithValidDocument() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(isValid);
    }

    @Test
    public void testIsValidWithInvalidDocument() {
        dirtyDocument.body().appendChild(Tag.valueOf("script"));
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertTrue(!isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
        assertEquals("div", cleanDocument.body().child(0).tagName());
    }

    @Test
    public void testCreateSafeElement() {
        Document cleanDocument = Document.createShell("");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);

        Document dirtyDocumentWithUnsafeAttr = Document.createShell("");
        dirtyDocumentWithUnsafeAttr.body().appendChild(Tag.valueOf("div"));
        dirtyDocumentWithUnsafeAttr.body().child(0).attr("onclick", "alert('XSS')");
        numDiscarded = cleaner.copySafeNodes(dirtyDocumentWithUnsafeAttr.body(), cleanDocument.body());
        assertEquals(1, numDiscarded);
    }
}