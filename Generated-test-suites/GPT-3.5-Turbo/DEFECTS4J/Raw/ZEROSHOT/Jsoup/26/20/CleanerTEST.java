import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        Whitelist whitelist = new Whitelist();
        cleaner = new Cleaner(whitelist);
    }
    
    @Test
    public void testClean() {
        // Create a dirty document
        Document dirtyDocument = new Document("");
        
        // Call the clean method
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Assert that the clean document is not null
        assertNotNull(cleanDocument);
    }
    
    @Test
    public void testIsValid() {
        // Create a dirty document
        Document dirtyDocument = new Document("");
        
        // Call the isValid method
        boolean valid = cleaner.isValid(dirtyDocument);
        
        // Assert that the document is valid
        assertTrue(valid);
    }
    
    @Test
    public void testCopySafeNodes() {
        // Create source and destination elements
        Element source = new Element(Tag.valueOf("div"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        
        // Call the copySafeNodes method
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        // Assert that the number of discarded elements is 0
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        // Create a source element with attributes
        Element sourceEl = new Element(Tag.valueOf("div"), "");
        sourceEl.attr("class", "test");
        
        // Call the createSafeElement method
        ElementMeta meta = cleaner.createSafeElement(sourceEl);
        
        // Assert that the element is not null
        assertNotNull(meta.el);
        
        // Assert that the number of discarded attributes is 0
        assertEquals(0, meta.numAttribsDiscarded);
    }
}