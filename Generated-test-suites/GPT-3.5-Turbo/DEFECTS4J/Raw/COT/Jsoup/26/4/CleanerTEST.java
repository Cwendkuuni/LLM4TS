
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Test case 1: Test clean method with a valid HTML document
        Document dirtyDocument1 = new Document("<html><body><h1>Hello World!</h1></body></html>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        Document clean1 = cleaner1.clean(dirtyDocument1);
        String expectedOutput1 = "<html><head></head><body><h1>Hello World!</h1></body></html>";
        assertEquals(expectedOutput1, clean1.toString());

        // Test case 2: Test clean method with an empty HTML document
        Document dirtyDocument2 = new Document("<html><body></body></html>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        Document clean2 = cleaner2.clean(dirtyDocument2);
        String expectedOutput2 = "<html><head></head><body></body></html>";
        assertEquals(expectedOutput2, clean2.toString());
    }

    @Test
    public void testIsValid() {
        // Test case 1: Test isValid method with a valid HTML document
        Document dirtyDocument1 = new Document("<html><body><h1>Hello World!</h1></body></html>");
        Cleaner cleaner1 = new Cleaner(Whitelist.basic());
        boolean isValid1 = cleaner1.isValid(dirtyDocument1);
        assertTrue(isValid1);

        // Test case 2: Test isValid method with an HTML document containing an unsafe tag
        Document dirtyDocument2 = new Document("<html><body><script>alert('XSS')</script></body></html>");
        Cleaner cleaner2 = new Cleaner(Whitelist.basic());
        boolean isValid2 = cleaner2.isValid(dirtyDocument2);
        assertFalse(isValid2);
    }
}
