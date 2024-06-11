import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;


public class CleanerTest {

    private Cleaner cleaner;
    private Document dirtyDocument;

    @Before
    public void setup() {
        Whitelist whitelist = Whitelist.basic();
        this.cleaner = new Cleaner(whitelist);
        this.dirtyDocument = Jsoup.parse("<html><head><title>Dirty Document</title></head><body><h1>Dirty</h1><p>Text</p></body></html>");
    }

    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><h1>Dirty</h1><p>Text</p></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        assertEquals(true, isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner.ElementMeta elementMeta = cleaner.new ElementMeta(null, 0);
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), elementMeta.el);
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}