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
        Document dirtyDocument = new Document("<html><head></head><body><div>Hello World!</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertNotNull(cleanDocument);
        assertNotNull(cleanDocument.body());
        assertEquals(1, cleanDocument.body().children().size());
        assertEquals("div", cleanDocument.body().child(0).tagName());
        assertEquals("Hello World!", cleanDocument.body().child(0).text());
    }
    
    @Test
    public void testIsValid() {
        Document dirtyDocument = new Document("<html><head></head><body><div>Hello World!</div></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
    
    @Test
    public void testIsValid_Invalid() {
        Document dirtyDocument = new Document("<html><head></head><body><script>alert('XSS');</script></body></html>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }
}