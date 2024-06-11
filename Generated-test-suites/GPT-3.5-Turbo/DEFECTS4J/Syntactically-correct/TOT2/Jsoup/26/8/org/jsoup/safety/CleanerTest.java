import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    private Cleaner cleaner;
    private Whitelist whitelist;

    @Before
    public void setUp() {
        whitelist = Whitelist.relaxed();
        cleaner = new Cleaner(whitelist);
    }

    @Test
    public void testClean() {
        Document dirtyDocument = Jsoup.parse("<p>Test <b>dirty</b> document</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        
        // perform assertions to test cleanDocument
    }

    @Test
    public void testIsValid() {
        Document dirtyDocument = Jsoup.parse("<p>Test <b>dirty</b> document</p>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        // perform assertions to test isValid
    }

    @Test
    public void testCopySafeNodes() {
        // Create sample Element and test the copySafeNodes method
    }

    @Test
    public void testCreateSafeElement() {
        // Create sample Element and test the createSafeElement method
    }
}