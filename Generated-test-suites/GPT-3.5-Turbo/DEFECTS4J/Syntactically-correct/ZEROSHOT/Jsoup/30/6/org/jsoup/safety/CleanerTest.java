import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String html = "<html><body><p>Hello, world!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String expected = "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>Hello, world!</p>\n" +
                " </body>\n" +
                "</html>";
        assertEquals(expected, cleanDocument.html());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        String html = "<html><body><p>Hello, world!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertTrue(isValid);
    }
}