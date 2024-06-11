import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CleanerTest {
    
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basicWithImages();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<p>This is a <b>dirty</b> document</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Cleaned document should not be null", true, cleanDocument != null);
        assertEquals("Cleaned document should not contain any unsafe elements", 0, 
                     cleanDocument.select("b").size());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document validDocument = Jsoup.parse("<p>This is a valid HTML document</p>");
        Document invalidDocument = Jsoup.parse("<script>alert('XSS')</script>");
        
        assertEquals("Valid document should return true", true, cleaner.isValid(validDocument));
        assertEquals("Invalid document should return false", false, cleaner.isValid(invalidDocument));
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = Jsoup.parse("<div><img src='image.jpg' alt='An image'></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        assertEquals("Cleaned document should contain safe nodes", 1, cleanDocument.select("img").size());
    }

}