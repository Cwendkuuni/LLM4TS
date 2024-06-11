import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = new Document("<html><body><p>Hello, world!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertTrue(cleanDocument.body().html().contains("Hello, world!"));
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><body><p>Hello, world!</p></body></html>");
        
        assertTrue(cleaner.isValid(dirtyDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Element sourceEl = new Element(Tag.valueOf("p"), "");
        
        ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}