import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    
    private Cleaner cleaner;
    
    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }
    
    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = Jsoup.parse("<html><head></head><body><div>Hello</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertEquals("Hello", cleanDocument.body().text());
        
        // Edge case: empty body
        Document emptyDocument = Jsoup.parse("<html><head></head><body></body></html>");
        Document cleanEmptyDocument = cleaner.clean(emptyDocument);
        assertEquals("", cleanEmptyDocument.body().text());
        
        // Error scenario: null input
        try {
            cleaner.clean(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
    
    @Test
    public void testIsValid() {
        // Typical use case
        Document validDocument = Jsoup.parse("<html><head></head><body><div>Hello</div></body></html>");
        assertTrue(cleaner.isValid(validDocument));
        
        // Edge case: invalid tag
        Document invalidDocument = Jsoup.parse("<html><head></head><body><script>Malicious</script></body></html>");
        assertFalse(cleaner.isValid(invalidDocument));
        
        // Error scenario: null input
        try {
            cleaner.isValid(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
    
}