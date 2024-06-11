import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><a href='#'>Link</a></div>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        // Assert the clean document contains only allowed elements
        // Add appropriate assertions here
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><a href='#'>Link</a></div>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        // Assert the validity of the document
        // Add appropriate assertions here
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><a href='#'>Link</a></div>");
        Document cleanDocument = Document.createShell("example.com");
        int numDiscarded = cleaner.copySafeNodes(dirtyDocument.body(), cleanDocument.body());
        // Assert the number of discarded elements
        // Add appropriate assertions here
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parseBodyFragment("<div><a href='#'>Link</a></div>");
        ElementMeta elementMeta = cleaner.createSafeElement(dirtyDocument.body());
        // Assert the creation of safe element
        // Add appropriate assertions here
    }
}