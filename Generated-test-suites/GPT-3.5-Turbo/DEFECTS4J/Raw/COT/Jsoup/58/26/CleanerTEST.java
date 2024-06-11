import org.junit.Test;
import org.jsoup.safety.*;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        // Create a test dirty document
        String dirtyHTML = "<html><body><div><p>Dirty text</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);

        // Create a Cleaner object with a whitelist
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test if the cleaned document only contains allowed elements
        assertTrue(cleanDocument.getElementsByTag("html").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("body").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("div").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("p").size() == 1);
    }

    @Test
    public void testIsValid() {
        // Create a test dirty document
        String dirtyHTML = "<html><body><div><p>Dirty text</p></div></body></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);

        // Create a Cleaner object with a whitelist
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Check if the dirty document is considered valid
        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCleanEmptyDocument() {
        // Create an empty dirty document
        Document dirtyDocument = new Document("");

        // Create a Cleaner object with a whitelist
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the empty dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test if the clean document is also empty
        assertTrue(cleanDocument.body().children().isEmpty());
    }

    // Test edge case: Document with frameset
    @Test
    public void testCleanFramesetDocument() {
        // Create a dirty document with frameset
        String dirtyHTML = "<html><frameset><frame src='example.html'></frameset></html>";
        Document dirtyDocument = Jsoup.parse(dirtyHTML);

        // Create a Cleaner object with a whitelist
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean the dirty document
        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Test if the clean document only contains allowed elements
        assertTrue(cleanDocument.getElementsByTag("html").size() == 1);
        assertTrue(cleanDocument.getElementsByTag("frameset").isEmpty());
        assertTrue(cleanDocument.getElementsByTag("frame").isEmpty());
    }

    // Test exception handling: null document
    @Test(expected = IllegalArgumentException.class)
    public void testCleanNullDocument() {
        // Create a Cleaner object with a whitelist
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Clean a null document
        cleaner.clean(null);
    }

    // Test exception handling: null document
    @Test(expected = IllegalArgumentException.class)
    public void testIsValidNullDocument() {
        // Create a Cleaner object with a whitelist
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        // Check validity of a null document
        cleaner.isValid(null);
    }
}