import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.safety.Whitelist;

public class CleanerTest {
    
    private Cleaner cleaner;

    @Before
    public void setUp() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        // Typical use case
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello, <a href='example.com'>example link</a></p>");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        // Assert cleanedDocument contains only allowed elements
    }

    @Test
    public void testIsValid() {
        // Typical use case
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello, <a href='example.com'>example link</a></p>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        // Assert isValid is true as all elements and attributes are allowed
    }

    @Test
    public void testClean_WithFramesetDocument() {
        // Edge case - dirtyDocument is a frameset document
        Document dirtyDocument = new Document("");
        dirtyDocument.appendElement("frameset");
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        // Assert cleanedDocument has an empty body
    }

    @Test
    public void testIsValid_WithInvalidDocument() {
        // Error scenario - dirtyDocument has disallowed elements
        Document dirtyDocument = new Document("");
        dirtyDocument.body().append("<p>Hello, <script>alert('xss')</script></p>");
        boolean isValid = cleaner.isValid(dirtyDocument);
        // Assert isValid is false as script tag is disallowed
    }
}