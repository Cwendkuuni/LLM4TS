import static org.junit.Assert.*;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanerClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // Check if cleanDocument is not null
        assertNotNull(cleanDocument);
        
        // Check if the body tag is preserved
        assertTrue(cleanDocument.body() != null);
        
        // Check if the title tag is removed
        assertFalse(cleanDocument.select("title").size() > 0);
    }

    @Test
    public void testCleanerIsValid() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // Check if the dirtyDocument is considered valid
        assertTrue(isValid);
    }

    @Test
    public void testCleaningVisitor() {
        Cleaner cleaner = new Cleaner(Whitelist.none());
        Element source = new Element(Tag.valueOf("p"), "");
        Element dest = new Element(Tag.valueOf("div"), "");
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        // Check if numDiscarded is 0 for elements that are not allowed by the whitelist
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(Whitelist.simpleText());
        Element source = new Element(Tag.valueOf("div"), "");
        ElementMeta elementMeta = cleaner.createSafeElement(source);
        
        // Check if the elementMeta is not null
        assertNotNull(elementMeta);
        
        // Check if the attributes are copied correctly
        assertEquals(0, elementMeta.numAttribsDiscarded);
    }

}