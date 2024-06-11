import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("");
        dirtyDocument.append("<div><img src='image.jpg'></div>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<body>\n <div><img src=\"image.jpg\"></div>\n</body>\n", cleanDocument.toString());
    }

    @Test
    public void testIsValidValid() {
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidInvalid() {
        dirtyDocument.append("<script>alert('test');</script>");
        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Document cleanDocument = new Document("");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        assertEquals(0, numDiscarded);
    }

}