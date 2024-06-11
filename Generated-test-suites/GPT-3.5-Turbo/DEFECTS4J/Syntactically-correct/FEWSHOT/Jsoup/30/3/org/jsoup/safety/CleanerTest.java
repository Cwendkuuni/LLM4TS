import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

public class CleanerTest {
    private Cleaner cleaner;

    @Before
    public void setup() {
        cleaner = new Cleaner(Whitelist.basic());
    }

    @Test
    public void testClean() {
        String dirtyHtml = "<html><body><script>alert('XSS')</script><p>Hello, World!</p></body></html>";
        Document dirtyDoc = Jsoup.parse(dirtyHtml);
        Document cleanedDoc = cleaner.clean(dirtyDoc);

        assertFalse(cleanedDoc.toString().contains("<script>alert('XSS')</script>"));
        assertTrue(cleanedDoc.toString().contains("<p>Hello, World!</p>"));
    }

    @Test
    public void testIsValid() {
        String validHtml = "<html><body><p>Hello, World!</p></body></html>";
        Document validDoc = Jsoup.parse(validHtml);
        assertTrue(cleaner.isValid(validDoc));

        String invalidHtml = "<html><body><script>alert('XSS')</script><p>Hello, World!</p></body></html>";
        Document invalidDoc = Jsoup.parse(invalidHtml);
        assertFalse(cleaner.isValid(invalidDoc));
    }
}