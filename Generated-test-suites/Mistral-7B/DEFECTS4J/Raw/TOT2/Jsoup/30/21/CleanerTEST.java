import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CleanerTest {

    @Test
    public void testConstructor() {
        Whitelist whitelist = Whitelist.simple();
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }
}