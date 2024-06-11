import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    
    private Cleaner cleaner;

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = new Document("<html><body><div>Dirty document</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("<html><head></head><body><div>Dirty document</div></body></html>", cleanDocument.toString());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        
        Document validDocument = new Document("<html><body><div>Valid document</div></body></html>");
        Document invalidDocument = new Document("<html><body><script>alert('XSS attack')</script></body></html>");
        
        assertTrue(cleaner.isValid(validDocument));
        assertTrue(!cleaner.isValid(invalidDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        
        Element root = new Element(Tag.valueOf("div"), "");
        Element destination = new Element(Tag.valueOf("body"), "");
        
        int numDiscarded = cleaner.copySafeNodes(root, destination);
        
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertEquals(sourceEl, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}