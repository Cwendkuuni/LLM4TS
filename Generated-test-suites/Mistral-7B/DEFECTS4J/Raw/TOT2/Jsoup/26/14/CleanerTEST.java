import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        Document dirtyDocument = Jsoup.parse("<html><body><script>alert('XSS')</script></body></html>");
        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }
}