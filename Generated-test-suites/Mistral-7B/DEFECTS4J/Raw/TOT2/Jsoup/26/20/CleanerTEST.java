import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Whitelist;
import org.jsoup.safety.Cleaner;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {
    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }
}