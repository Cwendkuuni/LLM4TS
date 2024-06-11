import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import static org.junit.Assert.*;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<p>Hello, <script>alert('XSS');</script>World!</p>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<p>Hello, World!</p>", cleanDocument.body().html());
    }

    @Test
    public void testIsValidValidDocument() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<p>Hello, World!</p>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidInvalidDocument() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Parser.parseBodyFragment("<p>Hello, <script>alert('XSS');</script>World!</p>");
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    // Add more test cases for edge cases and exceptions as needed

}