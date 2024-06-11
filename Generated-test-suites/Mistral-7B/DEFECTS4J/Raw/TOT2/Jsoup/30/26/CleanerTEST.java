import org.junit.jupitermk.junit4.TestFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@RunWith(TestFormat.class)
public class CleanerTest {

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist(); // some valid whitelist
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    @Test
    public void testCleanMethod() {
        Whitelist whitelist = new Whitelist(); // some valid whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='test'>Hello World!</div></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body></body></html>", cleanDocument.html());
        assertNotNull(cleanDocument);
    }

    @Test
    public void testIsValidMethod() {
        Whitelist whitelist = new Whitelist(); // some valid whitelist
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Jsoup.parse("<html><body><div id='test'>Hello World!</div></body></html>");
        assertTrue(cleaner.isValid(dirtyDocument));
        assertFalse(cleaner.isValid(Jsoup.parse("<html><body><script>alert('Hello!');</script></body></html>")));
    }

    // add test cases for other methods here
}