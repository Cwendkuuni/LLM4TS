import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    
    private Cleaner cleaner;
    private Document dirtyDocument;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
        dirtyDocument = new Document("<html><head><title>Dirty Document</title></head><body><p>This is a dirty document</p></body></html>");
    }
    
    @Test
    public void testClean() {
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><p>This is a dirty document</p></html>", cleanedDocument.toString());
    }
    
    @Test
    public void testIsValid() {
        assertTrue(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Document clean = Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), clean.body());
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Document clean = Document.createShell(dirtyDocument.baseUri());
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());
        assertNotNull(elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}