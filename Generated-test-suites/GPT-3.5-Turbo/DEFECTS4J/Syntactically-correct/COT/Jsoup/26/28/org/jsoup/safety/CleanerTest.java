import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CleanerTest {
    private Cleaner cleaner;
    
    private Document dirtyDocument;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(new Whitelist());
        dirtyDocument = new Document("<html><head></head><body><div>test</div></body></html>");
    }
    
    @Test
    public void testClean() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("<html><head></head><body><div>test</div></body></html>", cleanDocument.html());
    }
    
    @Test
    public void testIsValid() {
        boolean valid = cleaner.isValid(dirtyDocument);
        assertTrue(valid);
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
        Element sourceElement = new Element(Tag.valueOf("div"), "");
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElement);
        assertEquals(sourceElement, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }
}