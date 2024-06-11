import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanerTest {
    @Test
    public void testCleanMethod() {
        Whitelist whitelist = Whitelist.simpleTextOnly();
        Cleaner cleaner = new Cleaner(whitelist);
        String htmlInput = "<html><body><p>Hello World!</p></body></html>";
        Document dirtyDocument = Jsoup.parse(htmlInput);
        Document cleanDocument = cleaner.clean(dirtyDocument);
        String cleanHtmlOutput = cleanDocument.body().html();
        assertEquals("<p>Hello World!</p>", cleanHtmlOutput);
    }
}