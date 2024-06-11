import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        String dirtyHtml = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parseBodyFragment(dirtyHtml);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        assertNotNull(cleanDocument);
    }
}